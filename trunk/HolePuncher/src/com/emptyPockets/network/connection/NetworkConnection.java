package com.emptyPockets.network.connection;

import java.net.InetAddress;

import com.emptyPockets.network.server.NetworkNode;
import com.emptyPockets.network.transport.FrameworkMessages.ConnectionRequest;
import com.emptyPockets.network.transport.FrameworkMessages.Ping;

/**
 * This represents a client seen on a server
 * @author jenfield
 *
 */
public class NetworkConnection {
	static int clientCount = 0;
	
	int clientId;
	String username;
	
	InetAddress clientAddress;
	int clientPort;
	private Pinger pinger;
	
	public NetworkConnection(ConnectionRequest request, InetAddress address, int port){
		this.clientId = clientCount++;
		this.clientAddress = address;
		this.clientPort = port;
		this.username = request.getUsername();
		setPinger(new Pinger(this));
	}
	
	public void notifyClientTimeout(NetworkNode server) {
		server.clientTimeoutRecieved(this);
	}
	
	public void update(NetworkNode server){
		getPinger().update(server);
	}

	public int getClientId() {
		return clientId;
	}

	public String getUsername() {
		return username;
	}

	public int getPing() {
		return getPinger().getPing();
	}

	public Pinger getPinger() {
		return pinger;
	}

	public void setPinger(Pinger pinger) {
		this.pinger = pinger;
	}


	
}
