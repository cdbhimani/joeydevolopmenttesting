package com.emptypockets.networking.controls.commands.server;

import com.emptyPockets.commandline.Command;
import com.emptypockets.networking.server.ServerManager;

public abstract class ServerCommand extends Command {

	ServerManager server;
	
	public ServerCommand(String name, ServerManager server) {
		super(name);
		this.server = server;
	}

}
