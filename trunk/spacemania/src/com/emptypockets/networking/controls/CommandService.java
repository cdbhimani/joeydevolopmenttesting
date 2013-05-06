package com.emptypockets.networking.controls;

import com.emptypockets.network.server.NetworkNode;
import com.emptypockets.networking.controls.commands.networknode.ServerConnectCommand;
import com.emptypockets.networking.controls.commands.networknode.ServerDisconnectCommand;
import com.emptypockets.networking.controls.commands.networknode.ServerListCommand;
import com.emptypockets.networking.controls.commands.networknode.ServerLogCommand;
import com.emptypockets.networking.controls.commands.networknode.ServerSendMessageCommand;
import com.emptypockets.networking.controls.commands.networknode.ServerStartCommand;
import com.emptypockets.networking.controls.commands.networknode.ServerStatusCommand;
import com.emptypockets.networking.controls.commands.networknode.ServerStopCommand;



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
