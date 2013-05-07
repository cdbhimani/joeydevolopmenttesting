package com.emptypockets.networking.controls.commands.server;

import com.emptypockets.networking.server.ServerManager;

public class ServerStatusCommand extends ServerCommand {

	public ServerStatusCommand(ServerManager nodes) {
		super("status", nodes);
	}

	@Override
	public void proceeArg(String data) {
		server.logStatus();
	}

}
