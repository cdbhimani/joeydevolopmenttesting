package com.emptyPockets.network.controls.commands.networknode;

import java.io.IOException;

import com.emptyPockets.network.server.NetworkNode;
import com.esotericsoftware.minlog.Log;

public class ServerStartCommand extends NetworkNodeCommand {

	public ServerStartCommand(NetworkNode nodes) {
		super("start", nodes);
	}

	@Override
	public void proceeArg(String data) {
		try {
			if (data != null) {
				int port = Integer.parseInt(data);
				node.setLocalPort(port);
			}
		} catch (Exception e) {
			Log.error("Invalid Port Number", e);
		}

		try {
			node.start();
		} catch (IOException e) {
			Log.error("Failed to start server", e);
			e.printStackTrace();
		}
	}

}
