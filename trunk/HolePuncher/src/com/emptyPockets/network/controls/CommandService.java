package com.emptyPockets.network.controls;

import com.emptyPockets.network.controls.commands.networknode.ConnectCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerListCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerSendMessageCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerStartCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerStatusCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerStopCommand;
import com.emptyPockets.network.server.NetworkNode;



public class CommandService {
	
	public static void registerServer(NetworkNode node){
		node.getCommandHub().addCommand(new ConnectCommand(node));
		node.getCommandHub().addCommand(new ServerListCommand(node));
		node.getCommandHub().addCommand(new ServerStartCommand(node));
		node.getCommandHub().addCommand(new ServerStopCommand(node));
		node.getCommandHub().addCommand(new ServerStatusCommand(node));
		node.getCommandHub().addCommand(new ServerSendMessageCommand(node));
	}
	
	public static void registerClient(CommandHub hub){
		
	}
}
