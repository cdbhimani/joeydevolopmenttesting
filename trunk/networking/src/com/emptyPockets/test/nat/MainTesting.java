package com.emptyPockets.test.nat;


public class MainTesting {
	public static int tcpPort = 8080;
	public static int udpPort = 8081;
	public static String host = "54.217.240.178";
	
	public static void main(String input[]) throws Exception{
		host = "localhost";
//		tcpPort = 21;
		ClientConnection client = new ClientConnection();
		client.connectServer(host, tcpPort, udpPort);
		client.requestServerStatus();
		Thread.sleep(1000);
		client.requestServerStatus();
		Thread.sleep(1000);
		client.requestServerStatus();
		Thread.sleep(1000);
		Thread.sleep(1000000);
	}
}
