package com.emptypockets.networking.controls.commands.networknode;

import com.emptypockets.network.server.NetworkNode;

public class ServerListCommand extends NetworkNodeCommand {

	public ServerListCommand(NetworkNode node) {
		super("list", node);
	}

	@Override
	public void proceeArg(String data) {
		node.logClients();
	}

}
