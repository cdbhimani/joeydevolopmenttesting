package com.emptypockets.networking.controls.commands.commandHub;

import com.emptypockets.networking.controls.CommandHub;
import com.emptypockets.networking.controls.commands.Command;

public abstract class CommandHubCommand extends Command {

	CommandHub commandHub;
	
	public CommandHubCommand(String name, CommandHub commandHub) {
		super(name);
		this.commandHub = commandHub;
	
	}

}
