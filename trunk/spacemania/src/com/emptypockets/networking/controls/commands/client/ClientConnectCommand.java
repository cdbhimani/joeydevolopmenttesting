package com.emptypockets.networking.controls.commands.client;

import java.io.IOException;

import com.emptypockets.networking.client.ClientManager;
import com.emptypockets.networking.log.ServerLogger;

public class ClientConnectCommand extends ClientCommand {

	public ClientConnectCommand(ClientManager client) {
		super("connect", client);
		setDescription("Connects to a remote server : connect [tcpPort,udpPort]");
	}

	@Override
	public void exec(String data) {
		try {
			if (data != null) {
				String arg[] = data.split(",");
				String address = arg[0];
				int tcpPort = Integer.parseInt(arg[1]);
				int udpPort = Integer.parseInt(arg[2]);
				try {
					client.connect(address, tcpPort, udpPort);
				} catch (IOException e) {
					ServerLogger.error("Failed to start server", e);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			ServerLogger.error("Invalid Arguments", e);
		}
	}


}
