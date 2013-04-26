package com.emptyPockets.test.datagrams.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpTestServer extends Thread{
	DatagramSocket datagramSocket;
	byte[] buffer;
	DatagramPacket packet;
	int maxPacketSize;
	
	public UdpTestServer(int port, int maxPacketSize) throws SocketException{
		this.maxPacketSize = maxPacketSize;
		datagramSocket = new DatagramSocket(port);
		buffer = new byte[maxPacketSize];
		packet = new DatagramPacket(buffer, maxPacketSize);
	}
	public void readData() throws Exception{
		datagramSocket.receive(packet);
		String message = new String(packet.getData(), 1, packet.getLength()-1);
		
		System.out.println("Server : Packet Recieved : "+buffer[0]+" - "+message);
		System.out.println(packet.getLength());
	}
	@Override
	public void run() {
		System.out.println("SERVER: Server Started");
		while(true){
			try {
				readData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
