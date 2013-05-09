package com.emptypockets.networking.controls.commands.commandHub;

import com.emptyPockets.logging.Console;
import com.emptypockets.networking.controls.CommandHub;
import com.emptypockets.networking.controls.commands.Command;

public class CommandHubHelpCommand extends CommandHubCommand{

	public CommandHubHelpCommand(CommandHub commandHub) {
		super("help", commandHub);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCommandHelp() {
		return "Displays help for the system";
	}

	@Override
	public void proceeArg(String data) {
		if(data == null || data.length() == 0){
			
		}else{
			Command command = commandHub.getCommand(data);
			if(command == null){
				Console.println("Unknown Command : "+data);
			}else{
				Console.println(command.getCommandHelp());				
			}

		}
		
	}

}
