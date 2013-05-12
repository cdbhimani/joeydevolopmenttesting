package com.emptypockets.networking.controls;

import com.emptypockets.networking.client.ClientManager;
import com.emptypockets.networking.controls.commands.client.ClientConnectCommand;
import com.emptypockets.networking.controls.commands.client.ClientLoginCommand;
import com.emptypockets.networking.controls.commands.client.ClientLogoutCommand;
import com.emptypockets.networking.controls.commands.client.ClientSearchCommand;
import com.emptypockets.networking.controls.commands.client.ClientServerCommand;
import com.emptypockets.networking.controls.commands.server.ServerStartCommand;
import com.emptypockets.networking.controls.commands.server.ServerStatusCommand;
import com.emptypockets.networking.controls.commands.server.ServerStopCommand;
import com.emptypockets.networking.server.ServerManager;

public class CommandService {

	public static void registerServer(ServerManager server) {
		server.getCommand().registerCommand(new ServerStartCommand(server));
		server.getCommand().registerCommand(new ServerStopCommand(server));
		server.getCommand().registerCommand(new ServerStatusCommand(server));
	}

	public static void registerClient(ClientManager client) {
		client.getCommand().registerCommand(new ClientConnectCommand(client));
		client.getCommand().registerCommand(new ClientLoginCommand(client));
		client.getCommand().registerCommand(new ClientLogoutCommand(client));
		client.getCommand().registerCommand(new ClientSearchCommand(client));
		client.getCommand().registerCommand(new ClientServerCommand(client));
		
		client.getCommand().getPanel().pushHistory("connect 54.217.240.178,8080,8081");
		client.getCommand().getPanel().pushHistory("connect localhost,8080,8081");
	}
}

