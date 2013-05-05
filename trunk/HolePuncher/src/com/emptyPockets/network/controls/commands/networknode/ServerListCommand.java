package com.emptyPockets.network.controls.commands.networknode;

import com.emptyPockets.network.server.NetworkNode;

public class ServerListCommand extends NetworkNodeCommand {

	public ServerListCommand(NetworkNode node) {
		super("list", node);
	}

	@Override
	public void proceeArg(String data) {
		node.logClients();
	}

}
