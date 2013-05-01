package com.emptyPockets.network.server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import com.emptyPockets.network.connection.NetworkConnection;
import com.emptyPockets.network.connection.UDPConnection;
import com.emptyPockets.network.connection.UDPConnectionListener;
import com.emptyPockets.network.transport.FrameworkMessages.ConnectionRequest;
import com.emptyPockets.network.transport.FrameworkMessages.ConnectionResponse;
import com.emptyPockets.network.transport.FrameworkMessages.Ping;
import com.emptyPockets.network.transport.TransportObject;
import com.esotericsoftware.minlog.Log;

public class NetworkNode implements UDPConnectionListener, Runnable{
	static int nodeCount = 0;
	String nodeName = "Node"+nodeCount++;
	
	private ArrayList<NetworkConnection> timeoutClients;
	private ArrayList<NetworkConnection> clients;
	private HashMap<Integer, NetworkConnection> clientsById;
	private HashMap<String, NetworkConnection> clientsByUsername;
	Thread thread;
	boolean active = false;
	
	private UDPConnection connection;
	int localPort;
	int maxPacketSize;

	public NetworkNode(int localPort, int maxPacketSize) throws IOException {
		this.localPort = localPort;
		this.maxPacketSize = maxPacketSize;
		init();
	}

	public void init() throws IOException {
		setConnection(new UDPConnection(maxPacketSize, localPort));
		getConnection().addListener(this);
		timeoutClients = new ArrayList<NetworkConnection>();
		clients  = new ArrayList<NetworkConnection>();
		clientsById = new HashMap<Integer, NetworkConnection>();
		clientsByUsername = new HashMap<String, NetworkConnection>();
	}

	public void start() {
		if(active){
			return;
		}
		active = true;
		Thread t = new Thread(this);
		t.start();
		getConnection().start();
	}

	public void stop() {
		active = false;
		getConnection().stop();
	}

	public void clientConnectRecieved(NetworkConnection client){
		Log.info(nodeName, "Connection : "+client);
		synchronized (clients) {
			clientsById.put(client.getClientId(), client);
			clientsByUsername.put(client.getUsername(), client);
			clients.add(client);
		}
		
	}
	
	public void clientDisconnectRecieved(NetworkConnection client){
		Log.info(nodeName, "Disconnect : "+client);
		synchronized (clients) {
			clients.remove(client);
			clientsById.remove(client.getClientId());
			clientsByUsername.remove(client.getUsername());
		}
	}
	
	public void clientTimeoutRecieved(NetworkConnection client){
		Log.info(nodeName, "Timeout : "+client);
		synchronized (timeoutClients) {
			timeoutClients.add(client);	
		}
	}
	
	@Override
	public void notifyObjectRecieved(UDPConnection con, TransportObject object) {
		Log.info(nodeName, "Recieved : "+object);
		
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
		}else if(object.data instanceof ConnectionRequest){
			ConnectionRequest request = (ConnectionRequest) object.data;
			NetworkConnection client = new NetworkConnection(request, object.host, object.port);
			ConnectionResponse response = generateConnectionResponse(client);
			Log.info(nodeName, "Response : "+response);
			
			if(response.isAccepted()){
				clientConnectRecieved(client);
			}
			connection.sendObject(response, object.host, object.port);
		}
	}

	public ConnectionResponse generateConnectionResponse(NetworkConnection newClient){
		ConnectionResponse resp = new ConnectionResponse();
		
		if(clientsByUsername.containsKey(newClient.getUsername())){
			resp.setAccepted(false);
			resp.setMessage("Username already in use");
		}else{
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
		while(active){
			synchronized (clients) {
				for(NetworkConnection c : clients){
					c.update(this);
				}
			}
			
			synchronized (timeoutClients) {
				for(NetworkConnection c : timeoutClients){
					clientDisconnectRecieved(c);
				}
				timeoutClients.clear();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void connect(String username, InetAddress address, int port) {
		Log.info(nodeName, "Attempting connection : "+address+":"+port);
		ConnectionRequest request = new ConnectionRequest();
		request.setUsername(username);
		connection.sendObject(request, address, port);
	}
}
