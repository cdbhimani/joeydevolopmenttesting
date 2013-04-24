package com.emptyPockets.test.nat;

import java.io.IOException;

import com.emptyPockets.logging.ConsoleScreen;
import com.emptyPockets.test.nat.transport.Network;
import com.emptyPockets.test.nat.transport.stun.STUNRequest;
import com.emptyPockets.test.nat.transport.stun.STUNResponse;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientConnection {
	Client serverManager;
	Client server;
	
	ClientListener listener;

	ConsoleScreen console;
	
	public ClientConnection() {
		listener = new ClientListener(this);
		
		server = new Client();
		server.start();
		server.addListener(listener);
		Network.registerClasses(server);
		
		serverManager = new Client();
		serverManager.start();
		serverManager.addListener(listener);
		Network.registerClasses(serverManager);
	}

	public void connectServerManager(String serverAddress, int tcpPort, int udpPort) throws IOException {
		serverManager.connect(5000, serverAddress, tcpPort, udpPort);
	}
	
	public void connectServer(String serverAddress, int tcpPort, int udpPort) throws IOException {
		server.connect(5000, serverAddress, tcpPort, udpPort);
	}

	public void sendServerManagerSTUN(){
		if(console != null){
			console.println("CLIENT: Sending Stun Message");	
		}
		STUNRequest msg = new STUNRequest();
		serverManager.sendTCP(msg);
	}
	
	public void requestServerListFromServerManager(){
	}
	
	public void disconnectServer() {
		server.close();
	}
	
	public void disconnectServerManager(){
		serverManager.close();
	}

	protected void processSTUNResponse(STUNResponse object) {
		System.out.println("CLIENT : "+object);
		if(console != null){
			console.println("CLIENT : "+object);	
		}
	}

	public void dispose() {
		server.stop();
		serverManager.stop();
		server = null;
		serverManager = null;
		listener = null;
	}

	public ConsoleScreen getConsole() {
		return console;
	}

	public void setConsole(ConsoleScreen console) {
		this.console = console;
	}
}

class ClientListener extends Listener {
	ClientConnection owner;

	public ClientListener(ClientConnection clientConnection) {
		this.owner = clientConnection;
	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
	}
	
	@Override
	public void idle(Connection connection) {
		super.idle(connection);
	}
	
	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
	}
	
	@Override
	public void received(Connection connection, Object object) {
		System.out.printf("CLIENT:Recieved Object from [%s] : %s\n",connection.toString(),object.toString());
		if(object instanceof STUNResponse){
			owner.processSTUNResponse((STUNResponse)object);
		}
	}
	
	
}
