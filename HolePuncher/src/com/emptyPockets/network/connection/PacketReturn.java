package com.emptyPockets.network.connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

import com.emptyPockets.network.PacketUtils;

public class PacketReturn implements UDPConnectionListener {

	@Override
	public void notifyPacketRecieved(UDPConnection con, DatagramPacket pkt) {
		DatagramPacket newPkt = new DatagramPacket(pkt.getData(), pkt.getData().length);
		newPkt.setAddress(pkt.getAddress());
		newPkt.setPort(pkt.getPort());
		try {
			con.sendPacket(newPkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
