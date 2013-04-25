package com.emptyPockets.test.nat;


public class MainTesting {
	public static int tcpPort = 8080;
	public static int udpPort = 8081;
	public static String host = "54.247.121.180";
	
	public static void main(String input[]) throws Exception{
//		host = "localhost";
//		tcpPort = 21;
		ClientConnection client = new ClientConnection();
		client.connectServer(host, tcpPort, udpPort);
		
		Thread.sleep(1000000);
	}
}
