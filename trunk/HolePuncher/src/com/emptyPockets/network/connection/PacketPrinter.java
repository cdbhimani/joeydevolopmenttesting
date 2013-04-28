package com.emptyPockets.network.connection;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

import com.emptyPockets.network.PacketUtils;

public class PacketPrinter implements UDPConnectionListener {

	@Override
	public void notifyPacketRecieved(UDPConnection con, DatagramPacket pkt) {
		PacketUtils.printPacket(pkt);
	}

}
