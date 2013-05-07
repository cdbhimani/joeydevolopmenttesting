package com.emptypockets.networking.controls;

import com.emptypockets.networking.controls.commands.server.ServerStartCommand;
import com.emptypockets.networking.controls.commands.server.ServerStatusCommand;
import com.emptypockets.networking.controls.commands.server.ServerStopCommand;
import com.emptypockets.networking.server.ServerManager;

public class CommandService {

	public static void registerServer(ServerManager server) {
		server.getCommand().addCommand(new ServerStartCommand(server));
		server.getCommand().addCommand(new ServerStopCommand(server));
		server.getCommand().addCommand(new ServerStatusCommand(server));
	}

	public static void registerClient(CommandHub hub) {

	}
}
