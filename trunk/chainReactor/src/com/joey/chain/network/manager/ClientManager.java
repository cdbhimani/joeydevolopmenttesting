package com.joey.chain.network.manager;

import com.esotericsoftware.kryonet.Client;
import com.joey.chain.network.utils.NetworkUtils;

public class ClientManager {
	NetworkUtils utils = NetworkUtils.getNetworkUtils();
	Client client;
	
	public ClientManager(){
		client = new Client();
		utils.registerClasses(client);
	}
	
	public void start(String serverAddress){
		client.connect(timeout, host, tcpPort, udpPort)
	}
}
