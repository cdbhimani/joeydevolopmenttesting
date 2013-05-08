package com.emptypockets.networking.controls.commands.client;

import com.emptypockets.networking.client.ClientManager;

public class ClientLoginCommand extends ClientCommand {

	public ClientLoginCommand(ClientManager client) {
		super("login", client);
	}

	@Override
	public void proceeArg(String data) {
		client.setUsername(data);
		client.serverLogin();
	}

}
