package com.emptyPockets.network.framework;

import java.io.IOException;

import com.emptyPockets.network.framework.messages.ServerFullMessage;


public abstract class GameClient {

	protected int connectTimeout = 5000;
	protected int tcpPort;
	protected int udpPort;
	protected String host;
	
	boolean connecting = false;
	
	public GameClient() {
		super();
	}
	
	public abstract void sendUDP(Object object);
	public abstract void sendTCP(Object object);

	protected abstract void startConnection() throws IOException;
	protected abstract void stopConnection();
	
	public void connect(){
		
	}
	
	public void disconnect(){
		
	}
	public void setHost(String host) {
		this.host = host;
	}

	public void setPorts(int tcpPort, int udpPort) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
	}

	public void recieved(Object object){
		if(object instanceof ServerFullMessage){
			stopConnection();
		}
	}

}