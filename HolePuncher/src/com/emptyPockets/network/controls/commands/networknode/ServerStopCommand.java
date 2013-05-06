package com.emptypockets.network.controls.commands.networknode;

import com.emptypockets.network.server.NetworkNode;

public class ServerStopCommand extends NetworkNodeCommand {

	public ServerStopCommand(NetworkNode nodes) {
		super("stop", nodes);
	}

	@Override
	public void proceeArg(String data) {
		node.stop();
	}

}
