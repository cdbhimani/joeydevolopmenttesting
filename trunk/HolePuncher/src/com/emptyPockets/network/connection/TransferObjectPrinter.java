package com.emptyPockets.network.connection;

import com.emptyPockets.network.transport.TransportObject;
import com.esotericsoftware.minlog.Log;

public class TransferObjectPrinter implements UDPConnectionListener {

	@Override
	public void notifyObjectRecieved(UDPConnection con, TransportObject object) {
		Log.trace("\n\n@@@@@@@@@@@@@@@@@Object Recieved : START @@@@@@@@@@@@@@@");
		Log.trace(object.data.toString());
		Log.trace("@@@@@@@@@@@@@@@@@Object Recieved : DONE  @@@@@@@@@@@@@@@\n\n");
	}

}
