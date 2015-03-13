package com.emptyPockets.network.server;

import java.net.InetAddress;

/**
 * This represents a client seen on a server
 * @author jenfield
 *
 */
public class NetworkConnection {
	static int clientCount = 0;
	
	int clientId;
	String nodeName;
	
	InetAddress clientAddress;
	int clientPort;
	Pinger pinger;
	boolean pingRequired = true;
	
	public NetworkConnection(String nodeName, InetAddress address, int port){
		this.clientId = clientCount++;
		this.clientAddress = address;
		this.clientPort = port;
		this.nodeName = nodeName;
		setPinger(new Pinger(this));
	}
	
	@Override
	public String toString() {
		StringBuilder rst = new StringBuilder();
		rst.append("{NetworkConnection :");
		rst.append(" nodeName=[");
		rst.append(nodeName);
		rst.append("], clientId=[");
		rst.append(clientId);
		rst.append("], clientAddress=[");
		rst.append(clientAddress);
		rst.append("], clientPort=[");
		rst.append(clientPort);
		rst.append("], ping=[");
		rst.append(getPing());
		rst.append("]}");
		return rst.toString();
	}
	
	public void notifyClientTimeout(NetworkNode server) {
		server.nodeTimeoutRecieved(this);
	}
	
	public void update(NetworkNode server){
		if(pingRequired){
			getPinger().update(server);
		}
	}

	public int getClientId() {
		return clientId;
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

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public boolean isPingRequired() {
		return pingRequired;
	}

	public void setPingRequired(boolean pingRequired) {
		this.pingRequired = pingRequired;
	}

	public InetAddress getClientAddress() {
		return clientAddress;
	}

	public int getClientPort() {
		return clientPort;
	}
	
}
