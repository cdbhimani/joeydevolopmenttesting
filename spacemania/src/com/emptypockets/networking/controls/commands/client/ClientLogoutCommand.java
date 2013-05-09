package com.emptypockets.networking.controls.commands.client;

import com.emptypockets.networking.client.ClientManager;

public class ClientLogoutCommand extends ClientCommand {

	public ClientLogoutCommand(ClientManager client) {
		super("logout", client);
	}

	@Override
	public void proceeArg(String data) {
		client.serverLogout();
	}

}
