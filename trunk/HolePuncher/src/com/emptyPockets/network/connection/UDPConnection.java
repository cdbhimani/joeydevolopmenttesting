package com.emptyPockets.network.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

import com.emptyPockets.network.PacketUtils;

public class UDPConnection implements Runnable{
	Thread recieveThread;
	boolean alive = false;
	
	DatagramSocket connection;
	DatagramPacket recievePacket;
	int maxBufferSize;

	int localPort;
	
	ArrayList<UDPConnectionListener> listeners;
	
	public UDPConnection(int maxBufferSize, int localPort) throws IOException{
		this.maxBufferSize = maxBufferSize;
		this.localPort=localPort;
		listeners = new ArrayList<UDPConnectionListener>();
		init();
	}
	
	public void init() throws IOException{
		connection = new DatagramSocket(localPort);
		recievePacket = createPacket(new byte[maxBufferSize]);
	}
	
	private DatagramPacket createPacket(byte[] buffer){
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		return packet;
	}
	
	public void sendPacket(DatagramPacket packet) throws IOException{
		System.out.println("Sending Packet");
		PacketUtils.printPacket(packet);
		connection.send(packet);
	}
	
	private void readPacket(DatagramPacket packet) throws IOException{
		System.out.println("Waiting For Packet");
		connection.receive(packet);
		System.out.println("Packet Recieved");
	}
	
	private void notifyPacketRecieved(DatagramPacket packet){
		synchronized (listeners) {
			for(UDPConnectionListener list : listeners){
				list.notifyPacketRecieved(this, packet);
			}
		}
	}
	
	public void addListener(UDPConnectionListener list){
		synchronized (listeners) {
			listeners.add(list);
		}
	}
	
	public void removeListener(UDPConnectionListener list){
		synchronized (listeners) {
			listeners.remove(list);
		}
	}
	
	@Override
	public void run() {
		while(alive){
			try{
				readPacket(recievePacket);
				notifyPacketRecieved(recievePacket);
			}catch(Exception e){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
				}
			}
		}
	}

	public void stop(){
		alive = false;
		connection.close();
	}
	
	public void start() {
		if(alive == true){
			return;
		}
		alive = true;
		recieveThread = new Thread(this);
		recieveThread.start();
	}
}
