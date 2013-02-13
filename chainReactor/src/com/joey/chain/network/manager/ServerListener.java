package com.joey.chain.network.manager;

import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener {
	ServerManager owner;
	
	public ServerListener(ServerManager owner){
		this.owner = owner;
	}
}
