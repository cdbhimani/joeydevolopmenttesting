package com.emptypockets.networking.controls.commands.client;

import com.emptypockets.networking.client.ClientManager;
import com.emptypockets.networking.controls.commands.Command;

public abstract class ClientCommand extends Command {

	ClientManager client;
	
	public ClientCommand(String name, ClientManager client) {
		super(name);
		this.client = client;
	}

}
