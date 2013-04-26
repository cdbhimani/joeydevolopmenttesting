package com.emptyPockets.test.datagrams;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import com.esotericsoftware.kryo.io.ByteBufferInputStream;
import com.esotericsoftware.kryo.io.ByteBufferOutputStream;

public class DatagramChannelTest {
	static int packetSize = 20;
	public static void main(String input[]) throws IOException {
		startServer();
		startClient();
	}

	public static void startClient() {
		try {
			DatagramChannel channel = DatagramChannel.open();
			ByteBuffer buffer = ByteBuffer.allocate(packetSize);
			
			String object = new String("abc");
			ByteBufferOutputStream stream = new ByteBufferOutputStream(buffer);
			ObjectOutputStream out = new ObjectOutputStream(stream);
			out.writeObject(object);
			out.flush();
			out.close();
			
			buffer.flip();
			channel.send(buffer, new InetSocketAddress("localhost", 8080));
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void startServer() {
		Thread thread = new Thread() {
			public void run() {
				try {
					DatagramChannel channel = DatagramChannel.open();
					channel.socket().bind(new InetSocketAddress(8080));
					ByteBuffer buffer = ByteBuffer.allocate(packetSize);
					SocketAddress address = channel.receive(buffer);
					
					buffer.flip();
					
					ByteBufferInputStream stream = new ByteBufferInputStream(buffer);
					ObjectInputStream in = new ObjectInputStream(stream);
					Object object = in.readObject();
					in.close();
					stream.close();
					System.out.println(object);

				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 
			}
		};
		thread.start();
	}
}
