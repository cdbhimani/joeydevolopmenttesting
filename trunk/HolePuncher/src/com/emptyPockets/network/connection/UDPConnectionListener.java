package com.emptypockets.network.connection;

import com.emptypockets.network.transport.TransportObject;

public interface UDPConnectionListener{
	public void notifyObjectRecieved(UDPConnection con, TransportObject object);
}
