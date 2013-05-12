package com.emptypockets.networking.controls.commands.server;

import com.emptypockets.networking.server.ServerManager;

public class ServerStopCommand extends ServerCommand {

	public ServerStopCommand(ServerManager server) {
		super("stop", server);
		setDescription("Stops a currently running server : stop");
	}

	@Override
	public void exec(String data) {
		server.stop();
	}
}
