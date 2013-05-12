package com.emptypockets.networking.controls.commands.client;

import com.emptyPockets.commandline.Command;
import com.emptypockets.networking.client.ClientManager;

public abstract class ClientCommand extends Command {

	ClientManager client;
	
	public ClientCommand(String name, ClientManager client) {
		super(name);
		this.client = client;
	}

}
