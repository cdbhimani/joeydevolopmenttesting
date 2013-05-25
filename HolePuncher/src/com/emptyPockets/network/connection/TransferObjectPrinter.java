package com.emptypockets.network.connection;

import com.emptypockets.network.log.ServerLogger;
import com.emptypockets.network.transport.TransportObject;


public class TransferObjectPrinter implements UDPConnectionListener {
	@Override
	public void notifyObjectRecieved(UDPConnection con, TransportObject object) {
		ServerLogger.trace("\n\n@@@@@@@@@@@@@@@@@Object Recieved : START @@@@@@@@@@@@@@@");
		ServerLogger.trace(object.data.toString());
		ServerLogger.trace("@@@@@@@@@@@@@@@@@Object Recieved : DONE  @@@@@@@@@@@@@@@\n\n");
	}

}
