package com.emptyPockets.test.kryoNetwork.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;

import com.emptyPockets.test.kryoNetwork.transport.Network;
import com.emptyPockets.test.kryoNetwork.transport.messages.EntityUpdate;
import com.emptyPockets.test.kryoNetwork.transport.messages.LoginRequest;
import com.emptyPockets.test.kryoNetwork.transport.messages.ServerStateRequest;
import com.emptyPockets.test.kryoNetwork.transport.messages.ServerStateResponse;
import com.emptyPockets.test.kryoNetwork.transport.messages.UserData;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class ManagementServer extends Listener {
	Server server;
	int udpPort;
	int tcpPort;
	LoginRequest loginRequest = new LoginRequest();

	HashMap<Integer, UserData> connections = new HashMap<Integer, UserData>();
	ServerStateResponse serverState = new ServerStateResponse();

	public ManagementServer(int tcpPort, int udpPort) {
		this.udpPort = udpPort;
		this.tcpPort = tcpPort;
	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		System.out.println("HOST MANAGER : Connection - " + connection);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		System.out.println("HOST MANAGER : Disconnect - " + connection);
		logout(connection);
	}

	public void sendServerStateTCP(Connection con) {
		synchronized (serverState) {
			con.sendTCP(serverState);
		}
	}

	public void sendServerStateUDP(Connection con) {
		synchronized (serverState) {
			con.sendUDP(serverState);
		}
	}

	public boolean isLoggedIn(Connection connection) {
		boolean result = false;
		synchronized (serverState) {
			result = connections.containsKey(connection.getID());
		}
		return result;
	}

	public void logout(Connection con) {
		System.out.println("HOST MANAGER : Logout");
		synchronized (serverState) {
			UserData data = connections.remove(con.getID());
			serverState.connections.remove(data);
		}
	}

	public void login(Connection con, UserData data) {
		System.out.println("HOST MANAGER : Login");
		synchronized (serverState) {
			connections.put(con.getID(), data);
			serverState.connections.add(data);
		}
	}

	public void updateConnectionState(Connection con) {
		synchronized (serverState) {
			UserData data = connections.get(con.getID());
			data.serverTCPAddress = con.getRemoteAddressTCP().toString();
			data.serverUDPAddress = con.getRemoteAddressUDP().toString();
		}
	}

	public void received(Connection connection, Object object) {
		System.out.println("HOST MANAGER:Update entity["+connection.getID()+"]"+object);
		if (object instanceof UserData) {
			login(connection, (UserData) object);
			sendServerStateTCP(connection);
		}
		if (isLoggedIn(connection)) {
			updateConnectionState(connection);
			if (object instanceof ServerStateRequest) {
				sendServerStateTCP(connection);
			}else if(object instanceof EntityUpdate){
				updateEntity(connection, (EntityUpdate)object);
			}
		} else {
			connection.sendTCP(loginRequest);
		}
	}

	private void updateEntity(Connection con, EntityUpdate update) {
		System.out.printf("HOST MANAGER:Updating Entity\n");
		synchronized (serverState) {
			UserData data = connections.get(con.getID());
			if(data != null){
				data.pos.set(update.pos);
				data.vel.set(update.vel);
			}
		}
	}

	public void pingAllClients() {
		Connection[] conns = server.getConnections();
		for (int i = 0; i < conns.length; i++) {
			synchronized (serverState) {
				UserData data = connections.get(conns[i].getID());
				if (data != null) {
					data.serverPing = conns[i].getReturnTripTime();
				}
			}
			conns[i].updateReturnTripTime();
		}
	}

	public void broadcastServerState() {
		Connection[] conns = server.getConnections();
		for (int i = 0; i < conns.length; i++) {
			sendServerStateUDP(conns[i]);
		}
	}

	public void startServer() throws IOException {
		server = new Server();
		server.start();
		server.bind(tcpPort, udpPort);
		Network.registerClasses(server);
		server.addListener(this);
	}

	public static void main(String input[]) throws IOException, SecurityException, NoSuchMethodException {
		int broadcasePerSecond = 20;
		ManagementServer server = new ManagementServer(8080, 8081);
		server.startServer();

		SchedultedMethodCallerTask ping = new SchedultedMethodCallerTask("Ping Task", server, "pingAllClients", null);
		ping.setDelay(10000);
		ping.start();

		SchedultedMethodCallerTask state = new SchedultedMethodCallerTask("Server State", server, "broadcastServerState", null);
		state.setDelay((int)(1000*1f/broadcasePerSecond));
		state.start();
	}
}
