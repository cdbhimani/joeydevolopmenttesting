package com.emptyPockets.network.controls.commands.networknode;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.emptyPockets.network.server.NetworkNode;

public class ConnectCommand extends NetworkNodeCommand {

	public ConnectCommand(NetworkNode nodes) {
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
