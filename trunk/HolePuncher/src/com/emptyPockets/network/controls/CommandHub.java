package com.emptyPockets.network.controls;

import java.util.HashMap;

import com.emptyPockets.network.controls.commands.Command;
import com.esotericsoftware.minlog.Log;


public class CommandHub{
	HashMap<String, Command> commands;
	
	public CommandHub(){
		commands = new HashMap<String, Command>();
	}
	
	public void addCommand(Command command){
		synchronized (commands) {
			commands.put(command.getName(), command);
		}
	}
	
	public void processCommand(String data){
		String cmd[]= data.split(" ",2);
		
		String commandName = cmd[0].substring(1);
		String args = null;
		if(cmd.length > 1){
			args = cmd[1];
		}
		Log.debug("Command Called : "+commandName);
		try{
			synchronized (commands) {
				Command command = commands.get(commandName);
				if(command != null){
					command.proceeArg(args);
				}
			}
		}catch(Throwable e){
			Log.error("Error processing command", e);
		}
		
	}
}
