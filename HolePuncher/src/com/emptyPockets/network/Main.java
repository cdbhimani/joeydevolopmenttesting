package com.emptyPockets.network;

import java.io.IOException;
import java.net.InetAddress;

import com.emptyPockets.network.connection.TransferObjectPrinter;
import com.emptyPockets.network.connection.UDPConnection;
import com.emptyPockets.network.server.NetworkNode;
import com.emptyPockets.network.transport.TransportObject;
import com.esotericsoftware.minlog.Log;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		Log.set(Log.LEVEL_DEBUG);
		int portA = 8080;
		int portB = 8081;
//		InetAddress address = InetAddress.getByName("54.217.240.178");
//		InetAddress address  = InetAddress.getLocalHost();
		InetAddress address = InetAddress.getByName("127.0.0.1");
		
		int maxPacketSize=10*1024;
		
		NetworkNode nodeA = new NetworkNode(portA, maxPacketSize);
		NetworkNode nodeB = new NetworkNode(portB, maxPacketSize);
		
		nodeA.start();
		nodeB.start();
		
		nodeA.connect("Testing",address, portB);
		
	}
}
