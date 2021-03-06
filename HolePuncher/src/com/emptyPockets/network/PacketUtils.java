package com.emptyPockets.network;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

import com.emptyPockets.network.log.ServerLogger;

public class PacketUtils {
	public static void printPacket(DatagramPacket pkt){
		if(!ServerLogger.isTRACE()){
			return;
		}
		ServerLogger.trace(">>>>>>>>>>>>>>>>>PACKET START<<<<<<<<<<<<<<<<<<");
		ServerLogger.trace("Address : "+pkt.getAddress());
		ServerLogger.trace("Port    : "+pkt.getPort());
		ServerLogger.trace("Length  : "+pkt.getLength());
		try {
			ServerLogger.trace(new String(pkt.getData(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			ServerLogger.trace("Cant Print Data");
		}
		ServerLogger.trace(">>>>>>>>>>>>>>>>>PACKET END<<<<<<<<<<<<<<<<<<");
	}
}
