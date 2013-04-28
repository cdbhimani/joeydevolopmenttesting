package com.emptyPockets.network;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

public class PacketUtils {
	public static void printPacket(DatagramPacket pkt){
		System.out.println(">>>>>>>>>>>>>>>>>PACKET START<<<<<<<<<<<<<<<<<<");
		System.out.println("Address : "+pkt.getAddress());
		System.out.println("Port    : "+pkt.getPort());
		System.out.println("Length  : "+pkt.getLength());
		try {
			System.out.println(new String(pkt.getData(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			System.out.println("Cant Print Data");
		}
		System.out.println(">>>>>>>>>>>>>>>>>PACKET END<<<<<<<<<<<<<<<<<<");
	}
}
