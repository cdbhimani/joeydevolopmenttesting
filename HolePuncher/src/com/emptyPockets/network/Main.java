package com.emptyPockets.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import com.emptyPockets.network.connection.PacketPrinter;
import com.emptyPockets.network.connection.PacketReturn;
import com.emptyPockets.network.connection.UDPConnection;

public class Main {

	public static void main(String[] args) throws IOException {
		int port = 8080;
		InetAddress address = InetAddress.getByName("54.217.240.178");
//		InetAddress address  = InetAddress.getLocalHost();
//		InetAddress address = InetAddress.getByName("127.0.0.1");
		
		int maxPacketSize=128;
		
		UDPConnection connection = new UDPConnection(maxPacketSize, port);
		connection.addListener(new PacketPrinter());
		connection.addListener(new PacketReturn());
		connection.start();
		
		String message = "Data For Message";
		byte[] data = message.getBytes("UTF-8");
		DatagramPacket packet = new DatagramPacket(data, data.length);
		packet.setAddress(address);
		packet.setPort(port);		
		
		connection.sendPacket(packet);
		
	}
}
