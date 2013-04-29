package com.emptyPockets.network;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Scanner;

import com.emptyPockets.network.connection.TransferObjectPrinter;
import com.emptyPockets.network.connection.PacketReturn;
import com.emptyPockets.network.connection.UDPConnection;
import com.emptyPockets.network.transport.FrameworkMessages;
import com.emptyPockets.network.transport.TransportObject;
import com.esotericsoftware.minlog.Log;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		Log.set(Log.LEVEL_DEBUG);
		int port = 8080;
//		InetAddress address = InetAddress.getByName("54.217.240.178");
//		InetAddress address  = InetAddress.getLocalHost();
		InetAddress address = InetAddress.getByName("127.0.0.1");
		
		int maxPacketSize=10*1024;
		UDPConnection connection = new UDPConnection(maxPacketSize, port);
		connection.addListener(new TransferObjectPrinter());
		connection.start();
		
		TransportObject transport = new TransportObject();
		transport.host = address;
		transport.port = port;
		transport.data = "ABCD";

		
//		Scanner in = new Scanner(System.in);
//		String line = "ABBA";
//		while((line = in.nextLine()) != null){
//			transport.data = line;
//			connection.sendTransportObject(transport);
//			connection.ping(address, port);
//		}
		while(true){
			connection.ping(address, port);
			Thread.sleep(500);
		}
	}
}
