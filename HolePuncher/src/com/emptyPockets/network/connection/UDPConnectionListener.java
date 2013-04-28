package com.emptyPockets.network.connection;

import java.net.DatagramPacket;

public interface UDPConnectionListener{
	public void notifyPacketRecieved(UDPConnection con, DatagramPacket packet);
}
