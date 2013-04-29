package com.emptyPockets.network.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import com.emptyPockets.network.connection.TransferObjectPrinter;
import com.emptyPockets.network.connection.UDPConnection;
import com.emptyPockets.network.connection.UDPConnectionListener;
import com.emptyPockets.network.transport.NetworkTransferManager;
import com.emptyPockets.network.transport.TransportObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInputStream;
import com.esotericsoftware.kryo.io.ByteBufferOutputStream;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class RelayServer implements UDPConnectionListener{
	UDPConnection con;
	int localPort;
	int maxPacketSize;
	
	public RelayServer(int localPort, int maxPacketSize) throws IOException{
		this.localPort = localPort;
		this.maxPacketSize = maxPacketSize;
		init();
	}
	public void init() throws IOException{
		con = new UDPConnection(maxPacketSize,localPort);
		con.addListener(this);
	}
	public void start(){
		con.start();
	}
	
	public void stop(){
		con.stop();
	}
	public static void main(String args[]){
		Kryo kryo = new Kryo();
		kryo.setRegistrationRequired(true);
		NetworkTransferManager.register(kryo);

		TransportObject data = new TransportObject();
		data.data = "HELLO";
		
		ByteBuffer memory = ByteBuffer.allocate(12);
		ByteBufferOutputStream output = new ByteBufferOutputStream(memory);
		Output out = new Output(output);
		
		kryo.writeObject(out, data);

		memory.flip();
		
		ByteBufferInputStream input = new ByteBufferInputStream(memory);
		Input in = new Input(input);
		
		Object someObject = kryo.readClassAndObject(in);
		in.close();
		
		System.out.println(someObject);

	}
	@Override
	public void notifyObjectRecieved(UDPConnection con, TransportObject object) {
		// TODO Auto-generated method stub
		
	}
}
