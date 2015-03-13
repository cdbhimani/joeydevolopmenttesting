package com.emptyPockets.network.controls;

import java.util.HashMap;

import com.emptyPockets.network.controls.commands.Command;
import com.emptyPockets.network.log.ServerLogger;
import com.emptyPockets.network.server.NetworkConnection;
import com.emptyPockets.network.server.NetworkNode;
import com.emptyPockets.network.server.NetworkNodeListenerAdapter;
import com.emptyPockets.network.transport.RemoteMessage;

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

	public void displayHelp(String commandName){
		ServerLogger.info("The following commands are supported");
		synchronized (commands) {
			for(Command command : commands.values()){
				ServerLogger.info(command.getName()+"\t : \t"+command.getDescription());
			}
		}
	}
	public void processCommand(String data) {
		ServerLogger.debug("Command Called - processCommand : " + data);
		if (data != null && data.startsWith("\\")) {
			String cmd[] = data.split(" ", 2);

			String commandName = cmd[0].substring(1);
			String args = null;
			if (cmd.length > 1) {
				args = cmd[1];
			}
			if (commandName.toLowerCase().startsWith("help")) {
				displayHelp(commandName);
			} else {
				try {
					synchronized (commands) {
						Command command = commands.get(commandName);
						if (command != null) {
							command.proceeArg(args);
						}else{
							ServerLogger.info("Unknown command, type help for available commands");
						}
					}
				} catch (Throwable e) {
					ServerLogger.error("Error processing command", e);
				}
			}
		}else{
			ServerLogger.info("Unknown command, type \\help for available commands");
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
