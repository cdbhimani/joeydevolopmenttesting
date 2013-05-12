package com.emptyPockets.commandline.commands;

import com.emptyPockets.commandline.Command;
import com.emptyPockets.commandline.CommandLine;

public abstract class CommandLineCommand extends Command {

	CommandLine commandHub;
	
	public CommandLineCommand(String name, CommandLine commandHub) {
		super(name);
		this.commandHub = commandHub;
	}

}
