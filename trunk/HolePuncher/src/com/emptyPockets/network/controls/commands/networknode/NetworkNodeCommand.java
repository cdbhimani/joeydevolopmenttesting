package com.emptypockets.network.controls.commands.networknode;

import com.emptypockets.network.controls.commands.Command;
import com.emptypockets.network.server.NetworkNode;

public abstract class NetworkNodeCommand extends Command {

	NetworkNode node;

	public NetworkNodeCommand(String name, NetworkNode nodes) {
		super(name);
		this.node = nodes;
	}

}
