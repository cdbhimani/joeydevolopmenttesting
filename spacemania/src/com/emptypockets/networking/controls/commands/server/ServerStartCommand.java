package com.emptypockets.networking.controls.commands.server;

import java.io.IOException;

import com.emptypockets.networking.log.ServerLogger;
import com.emptypockets.networking.server.ServerManager;

public class ServerStartCommand extends ServerCommand {

	public ServerStartCommand(ServerManager server) {
		super("start", server);
	}

	@Override
	public void exec(String data) {
		try {
			if (data != null) {
				String args[] = data.split(",");
				
				if(args.length == 3){
					int tcpPort = Integer.parseInt(args[0]);
					int udpPort = Integer.parseInt(args[1]);
					String name = args[3];
					server.setTcpPort(tcpPort);
					server.setUdpPort(udpPort);
					server.setName(name);
				}if(args.length ==2){
					int tcpPort = Integer.parseInt(args[0]);
					int udpPort = Integer.parseInt(args[1]);
					server.setTcpPort(tcpPort);
					server.setUdpPort(udpPort);
				}
				
			}
		} catch (Exception e) {
			ServerLogger.error("Invalid Arguments", e);
		}

		try {
			server.start();
		} catch (IOException e) {
			ServerLogger.error("Failed to start server", e);
			e.printStackTrace();
		}
	}
}
