package com.emptyPockets.test.nat;

import java.io.IOException;
import java.util.HashMap;

import com.emptyPockets.test.nat.transport.Network;
import com.emptyPockets.test.nat.transport.messages.LoginData;
import com.emptyPockets.test.nat.transport.messages.LoginRequest;
import com.emptyPockets.test.nat.transport.messages.ServerStateRequest;
import com.emptyPockets.test.nat.transport.messages.ServerStateResponse;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ManagementServer extends Listener {
	Server server;
	int udpPort;
	int tcpPort;
	LoginRequest loginRequest = new LoginRequest();

	HashMap<Integer, LoginData> connections = new HashMap<Integer, LoginData>();
	ServerStateResponse serverState = new ServerStateResponse();

	public ManagementServer(int tcpPort, int udpPort) {
		this.udpPort = udpPort;
		this.tcpPort = tcpPort;
	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		connection.sendTCP(loginRequest);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		logout(connection);
	}

	public void sendServerState(Connection con) {
		synchronized (serverState) {
			con.sendTCP(serverState);
		}
	}

	public boolean isLoggedIn(Connection connection) {
		boolean result = false;
		synchronized (serverState) {
			result = connections.containsKey(connection.getID());
		}
		return result;
	}

	public void logout(Connection con){
		System.out.println("HOST MANAGER : Logout");
		synchronized (serverState) {
			LoginData data = connections.remove(con.getID());
			serverState.connections.remove(data);
		}
	}
	public void login(Connection con, LoginData data) {
		System.out.println("HOST MANAGER : Login");
		synchronized (serverState) {
			connections.put(con.getID(),data);
			serverState.connections.add(data);
		}
	}
	
	public void updateConnectionState(Connection con){
		System.out.println("HOST MANAGER : Update Connection");
		synchronized (serverState) {
			LoginData data = connections.get(con.getID());
			data.serverTCPAddress = con.getRemoteAddressTCP().toString();
			data.serverUDPAddress = con.getRemoteAddressUDP().toString();
			data.serverPing = con.getReturnTripTime();
		}
	}

	public void received(Connection connection, Object object) {
		System.out.printf("HOST MANAGER:Recieved Object from [%s] : %s\n", connection.toString(), object.toString());
		if (object instanceof LoginData) {
			login(connection, (LoginData) object);
			sendServerState(connection);
		}
		if (isLoggedIn(connection)) {
			updateConnectionState(connection);
			if (object instanceof ServerStateRequest) {
				sendServerState(connection);
			}
		} else {
			connection.sendTCP(loginRequest);
		}
	}

	public void startServer() throws IOException {
		server = new Server();
		server.start();
		server.bind(tcpPort, udpPort);
		Network.registerClasses(server);
		server.addListener(this);
	}

	public static void main(String input[]) throws IOException {
		ManagementServer server = new ManagementServer(8080, 8081);
		server.startServer();
	}
}
