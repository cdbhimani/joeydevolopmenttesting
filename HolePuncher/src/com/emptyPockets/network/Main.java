package com.emptyPockets.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

import com.emptyPockets.network.log.LogListener;
import com.emptyPockets.network.log.ServerLogger;
import com.emptyPockets.network.server.NetworkConnection;
import com.emptyPockets.network.server.NetworkNode;
import com.emptyPockets.network.server.NetworkNodeListenerAdapter;
import com.emptyPockets.network.server.NodeNotFoundException;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException, NodeNotFoundException {
		ServerLogger.INFO();
		int port = 8080;
		// InetAddress address = InetAddress.getByName("54.217.240.178");
		// InetAddress address = InetAddress.getLocalHost();
		InetAddress address = InetAddress.getByName("127.0.0.1");

		int maxPacketSize = 10 * 1024;

		NetworkNodeListenerAdapter echo = new NetworkNodeListenerAdapter() {
			@Override
			public void dataRecieved(NetworkNode node, NetworkConnection connection, Object data) {
				super.dataRecieved(node, connection, data);
				ServerLogger.info(node.getNodeName(), "Data Recieved [" + data + "]");
			}
		};

		final NetworkNode host = new NetworkNode(port, maxPacketSize);
		host.setNodeName("server");
//		host.start();
		host.addListener(echo);
		final NetworkNode client;

		ServerLogger.info("Startup");
//		int i = 1;
//		client = new NetworkNode(port + i, maxPacketSize);
//		client.setNodeName("client");
//		client.start();
//		client.addListener(echo);
//		Thread.sleep(1000);
//
//		client.connect(address, port);

		Scanner in = new Scanner(System.in);
		String line;
		while ((line = in.nextLine()) != null) {
			try {
				if (line.length() > 0) {
					if (line.charAt(0) == 'c') {
						String command = line.substring(1);
//						client.getCommandHub().processCommand(command);
					} else {
						host.getCommandHub().processCommand(line);
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}
