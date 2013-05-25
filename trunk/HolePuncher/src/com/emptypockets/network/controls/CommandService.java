package com.emptypockets.network.controls;

import com.emptypockets.network.controls.commands.networknode.ServerConnectCommand;
import com.emptypockets.network.controls.commands.networknode.ServerDisconnectCommand;
import com.emptypockets.network.controls.commands.networknode.ServerListCommand;
import com.emptypockets.network.controls.commands.networknode.ServerLogCommand;
import com.emptypockets.network.controls.commands.networknode.ServerSendMessageCommand;
import com.emptypockets.network.controls.commands.networknode.ServerStartCommand;
import com.emptypockets.network.controls.commands.networknode.ServerStatusCommand;
import com.emptypockets.network.controls.commands.networknode.ServerStopCommand;
import com.emptypockets.network.server.NetworkNode;



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
