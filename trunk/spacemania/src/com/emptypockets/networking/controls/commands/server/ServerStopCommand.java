package com.emptypockets.networking.controls.commands.server;

import com.emptypockets.networking.server.ServerManager;

public class ServerStopCommand extends ServerCommand {

	public ServerStopCommand(ServerManager server) {
		super("stop", server);
	}

	@Override
	public void proceeArg(String data) {
		server.stop();
	}

}
