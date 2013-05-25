package com.emptypockets.network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

import com.emptypockets.network.connection.UDPConnection;
import com.emptypockets.network.connection.UDPConnectionListener;
import com.emptypockets.network.controls.CommandHub;
import com.emptypockets.network.controls.CommandService;
import com.emptypockets.network.log.ServerLogger;
import com.emptypockets.network.transport.FrameworkMessages;
import com.emptypockets.network.transport.TransportObject;
import com.emptypockets.network.transport.FrameworkMessages.ConnectionRequest;
import com.emptypockets.network.transport.FrameworkMessages.ConnectionResponse;
import com.emptypockets.network.transport.FrameworkMessages.DisconnectNotification;
import com.emptypockets.network.transport.FrameworkMessages.Ping;

public class NetworkNode implements UDPConnectionListener, Runnable {
	static int nodeCount = 0;
	String nodeName = "Node" + nodeCount++;

	private ArrayList<NetworkConnection> timeoutNodess;
	private ArrayList<NetworkConnection> clients;
	private HashMap<Integer, NetworkConnection> clientsById;
	private HashMap<String, NetworkConnection> clientsByNodeName;
	Thread thread;
	boolean active = false;

	ArrayList<NetworkNodeListener> listeners;

	private UDPConnection connection;
	int localPort;
	int maxPacketSize;

	CommandHub commandHub;

	public NetworkNode(int localPort, int maxPacketSize) {
		this.localPort = localPort;
		this.maxPacketSize = maxPacketSize;
		listeners = new ArrayList<NetworkNodeListener>();
		commandHub = new CommandHub();
		init();
	}

	public void init() {
		timeoutNodess = new ArrayList<NetworkConnection>();
		clients = new ArrayList<NetworkConnection>();
		clientsById = new HashMap<Integer, NetworkConnection>();
		clientsByNodeName = new HashMap<String, NetworkConnection>();

		CommandService.registerServer(this);
		addListener(commandHub);
	}

	public void start() throws IOException {
		ServerLogger.info(nodeName, "Starting Network Node Port[" + localPort + "]");
		if (active) {
			return;
		}
		setConnection(new UDPConnection(this, maxPacketSize, localPort));
		getConnection().addListener(this);

		active = true;
		Thread t = new Thread(this);
		t.start();
		getConnection().start();
	}

	public void stop() {
		ServerLogger.info(nodeName, "Stopping Network Node");
		disconnectAllConnections();
		active = false;
		if (getConnection() != null) {
			getConnection().stop();
		}
		setConnection(null);
	}

	public void disconnect(String name) {
		ServerLogger.info(nodeName, "Disconnect Node Requested [" + name + "]");
		synchronized (clients) {
			NetworkConnection con = clientsByNodeName.get(name);
			if (con != null) {
				sendData(con, new DisconnectNotification(this.nodeName));
				nodeDisconnectRecieved(con);
			} else {
				ServerLogger.error(nodeName, "No node to disconnect");
			}
		}
	}

	public void disconnectAllConnections() {
		sendDataToAll(new DisconnectNotification(this.nodeName));
		synchronized (clients) {
			clients.clear();
			clientsById.clear();
			clientsByNodeName.clear();
		}
	}

	public void addListener(NetworkNodeListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public void removeListener(NetworkNodeListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected void nodeConnectRecieved(NetworkConnection node) {
		ServerLogger.info(nodeName, "Node Connected : " + node);
		synchronized (clients) {
			clientsById.put(node.getClientId(), node);
			clientsByNodeName.put(node.getNodeName(), node);
			clients.add(node);
		}

		synchronized (listeners) {
			for (NetworkNodeListener list : listeners) {
				list.nodeConnected(node);
			}
		}

	}

	public boolean sendDataToAll(Object object) {
		boolean result = true;
		synchronized (clients) {
			for (NetworkConnection con : clients) {
				result = result && sendData(con, object);
			}
		}
		return result;
	}

	public boolean sendData(String nodeName, Object object) throws NodeNotFoundException {
		synchronized (clients) {
			NetworkConnection con = clientsByNodeName.get(nodeName);
			if (con == null) {
				throw new NodeNotFoundException("No such node found");
			}
			return sendData(con, object);
		}
	}

	public boolean sendData(NetworkConnection con, Object object) {
		return connection.sendObject(object, con);
	}

	protected void nodeDisconnectRecieved(NetworkConnection node) {
		ServerLogger.info(nodeName, "Node Disconnect : " + node);
		synchronized (clients) {
			clients.remove(node);
			clientsById.remove(node.getClientId());
			clientsByNodeName.remove(node.getNodeName());
		}

		synchronized (listeners) {
			for (NetworkNodeListener list : listeners) {
				list.nodeDisconnected(node);
			}
		}
	}

	protected void nodeTimeoutRecieved(NetworkConnection node) {
		ServerLogger.info(nodeName, "Node Timeout : " + node);
		synchronized (timeoutNodess) {
			timeoutNodess.add(node);
		}

		synchronized (listeners) {
			for (NetworkNodeListener list : listeners) {
				list.nodeTimeout(node);
			}
		}
	}

	@Override
	public void notifyObjectRecieved(UDPConnection con, TransportObject object) {
		ServerLogger.debug(nodeName, "Recieved : " + object);
		// Deal with Framework Messages
		if (object.data instanceof FrameworkMessages) {
			if (object.data instanceof Ping) {
				Ping p = (Ping) object.data;
				if (p.isResponse()) {
					NetworkConnection client = clientsById.get(p.getClientId());
					if (client != null) {
						client.getPinger().recievePing(p);
					}
				} else {
					p.setResponse(true);
					con.sendTransportObject(object);
				}
			} else if (object.data instanceof ConnectionRequest) {
				ConnectionRequest request = (ConnectionRequest) object.data;
				ServerLogger.info(nodeName, "Connection Request Recieved: " + request);
				NetworkConnection client = new NetworkConnection(request.getNodeName(), object.host, object.port);
				ConnectionResponse response = generateConnectionResponse(client);
				ServerLogger.debug(nodeName, "Connection Request Response : " + response);

				if (response.isAccepted()) {
					nodeConnectRecieved(client);
				}
				connection.sendObject(response, object.host, object.port);
			} else if (object.data instanceof ConnectionResponse) {
				ConnectionResponse response = (ConnectionResponse) object.data;
				NetworkConnection server = new NetworkConnection(response.getNodeName(), object.host, object.port);
				nodeConnectRecieved(server);
			} else if (object.data instanceof DisconnectNotification) {
				NetworkConnection clientToRemove;
				synchronized (clients) {
					clientToRemove = clientsByNodeName.get(((DisconnectNotification) object.data).getNodeName());
				}
				if (clientToRemove != null) {
					nodeDisconnectRecieved(clientToRemove);
				} else {
					ServerLogger.error(nodeName, "No node to disconnect");
				}

			} else {
				throw new InvalidParameterException("Unknown Framework Message");
			}
		} else {
			synchronized (clients) {
				NetworkConnection netCon = clientsByNodeName.get(object.nodeName);
				// Non Framework Data
				synchronized (listeners) {
					for (NetworkNodeListener list : listeners) {
						list.dataRecieved(this, netCon, object.data);
					}
				}

			}
		}
	}

	private ConnectionResponse generateConnectionResponse(NetworkConnection newClient) {
		ConnectionResponse resp = new ConnectionResponse();
		resp.setNodeName(this.nodeName);
		if (clientsByNodeName.containsKey(newClient.getNodeName())) {
			resp.setAccepted(false);
			resp.setMessage("Username already in use");
		} else {
			resp.setAccepted(true);
			resp.setServerClientId(newClient.getClientId());
		}
		return resp;
	}

	public UDPConnection getConnection() {
		return connection;
	}

	public void setConnection(UDPConnection connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		while (active) {
			synchronized (clients) {
				for (NetworkConnection c : clients) {
					c.update(this);
				}
			}

			// Remove clients That have timed out
			synchronized (timeoutNodess) {
				for (NetworkConnection c : timeoutNodess) {
					nodeDisconnectRecieved(c);
				}
				timeoutNodess.clear();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void connect(InetAddress address, int port) {
		ServerLogger.info(nodeName, "Attempting connection : " + address + ":" + port);
		ConnectionRequest request = new ConnectionRequest();
		request.setNodeName(nodeName);
		connection.sendObject(request, address, port);
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public void logClients() {
		ServerLogger.info(nodeName, "#########################");
		ServerLogger.info(nodeName, "Listing Nodes");
		ServerLogger.info(nodeName, "#########################");
		synchronized (clients) {
			for (NetworkConnection con : clients) {
				ServerLogger.info(nodeName, String.format("Name[%s] ping[%d] address[%s] port[%d]", con.getNodeName(), con.getPing(), con.getClientAddress(), con.getClientPort()));
			}
		}
		ServerLogger.info(nodeName, "#########################");
	}

	public CommandHub getCommandHub() {
		return commandHub;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public boolean isServerRunning() {
		return active;
	}

	public int getConnectedCount() {
		synchronized (clients) {
			return clients.size();
		}
	}

	public void logStatus() {
		ServerLogger.info(getNodeName(), "server[" + isServerRunning() + "] Connected[" + getConnectedCount() + "]");
		logClients();
	}
}
