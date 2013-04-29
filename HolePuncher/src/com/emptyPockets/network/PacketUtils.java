package com.emptyPockets.network;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

import com.esotericsoftware.minlog.Log;

public class PacketUtils {
	public static void printPacket(DatagramPacket pkt){
		Log.trace(">>>>>>>>>>>>>>>>>PACKET START<<<<<<<<<<<<<<<<<<");
		Log.trace("Address : "+pkt.getAddress());
		Log.trace("Port    : "+pkt.getPort());
		Log.trace("Length  : "+pkt.getLength());
		try {
			Log.trace(new String(pkt.getData(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			Log.trace("Cant Print Data");
		}
		Log.trace(">>>>>>>>>>>>>>>>>PACKET END<<<<<<<<<<<<<<<<<<");
	}
}
