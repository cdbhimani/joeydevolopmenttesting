package com.joey.chain.network.serverSide;

import com.esotericsoftware.kryonet.Connection;
import com.joey.chain.network.transport.ClientData;

public class ClientConnection extends Connection {
	boolean loggedIn = false;
	ClientData clientData = new ClientData();

	public String getName() {
		return clientData.name;
	}

	public void setName(String name) {
		this.clientData.name = name;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public void updateClientData(){
		updateReturnTripTime();

	}
	
}
