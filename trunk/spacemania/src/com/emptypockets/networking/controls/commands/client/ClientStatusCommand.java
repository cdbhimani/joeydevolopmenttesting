package com.emptypockets.networking.controls.commands.client;

import com.emptypockets.networking.client.ClientManager;

public class ClientStatusCommand extends ClientCommand {

	public ClientStatusCommand(ClientManager client) {
		super("status", client);
		setDescription("Echos the status of the client : status");
	}

	@Override
	public void exec(String data) {
		client.listStatus();
	}
}
