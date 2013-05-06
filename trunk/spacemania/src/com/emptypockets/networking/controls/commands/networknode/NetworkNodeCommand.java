package com.emptypockets.networking.controls.commands.networknode;

import com.emptypockets.network.server.NetworkNode;
import com.emptypockets.networking.controls.commands.Command;

public abstract class NetworkNodeCommand extends Command {

	NetworkNode node;

	public NetworkNodeCommand(String name, NetworkNode nodes) {
		super(name);
		this.node = nodes;
	}

}
