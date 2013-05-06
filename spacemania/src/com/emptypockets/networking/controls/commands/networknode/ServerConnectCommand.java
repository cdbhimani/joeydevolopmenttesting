package com.emptypockets.networking.controls.commands.networknode;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.emptypockets.network.server.NetworkNode;

public class ServerConnectCommand extends NetworkNodeCommand {

	public ServerConnectCommand(NetworkNode nodes) {
		super("connect", nodes);
	}

	@Override
	public void proceeArg(String data) {
		String arg[] = data.split(":");
		String address = arg[0];
		int port = Integer.parseInt(arg[1]);
		try {
			node.connect(InetAddress.getByName(address), port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
