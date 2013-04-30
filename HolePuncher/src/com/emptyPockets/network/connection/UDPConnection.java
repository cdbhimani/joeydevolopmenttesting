package com.emptyPockets.network.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

import com.emptyPockets.network.PacketUtils;
import com.emptyPockets.network.transport.FrameworkMessages;
import com.emptyPockets.network.transport.FrameworkMessages.Ping;
import com.emptyPockets.network.transport.NetworkTransferManager;
import com.emptyPockets.network.transport.TransportObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;

public class UDPConnection implements Runnable{
	Kryo kryo;
	Thread recieveThread;
	boolean alive = false;
	
	DatagramSocket connection;
	int packetBufferSize;

	Output out;
	byte[] outData;
	DatagramPacket outPacket;

	
	Input in;
	byte[] inData;
	DatagramPacket inPacket;
	
	int localPort;
	
	ArrayList<UDPConnectionListener> listeners;
	
	public UDPConnection(int maxBufferSize, int localPort) throws IOException{
		this.packetBufferSize = maxBufferSize;
		this.localPort=localPort;
		listeners = new ArrayList<UDPConnectionListener>();
		init();
	}
	
	public void init() throws IOException{
		connection = new DatagramSocket(localPort);
		
		inData = new byte[packetBufferSize];
		inPacket =  new DatagramPacket(inData, inData.length);
		in = new Input(inData);		
		
		outData = new byte[packetBufferSize];
		outPacket =  new DatagramPacket(outData, outData.length);
		out = new Output(outData);
		
		kryo = new Kryo();
		kryo.setRegistrationRequired(true);
		NetworkTransferManager.register(kryo);
	}
	
	public boolean sendObject(Object ojb, InetAddress dst, int port){
		TransportObject transport= new TransportObject();
		transport.data = ojb;
		transport.host = dst;
		transport.port = port;
		return sendTransportObject(transport);
	}
	
	public boolean sendTransportObject(TransportObject data){
		try{
		synchronized (outPacket) {
			out.setPosition(0);
			kryo.writeObject(out, data);
			out.flush();
			
			outPacket.setLength(out.position());
			outPacket.setAddress(data.host);
			outPacket.setPort(data.port);
			sendPacket(outPacket);
		}
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void clearPacketData(DatagramPacket pkt){
		Arrays.fill(pkt.getData(), 0,pkt.getLength(),(byte)0);
	}
	private void sendPacket(DatagramPacket packet) throws IOException{
//		Log.TRACE
		
		Log.trace("#################Sending Packet : START ###############");
		PacketUtils.printPacket(packet);
		connection.send(packet);
		clearPacketData(outPacket);
		Log.trace("#################Sending Packet : DONE  ###############");
	}
	
	private void readPacket(DatagramPacket packet) throws IOException{
		Log.trace("#################Recieve Packet : WAIT ###############");
		clearPacketData(packet);
		connection.receive(packet);
		PacketUtils.printPacket(packet);
		Log.trace("#################Recieve Packet : DONE ###############");
	}
	
	private TransportObject readObject() throws IOException{
		readPacket(inPacket);
		in.setLimit(inPacket.getLength());
		in.setPosition(0);
		try {
			TransportObject object = kryo.readObject(in, TransportObject.class);
			object.host = inPacket.getAddress();
			object.port = inPacket.getPort();
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
				TransportObject object = readObject();
				if(object != null){
					notifyObjectRecieved(object);
				}
			}catch(Exception e){
				e.printStackTrace();
				stop();
			}
		}
	}

	private void notifyObjectRecieved(TransportObject object) {
		for(UDPConnectionListener list : listeners){
			list.notifyObjectRecieved(this, object);
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
