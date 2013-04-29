package com.emptyPockets.network.connection;

import java.net.DatagramPacket;

import com.emptyPockets.network.transport.TransportObject;

public interface UDPConnectionListener{
	public void notifyObjectRecieved(UDPConnection con, TransportObject object);
}
