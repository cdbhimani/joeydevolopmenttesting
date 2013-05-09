package com.emptypockets.networking.controls;

import java.util.HashMap;

import com.emptyPockets.logging.Console;
import com.emptypockets.networking.controls.commands.Command;
import com.emptypockets.networking.log.ServerLogger;
import com.esotericsoftware.kryonet.Listener;

public class CommandHub extends Listener {
	CommandHubPanel panel;
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
		boolean commandFound = false;

		if (data != null) {
			String cmd[] = data.split(" ", 2);
			String commandName = cmd[0];
			String argument = null;
			if (cmd.length > 1) {
				argument = cmd[1];
			}
			try {
				Command command = getCommand(commandName);
				if (command != null) {
					commandFound = true;
					command.proceeArg(argument);
				}
			} catch (Throwable e) {
				ServerLogger.error("Error processing command", e);
			}
		}
		if (commandFound == false) {
			Console.println("Unknown Command : " + data);
		}
	}

	public Command getCommand(String name) {
		synchronized (commands) {
			return commands.get(name);
		}
	}

	public CommandHubPanel getPanel() {
		if (panel == null) {
			panel = new CommandHubPanel(this);
		}
		return panel;
	}
}
