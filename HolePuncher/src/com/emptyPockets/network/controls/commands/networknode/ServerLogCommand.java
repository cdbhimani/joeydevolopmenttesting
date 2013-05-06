package com.emptyPockets.network.controls.commands.networknode;

import com.emptyPockets.network.log.ServerLogger;
import com.emptyPockets.network.server.NetworkNode;

public class ServerLogCommand extends NetworkNodeCommand {

	public ServerLogCommand(NetworkNode node) {
		super("log", node);
	}

	@Override
	public void proceeArg(String data) {
		ServerLogger.INFO();
		int level = Integer.parseInt(data);
		switch (level) {
		case 0:	
			ServerLogger.info(node.getNodeName(), "Log set to : NONE");
			ServerLogger.NONE();
			break;
		case 1:
			ServerLogger.info(node.getNodeName(), "Log set to : ERROR");
			ServerLogger.ERROR();
			break;
		case 2:
			ServerLogger.info(node.getNodeName(), "Log set to : WARN");
			ServerLogger.WARN();
			break;
		case 3:	
			ServerLogger.info(node.getNodeName(), "Log set to : INFO");
			ServerLogger.INFO();
			break;
		case 4:	
			ServerLogger.info(node.getNodeName(), "Log set to : DEBUG");
			ServerLogger.DEBUG();
			break;
		case 5:	
			ServerLogger.info(node.getNodeName(), "Log set to : TRACE");
			ServerLogger.TRACE();
			break;
		default:
			ServerLogger.INFO();
			break;
		}
	}

}
