package com.emptypockets.networking.controls;

import com.emptypockets.networking.client.ClientManager;
import com.emptypockets.networking.controls.commands.client.ClientConnectCommand;
import com.emptypockets.networking.controls.commands.client.ClientLoginCommand;
import com.emptypockets.networking.controls.commands.client.ClientServerCommand;
import com.emptypockets.networking.controls.commands.commandHub.CommandHubHelpCommand;
import com.emptypockets.networking.controls.commands.server.ServerStartCommand;
import com.emptypockets.networking.controls.commands.server.ServerStatusCommand;
import com.emptypockets.networking.controls.commands.server.ServerStopCommand;
import com.emptypockets.networking.server.ServerManager;
import com.emptypockets.networking.transfer.ClientLoginRequest;

public class CommandService {

	public static void registerCommandHub(CommandHub hub){
		hub.addCommand(new CommandHubHelpCommand(hub));
	}
	public static void registerServer(ServerManager server) {
		server.getCommand().addCommand(new ServerStartCommand(server));
		server.getCommand().addCommand(new ServerStopCommand(server));
		server.getCommand().addCommand(new ServerStatusCommand(server));
		registerCommandHub(server.getCommand());
	}

	public static void registerClient(ClientManager client) {
		client.getCommand().addCommand(new ClientConnectCommand(client));
		client.getCommand().addCommand(new ClientLoginCommand(client));
		client.getCommand().addCommand(new ClientServerCommand(client));
		registerCommandHub(client.getCommand());
		
		client.getCommand().getPanel().pushHistory("connect 54.217.240.178,8080,8081");
		client.getCommand().getPanel().pushHistory("connect localhost,8080,8081");
	}
}

