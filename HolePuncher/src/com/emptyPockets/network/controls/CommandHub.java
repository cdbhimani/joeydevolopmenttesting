package com.emptypockets.network.controls;

import java.util.HashMap;

import com.emptypockets.network.controls.commands.Command;
import com.emptypockets.network.log.ServerLogger;
import com.emptypockets.network.server.NetworkConnection;
import com.emptypockets.network.server.NetworkNode;
import com.emptypockets.network.server.NetworkNodeListenerAdapter;
import com.emptypockets.network.transport.RemoteMessage;

public class CommandHub extends NetworkNodeListenerAdapter {
	HashMap<String, Command> commands;

	public CommandHub() {
		commands = new HashMap<String, Command>();
	}

	public void addCommand(Command command) {
		synchronized (commands) {
			commands.put(command.getName(), command);
		}
	}

	public void processCommand(String data) {
		ServerLogger.debug("Command Called - processCommand : " + data);
		if (data!=null && data.startsWith("\\")) {
			String cmd[] = data.split(" ", 2);

			String commandName = cmd[0].substring(1);
			String args = null;
			if (cmd.length > 1) {
				args = cmd[1];
			}
			try {
				synchronized (commands) {
					Command command = commands.get(commandName);
					if (command != null) {
						command.proceeArg(args);
					}
				}
			} catch (Throwable e) {
				ServerLogger.error("Error processing command", e);
			}
		}

	}

	@Override
	public void dataRecieved(NetworkNode node, NetworkConnection connection, Object data) {
		super.dataRecieved(node, connection, data);
		if (data instanceof RemoteMessage) {
			processCommand(((RemoteMessage) data).getMessage());
		}
	}
}
