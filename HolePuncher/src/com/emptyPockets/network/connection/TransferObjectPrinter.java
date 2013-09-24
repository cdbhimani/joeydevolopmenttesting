package com.emptyPockets.network.connection;

import com.emptyPockets.network.log.ServerLogger;
import com.emptyPockets.network.transport.TransportObject;


public class TransferObjectPrinter implements UDPConnectionListener {
	@Override
	public void notifyObjectRecieved(UDPConnection con, TransportObject object) {
		ServerLogger.trace("\n\n@@@@@@@@@@@@@@@@@Object Recieved : START @@@@@@@@@@@@@@@");
		ServerLogger.trace(object.data.toString());
		ServerLogger.trace("@@@@@@@@@@@@@@@@@Object Recieved : DONE  @@@@@@@@@@@@@@@\n\n");
	}

}
