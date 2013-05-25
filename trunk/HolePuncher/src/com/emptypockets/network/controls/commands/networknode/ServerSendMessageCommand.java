package com.emptypockets.network.controls.commands.networknode;

import com.emptypockets.network.log.ServerLogger;
import com.emptypockets.network.server.NetworkNode;
import com.emptypockets.network.server.NodeNotFoundException;
import com.emptypockets.network.transport.RemoteMessage;

public class ServerSendMessageCommand extends NetworkNodeCommand {

	public ServerSendMessageCommand(NetworkNode nodes) {
		super("send", nodes);
	}

	@Override
	public void proceeArg(String data) {
		ServerLogger.trace("Send Message Data ["+data+"]");
		String args[] = data.split(" ",2);
		try {
			ServerLogger.debug(node.getNodeName(), "Sending data to ["+args[0]+"] : "+args[1]);
			RemoteMessage message = new RemoteMessage(node.getNodeName(), args[1]);
			node.sendData(args[0],message);
		} catch (NodeNotFoundException e) {
			ServerLogger.error(node.getNodeName(), "Node not found", e);
			e.printStackTrace();
		}
	}

}
