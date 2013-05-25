package com.emptypockets.network.controls.commands.networknode;

import com.emptypockets.network.server.NetworkNode;

public class ServerStatusCommand extends NetworkNodeCommand {

	public ServerStatusCommand(NetworkNode nodes) {
		super("status", nodes);
	}

	@Override
	public void proceeArg(String data) {
		node.logStatus();
	}

}
