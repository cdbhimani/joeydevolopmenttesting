package com.emptypockets.networking.controls.commands.client;

import com.emptypockets.networking.client.ClientManager;

public class ClientServerCommand extends ClientCommand {

	public ClientServerCommand(ClientManager client) {
		super("server", client);
	}

	@Override
	public void proceeArg(String data) {
		if(data != null && data.startsWith("setup")){
			String arg[] = data.split(" ");
			int count = Integer.parseInt(arg[1]);
			client.setupServer(count);
		}else{
			client.getServerManager().getCommand().processCommand(data);
		}
	}

	@Override
	public String getCommandHelp() {
		return "This issues commands to the clients local server (see server help) : server [arg] ";
	}

}
