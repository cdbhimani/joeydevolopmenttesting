package com.emptyPockets.test.datagrams;

import java.net.InetAddress;

import com.emptyPockets.test.datagrams.client.UdpTestClient;
import com.emptyPockets.test.datagrams.server.UdpTestServer;


public class UdpTestConnectionMain {

	public static void main(String input[]) throws Exception{
		int port = 8080;
		int serverPacketSize = 500;
		int clientPacketSize = 10;
		
		String message = "ABCDEFG";
		InetAddress address = InetAddress.getByName("localhost");
		
		UdpTestServer server = new UdpTestServer(port, serverPacketSize);
		UdpTestClient client = new UdpTestClient(clientPacketSize);
		server.start();
		client.setDestination(address, port);
		client.sendData(message);
		client.sendData(message);
		client.sendData(message);
	}
}
