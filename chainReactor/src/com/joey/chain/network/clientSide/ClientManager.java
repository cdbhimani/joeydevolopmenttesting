package com.joey.chain.network.clientSide;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.joey.chain.network.transport.ClientLoginMessage;
import com.joey.chain.network.transport.ServerStatus;
import com.joey.chain.network.utils.NetworkUtils;

public class ClientManager extends Listener{
	NetworkUtils utils = NetworkUtils.getNetworkUtils();
	Client client;
	ServerStatus status = new ServerStatus();
	
	public ClientManager(){
		client = new Client();
		utils.registerClasses(client);
	}
	
	public void start(String hostAddress) throws IOException{
		client.start();
		client.addListener(this);
		client.connect(utils.getConnectionTimeout(), hostAddress, utils.getTcpPort(), utils.getUdpPort());
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		ClientLoginMessage login = new ClientLoginMessage();
		login.userName = "Testing";
		connection.sendTCP(login);
	}
	
	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
	}
	
	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		if(object instanceof ServerStatus){
			updateServerStatus((ServerStatus)object);
		}
	}
	
	public void updateServerStatus(ServerStatus status){
		this.status = status;
	}
}
