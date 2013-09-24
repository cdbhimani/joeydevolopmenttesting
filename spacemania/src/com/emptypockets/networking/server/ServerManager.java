package com.emptypockets.networking.server;

import java.io.IOException;

import com.emptyPockets.commandline.CommandLine;
import com.emptyPockets.logging.Console;
import com.emptypockets.engine.Engine;
import com.emptypockets.networking.NetworkProperties;
import com.emptypockets.networking.controls.CommandService;
import com.emptypockets.networking.log.ServerLogger;
import com.emptypockets.networking.transfer.ClientLoginRequest;
import com.emptypockets.networking.transfer.ClientLogoutRequest;
import com.emptypockets.networking.transfer.ClientStateTransferObject;
import com.emptypockets.networking.transfer.LoginFailedResponse;
import com.emptypockets.networking.transfer.LoginSucessfulResponse;
import com.emptypockets.networking.transfer.NetworkProtocall;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerManager extends Listener implements Runnable {
	int clientCount= 0;
	public static final int DEFAULT_SERVER_UPDATE = 10;
	String name = "Server";
	Server server;
	long lastEngineUpdate = 0;
	long lastBroadcase = 0;
	Engine engine;
	Thread thread;
	int maxUpdateCount = 0;
	boolean alive = false;
	CommandLine command;
	int udpPort = NetworkProperties.udpPort;
	int tcpPort = NetworkProperties.tcpPort;

	public ServerManager() {
		this(DEFAULT_SERVER_UPDATE);
	}

	public ServerManager(int maxUpdateCount) {
		setMaxUpdateCount(maxUpdateCount);
		setupServer();
		engine = new Engine();
		command = new CommandLine();
		CommandService.registerServer(this);
		NetworkProtocall.register(server.getKryo());
	}

	public void setupServer() {
		server = new Server() {
			@Override
			protected Connection newConnection() {
				Console.println("Connection Recieved");
				return new ClientConnection();
			}
		};
		server.start();
		server.addListener(this);
	}

	public void start() throws IOException {
		Console.printf("Starting Server [%d,%d]\n", tcpPort, udpPort);
		server.bind(tcpPort, udpPort);
		thread = new Thread(this);
		alive = true;
		thread.start();

	}

	public void stop() {
		Console.println("Stopping Server");
		server.stop();
		alive = false;
		thread = null;
	}

	public void clientExit(String name) {
		Console.println("Client Exit : "+name);
		ServerLogger.info(name, "Client Exit : " + name);
		synchronized (engine) {
			engine.removeEntity(name);
		}
	}

	public int clientJoin(String name) {
		Console.println("Client Join : "+name);
		ServerLogger.info(name, "Client Join : " + name);
		int id;
		synchronized (engine) {
			id=clientCount++;
			engine.addEntity(id,name);
		}
		return id;
	}

	private void clientStateRecieved(String name, ClientStateTransferObject object) {
		synchronized (engine) {
			engine.updateEntityVel(name, object.valueX, object.valueY);
		}
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		if (connection instanceof ClientConnection) {
			clientExit(((ClientConnection) connection).getUsername());
		}
	}

	public void update() {
		synchronized (engine) {
			if (lastEngineUpdate == 0) {
				lastEngineUpdate = System.currentTimeMillis();
				return;
			}
			float time = (System.currentTimeMillis() - lastEngineUpdate) * 1e-3f;
			engine.update(time);
			engine.tick();
			lastEngineUpdate = System.currentTimeMillis();
		}
	}

	public void broadcast() {
		synchronized (engine) {
			Connection[] con = server.getConnections();
			for (int i = 0; i < con.length; i++) {
				if (con[i] instanceof ClientConnection) {
					ClientConnection c = (ClientConnection) con[i];
					if(c.username != null){
						c.sendUDP(engine);
					}
				}
			}
//			server.sendToAllTCP(engine);
		}
	}

	public ClientConnection isUserConnected(String userName) {
		Connection[] con = server.getConnections();
		for (int i = 0; i < con.length; i++) {
			if (con[i] instanceof ClientConnection) {
				ClientConnection c = (ClientConnection) con[i];
				if (c.getUsername()!= null && userName != null && c.getUsername().equalsIgnoreCase(userName)) {
					return c;
				}
			}
		}
		return null;
	}

	@Override
	public void received(Connection newConnection, Object object) {
		super.received(newConnection, object);
		if (object instanceof ClientStateTransferObject) {
			clientStateRecieved(((ClientConnection) newConnection).getUsername(), (ClientStateTransferObject) object);
		} else if (object instanceof ClientLoginRequest) {
			ClientLoginRequest req = ((ClientLoginRequest) object);
			ClientConnection con = isUserConnected(req.getUsername());
			if (con == null) {
				//Login user
				con = ((ClientConnection) newConnection);
				con.setUsername(req.getUsername());
				
				clientJoin(req.getUsername());
				
				LoginSucessfulResponse resp = new LoginSucessfulResponse();
				resp.setServerClientid(con.getID());
				newConnection.sendTCP(resp);
			}else{
				Console.println("User ["+req.getUsername()+"] attempted to connect while already connected");
				LoginFailedResponse resp = new LoginFailedResponse();
				resp.setRejectionReason("User already logged in");
				newConnection.sendTCP(resp);
			}
		} else if (object instanceof ClientLogoutRequest) {
			clientExit(((ClientLogoutRequest) object).getUsername());
		}
	}

	@Override
	public void run() {
		long diff;
		float desired;
		Console.println("Starting started");
		while (alive) {
			lastBroadcase = System.currentTimeMillis();
			update();
			broadcast();
			diff = System.currentTimeMillis() - lastBroadcase;
			desired = 1000f / maxUpdateCount;
			if (diff < desired) {
				try {
					Thread.sleep((long) (desired - diff));
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void logStatus() {
		ServerLogger.info(name, "Server Running [" + alive + "] Connected [" + server.getConnections().length + "]");
	}

	public void setMaxUpdateCount(int maxUpdateCount) {
		this.maxUpdateCount = maxUpdateCount;
	}

	public CommandLine getCommand() {
		return command;
	}

	public void setCommand(CommandLine command) {
		this.command = command;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

}
