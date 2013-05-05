package com.emptyPockets.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.emptyPockets.network.controls.CommandHub;
import com.emptyPockets.network.controls.commands.Command;
import com.emptyPockets.network.server.NetworkConnection;
import com.emptyPockets.network.server.NetworkNode;
import com.emptyPockets.network.server.NetworkNodeListener;
import com.emptyPockets.network.server.NetworkNodeListenerAdapter;
import com.emptyPockets.network.server.NodeNotFoundException;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.minlog.Log.Logger;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException, NodeNotFoundException {
		Log.INFO();
		int port = 8080;
		// InetAddress address = InetAddress.getByName("54.217.240.178");
		// InetAddress address = InetAddress.getLocalHost();
		InetAddress address = InetAddress.getByName("127.0.0.1");

		int maxPacketSize = 10 * 1024;

		NetworkNodeListenerAdapter echo = new NetworkNodeListenerAdapter() {
			@Override
			public void dataRecieved(NetworkNode node, NetworkConnection connection, Object data) {
				super.dataRecieved(node, connection, data);
				Log.info(node.getNodeName(), "Data Recieved [" + data + "]");
				node.getCommandHub().processCommand(data.toString());
			}
		};

		final NetworkNode host = new NetworkNode(port, maxPacketSize);
		host.setNodeName("Server");
		host.addListener(echo);
		host.start();
		final NetworkNode client;

		int i = 1;
		client = new NetworkNode(port + i, maxPacketSize);
		client.setNodeName("Client");
		client.addListener(echo);
		client.start();
		Thread.sleep(1000);

		client.connect(address, port);

		Scanner in = new Scanner(System.in);
		String line;
		while ((line = in.nextLine()) != null) {
			try {
				if (line.charAt(0) == 'c') {
					String command = line.substring(1);
					client.getCommandHub().processCommand(command);
				} else {
					host.getCommandHub().processCommand(line);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}
