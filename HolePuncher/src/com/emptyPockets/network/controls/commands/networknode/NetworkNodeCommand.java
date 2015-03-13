package com.emptyPockets.network.controls.commands.networknode;

import com.emptyPockets.network.controls.commands.Command;
import com.emptyPockets.network.server.NetworkNode;

public abstract class NetworkNodeCommand extends Command {

	NetworkNode node;

	public NetworkNodeCommand(String name,NetworkNode nodes) {
		super(name);
		this.node = nodes;
	}

}
