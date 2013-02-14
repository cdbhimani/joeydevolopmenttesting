package com.joey.chain.network.serverSide;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.joey.chain.network.transport.ClientData;
import com.joey.chain.network.transport.ClientLoginMessage;
import com.joey.chain.network.transport.ServerStatus;
import com.joey.chain.network.utils.NetworkUtils;

public class ServerManager extends Listener{
	NetworkUtils utils = NetworkUtils.getNetworkUtils();
	Server server;
	ServerStatus status = new ServerStatus();
	public ServerManager(){
		server = new Server(){
			@Override
			protected Connection newConnection() {
				return new ClientConnection();
			}
		};
		utils.registerClasses(server);
	}
	
	public void start(){
		
	}
	
	public void stop(){
		
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		updateServerStatus();
	}
	
	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		updateServerStatus();
	}
	
	@Override
	public void idle(Connection connection) {
		super.idle(connection);
	}
	
	@Override
	public void received(Connection connection, Object object) {
		if(object instanceof ClientLoginMessage){
			ClientLoginMessage msg = (ClientLoginMessage)object;
			((ClientConnection)connection).setName(msg.userName);
			((ClientConnection)connection).setLoggedIn(true);
			updateServerStatus();
		}		
		
	}
	
	public void updateServerStatus(){
//		Connection[] connections = server.getConnections();
//		ArrayList<ClientData> names = new ArrayList<ClientData>();
//		for(Connection con : connections){
//			ClientConnection c = (ClientConnection)con;
//			ClientData data = new ClientData();
//			if(c.isLoggedIn()){
//				data.add(c.name);
//			}else{
//				names.add("Unknown");
//			}
//			na
//		}
//		
//		status.clientNames= names.toArray(new String[names.size()]);
	}
	
	public void sendServerStatus(){
		server.sendToAllTCP(status);
	}
}
