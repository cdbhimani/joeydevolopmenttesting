package com.emptypockets.network.controls.commands.networknode;

import com.emptypockets.network.log.ServerLogger;
import com.emptypockets.network.server.NetworkNode;

public class ServerDisconnectCommand extends NetworkNodeCommand {

	public ServerDisconnectCommand(NetworkNode node) {
		super("disconnect", node);
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
