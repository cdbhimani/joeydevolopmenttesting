package com.emptyPockets.network.controls.commands.networknode;

import com.emptyPockets.network.server.NetworkNode;
import com.emptyPockets.network.server.NodeNotFoundException;
import com.esotericsoftware.minlog.Log;

public class ServerSendMessageCommand extends NetworkNodeCommand {

	public ServerSendMessageCommand(NetworkNode nodes) {
		super("send", nodes);
	}

	@Override
	public void proceeArg(String data) {
		Log.trace("Send Message Data ["+data+"]");
		String args[] = data.split(" ",2);
		try {
			Log.debug(node.getNodeName(), "Sending data to ["+args[0]+"] : "+args[1]);
			node.sendData(args[0],args[1]);
		} catch (NodeNotFoundException e) {
			Log.error(node.getNodeName(), "Node not found", e);
			e.printStackTrace();
		}
	}

}
