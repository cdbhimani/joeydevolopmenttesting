package com.emptyPockets.network.controls;

import com.emptyPockets.network.controls.commands.networknode.ServerConnectCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerDisconnectCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerListCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerLogCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerSendMessageCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerStartCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerStatusCommand;
import com.emptyPockets.network.controls.commands.networknode.ServerStopCommand;
import com.emptyPockets.network.server.NetworkNode;



public class CommandService {
	
	public static void registerServer(NetworkNode node){
		node.getCommandHub().addCommand(new ServerConnectCommand(node));
		node.getCommandHub().addCommand(new ServerListCommand(node));
		node.getCommandHub().addCommand(new ServerStartCommand(node));
		node.getCommandHub().addCommand(new ServerStopCommand(node));
		node.getCommandHub().addCommand(new ServerStatusCommand(node));
		node.getCommandHub().addCommand(new ServerSendMessageCommand(node));
		node.getCommandHub().addCommand(new ServerDisconnectCommand(node));
		node.getCommandHub().addCommand(new ServerLogCommand(node));
	}
	
	public static void registerClient(CommandHub hub){
		
	}
}
