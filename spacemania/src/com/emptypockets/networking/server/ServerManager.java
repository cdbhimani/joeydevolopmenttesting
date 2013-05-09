package com.emptypockets.networking.server;

import java.io.IOException;

import com.emptypockets.engine.Engine;
import com.emptypockets.networking.controls.CommandHub;
import com.emptypockets.networking.controls.CommandService;
import com.emptypockets.networking.log.ServerLogger;
import com.emptypockets.networking.transfer.ClientLoginRequest;
import com.emptypockets.networking.transfer.ClientLogoutRequest;
import com.emptypockets.networking.transfer.ClientStateTransferObject;
import com.emptypockets.networking.transfer.NetworkProtocall;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerManager extends Listener implements Runnable {
	public static final int DEFAULT_SERVER_UPDATE = 30;
	String name = "Server";
	Server server;
	long lastEngineUpdate = 0;
	long lastBroadcase = 0;
	Engine engine;
	Thread thread;
	int maxUpdateCount = 0;
	boolean alive = false;
	CommandHub command;
	int udpPort = 8081;
	int tcpPort = 8080;

	public ServerManager(){
		this(DEFAULT_SERVER_UPDATE);
	}
	public ServerManager(int maxUpdateCount) {
		setMaxUpdateCount(maxUpdateCount);
		setupServer();
		engine = new Engine();
		command = new CommandHub();
		CommandService.registerServer(this);
		NetworkProtocall.register(server.getKryo());
	}

	public void setupServer() {
		server = new Server() {
			@Override
			protected Connection newConnection() {
				return new ClientConnection();
			}
		};
		server.start();
		server.addListener(this);
	}

	public void start() throws IOException {
		server.bind(tcpPort, udpPort);
		thread = new Thread(this);
		alive = true;
		thread.start();

	}

	public void stop() {
		server.stop();
		alive = false;
		thread = null;
	}

	public void clientExit(String name) {
		ServerLogger.info(name, "Client Exit : "+name);
		synchronized (engine) {
			engine.removeEntity(name);
		}
	}

	public void clientJoin(String name) {
		ServerLogger.info(name, "Client Join : "+name);
		synchronized (engine) {
			engine.addEntity(name);
		}
	}

	private void clientStateRecieved(String name, ClientStateTransferObject object) {
		synchronized (engine) {
			engine.updateEntityVel(name, object.velX, object.velY);
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
			server.sendToAllTCP(engine);
		}
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		if (object instanceof ClientStateTransferObject) {
			clientStateRecieved(((ClientConnection) connection).getUsername(), (ClientStateTransferObject) object);
		} else if (object instanceof ClientLoginRequest) {
			((ClientConnection) connection).setUsername(((ClientLoginRequest)object).getUsername());
			clientJoin(((ClientLoginRequest)object).getUsername());
		}else if (object instanceof ClientLogoutRequest) {
			clientExit(((ClientLogoutRequest)object).getUsername());
		}
	}

	@Override
	public void run() {
		long diff;
		float desired;
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

	public void logStatus(){
		ServerLogger.info(name, "Server Running ["+alive+"] Connected ["+server.getConnections().length+"]");
	}
	public void setMaxUpdateCount(int maxUpdateCount) {
		this.maxUpdateCount = maxUpdateCount;
	}

	public CommandHub getCommand() {
		return command;
	}

	public void setCommand(CommandHub command) {
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