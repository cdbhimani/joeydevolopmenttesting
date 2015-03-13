package com.emptyPockets.network.controls.commands.networknode;

import com.emptyPockets.network.log.ServerLogger;
import com.emptyPockets.network.server.NetworkNode;

public class ServerDisconnectCommand extends NetworkNodeCommand {

	public ServerDisconnectCommand(NetworkNode node) {
		super("disconnect", node);
		setDescription("Disconnects from a remote node. If no name is provided, all connections will be closed. :  disconnect {name}");
	}

	@Override
	public void proceeArg(String data) {
		ServerLogger.debug(node.getNodeName(), "Disconnect Recieved ["+data+"]");
		if(data == null || data.length() == 0){
			node.disconnectAllConnections();
		}else{
			node.disconnect(data);
		}
	}

}
