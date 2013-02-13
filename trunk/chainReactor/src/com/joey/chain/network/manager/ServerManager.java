package com.joey.chain.network.manager;

import com.esotericsoftware.kryonet.Server;
import com.joey.chain.network.utils.NetworkUtils;

public class ServerManager {
	NetworkUtils utils = NetworkUtils.getNetworkUtils();
	Server server;
	public ServerManager(){
		server = new Server();
		utils.registerClasses(server);
	}
	
	public void start(){
		
	}
	
	public void stop(){
		
	}
}
