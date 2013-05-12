package com.emptypockets.networking.controls.commands.client;

import com.emptypockets.networking.client.ClientManager;

public class ClientLogoutCommand extends ClientCommand {

	public ClientLogoutCommand(ClientManager client) {
		super("logout", client);
		setDescription("Logs a user out of a server : logout");
	}

	@Override
	public void exec(String data) {
		client.serverLogout();
	}
}
