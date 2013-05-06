package com.emptypockets.networking.controls.commands.networknode;

import java.io.IOException;

import com.emptypockets.network.log.ServerLogger;
import com.emptypockets.network.server.NetworkNode;

public class ServerStartCommand extends NetworkNodeCommand {

	public ServerStartCommand(NetworkNode nodes) {
		super("start", nodes);
	}

	@Override
	public void proceeArg(String data) {
		try {
			if (data != null) {
				String args[] = data.split(",");
				
				if(args.length > 1){
					int port = Integer.parseInt(args[0]);
					String name = args[1];
					node.setLocalPort(port);
					node.setNodeName(name);
				}else{
					int port = Integer.parseInt(args[0]);
					node.setLocalPort(port);
				}
				
			}
		} catch (Exception e) {
			ServerLogger.error("Invalid Arguments", e);
		}

		try {
			node.start();
		} catch (IOException e) {
			ServerLogger.error("Failed to start server", e);
			e.printStackTrace();
		}
	}

}
