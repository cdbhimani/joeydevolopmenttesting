package com.emptyPockets.network;

import java.io.IOException;
import java.net.InetAddress;

import com.emptyPockets.network.server.NetworkNode;
import com.esotericsoftware.minlog.Log;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		Log.set(Log.LEVEL_INFO);
		int port = 8080;
//		InetAddress address = InetAddress.getByName("54.217.240.178");
//		InetAddress address  = InetAddress.getLocalHost();
		InetAddress address = InetAddress.getByName("127.0.0.1");
		
		int maxPacketSize=10*1024;
		
		NetworkNode host = new NetworkNode(port, maxPacketSize);
		host.start();
		for(int i = 1; i < 12; i++){
			NetworkNode client = new NetworkNode(port+i, maxPacketSize);
			client.start();
			client.connect("Test"+i, address, port);
		}
		
	}
}
