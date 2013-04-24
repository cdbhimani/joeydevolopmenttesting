package com.emptyPockets.test.nat;


public class MainTesting {
	public static int tcpPort = 8080;
	public static int udpPort = 8081;
	public static String host = "54.247.121.180";
	
	public static void main(String input[]) throws Exception{
		host = "localhost";
		ClientConnection client = new ClientConnection();
		client.connectServerManager(host, tcpPort, udpPort);
		
		client.sendServerManagerSTUN();
		client.sendServerManagerSTUN();
		client.sendServerManagerSTUN();
		
		Thread.sleep(1000000);
	}
}
