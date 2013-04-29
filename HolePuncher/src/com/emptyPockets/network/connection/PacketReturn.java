package com.emptyPockets.network.connection;

import com.emptyPockets.network.transport.TransportObject;

public class PacketReturn implements UDPConnectionListener {

	@Override
	public void notifyObjectRecieved(UDPConnection con, TransportObject object) {
		TransportObject newObj = new TransportObject();
		newObj.port = object.port;
		newObj.host=object.host;
		newObj.data = object.data+"1";
		try {
			con.sendTransportObject(newObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
