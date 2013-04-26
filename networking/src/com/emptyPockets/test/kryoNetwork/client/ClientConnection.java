package com.emptyPockets.test.kryoNetwork.client;

import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.logging.ConsoleScreen;
import com.emptyPockets.test.kryoNetwork.server.ServerState;
import com.emptyPockets.test.kryoNetwork.transport.Network;
import com.emptyPockets.test.kryoNetwork.transport.messages.EntityUpdate;
import com.emptyPockets.test.kryoNetwork.transport.messages.LoginRequest;
import com.emptyPockets.test.kryoNetwork.transport.messages.ServerStateRequest;
import com.emptyPockets.test.kryoNetwork.transport.messages.ServerStateResponse;
import com.emptyPockets.test.kryoNetwork.transport.messages.UserData;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientConnection extends Listener{
	Client server;
	UserData loginData;
	ConsoleScreen console;
	String name;
	ServerStateRequest serverStateRequest = new ServerStateRequest();
	
	EntityUpdate entityUpdate = new EntityUpdate();
	ServerState serverState;
	
	public ClientConnection() {
		server = new Client();
		server.addListener(this);
		Network.registerClasses(server);
		server.start();
		serverState = new ServerState();
	}

	public void connectServer(String serverAddress, int tcpPort, int udpPort) throws IOException {
		if(console != null){
			console.printf("CLIENT: Connecting [%s] TCP[%d] UDP[%d]\n", serverAddress, tcpPort, udpPort);	
		}
		server.connect(5000, serverAddress, tcpPort, udpPort);
	}

	public void requestServerStatus(){
		if(console != null){
			console.println("CLIENT: Requesting Server State");	
		}
		server.sendTCP(serverStateRequest);
	}
	
	public void loginToServer(){
		if(console != null){
			console.println("CLIENT: Sending Login Message");	
		}
		loginData = new UserData();
		loginData.name = name;
		loginData.pos = new Vector2();
		loginData.vel = new Vector2();
		server.sendTCP(loginData);
	}

	public void setPosition(float x, float y){
		entityUpdate.pos.x = x;
		entityUpdate.pos.y = y;
		sendUpdate(entityUpdate);
	}
	
	private void sendUpdate(EntityUpdate update){
		server.sendTCP(update);
	}
	
	private void processServerState(ServerStateResponse object) {
		if(console != null){
			console.println("CLIENT: Recieved Server State");	
		}
		if(serverState != null){
			serverState.setServerResponse(object);
		}
	}
	
	public void disconnectServer() {
		server.close();
	}

	public void dispose() {
		server.stop();
		server = null;
	}

	public ConsoleScreen getConsole() {
		return console;
	}

	public void setConsole(ConsoleScreen console) {
		this.console = console;
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
	public void received(Connection con, Object object) {
		System.out.printf("CLIENT:Recieved Object from [%s] : %s\n",con.toString(),object.toString());
		if(object instanceof LoginRequest){
			loginToServer();
		}else if(object instanceof ServerStateResponse){
			processServerState((ServerStateResponse)object);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setServerState(ServerState serverState) {
		this.serverState = serverState;
	}
}
