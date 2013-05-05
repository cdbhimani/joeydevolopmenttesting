package com.emptyPockets.network.controls.commands.networknode;

import com.emptyPockets.network.server.NetworkNode;
import com.esotericsoftware.minlog.Log;

public class ServerStatusCommand extends NetworkNodeCommand {

	public ServerStatusCommand(NetworkNode nodes) {
		super("status", nodes);
	}

	@Override
	public void proceeArg(String data) {
		Log.info(node.getNodeName(), "server["+node.isServerRunning()+"] Connected["+node.getConnectedCount()+"]");
	}

}
