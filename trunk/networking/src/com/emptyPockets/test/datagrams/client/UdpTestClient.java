package com.emptyPockets.test.datagrams.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpTestClient {
	DatagramSocket clientSocket;
	
	DatagramPacket packet;
	int packetSize;
	byte[] sendData;
	
	byte msgId = 0;
	public UdpTestClient(int packetSize) throws SocketException{
		this.packetSize = packetSize;
		clientSocket = new DatagramSocket();
		sendData = new byte[packetSize];
		packet = new DatagramPacket(sendData,packetSize);
	}
	
	public void setDestination(InetAddress address, int port){
		packet.setAddress(address);
		packet.setPort(port);
	}
	
	public void sendData(String message) throws IOException{
		msgId++;
		sendData[0] = msgId;
		byte[] data = message.getBytes("UTF-8");
		System.arraycopy(message.getBytes("UTF-8"), 0, sendData,1,Math.min(data.length, packetSize-1));
		System.out.println("\tCLIENT:Sending "+msgId);
		clientSocket.send(packet);
	}
}
