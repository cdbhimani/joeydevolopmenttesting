package com.emptypockets.network.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

import com.emptypockets.network.PacketUtils;
import com.emptypockets.network.log.ServerLogger;
import com.emptypockets.network.server.NetworkConnection;
import com.emptypockets.network.server.NetworkNode;
import com.emptypockets.network.transport.NetworkTransferManager;
import com.emptypockets.network.transport.TransportObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class UDPConnection implements Runnable {
	NetworkNode node;
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

	public UDPConnection(NetworkNode node, int maxBufferSize, int localPort) throws IOException {
		this.packetBufferSize = maxBufferSize;
		this.localPort = localPort;
		this.node = node;
		listeners = new ArrayList<UDPConnectionListener>();
		init();
	}

	public void init() throws IOException {
		connection = new DatagramSocket(localPort);

		inData = new byte[packetBufferSize];
		inPacket = new DatagramPacket(inData, inData.length);
		in = new Input(inData);

		outData = new byte[packetBufferSize];
		outPacket = new DatagramPacket(outData, outData.length);
		out = new Output(outData);

		kryo = new Kryo();
		kryo.setRegistrationRequired(true);
		NetworkTransferManager.register(kryo);
	}
	
	public boolean sendObject(Object object, NetworkConnection con) {
		return sendObject(object, con.getClientAddress(), con.getClientPort());
	}

	public boolean sendObject(Object ojb, InetAddress dst, int port) {
		TransportObject transport = new TransportObject();
		transport.data = ojb;
		transport.host = dst;
		transport.nodeName = node.getNodeName();
		transport.port = port;
		ServerLogger.trace(node.getNodeName(), "Sending Object ["+transport+"]");
		return sendTransportObject(transport);
	}

	public boolean sendTransportObject(TransportObject data) {
		try {
			synchronized (outPacket) {
				out.setPosition(0);
				synchronized (kryo) {
					kryo.writeObject(out, data);
				}
				out.flush();

				outPacket.setLength(out.position());
				outPacket.setAddress(data.host);
				outPacket.setPort(data.port);
				sendPacket(outPacket);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void clearPacketData(DatagramPacket pkt) {
		Arrays.fill(pkt.getData(), 0, pkt.getLength(), (byte) 0);
	}

	private void sendPacket(DatagramPacket packet) throws IOException {
		// ServerLogger.TRACE

		ServerLogger.trace(node.getNodeName(), "#################Sending Packet : START ###############");
		PacketUtils.printPacket(packet);
		connection.send(packet);
		clearPacketData(outPacket);
		ServerLogger.trace(node.getNodeName(), "#################Sending Packet : DONE  ###############");
	}

	private void readPacket(DatagramPacket packet) throws IOException {
		ServerLogger.trace(node.getNodeName(), "#################Recieve Packet : WAIT ###############");
		clearPacketData(packet);
		connection.receive(packet);
		PacketUtils.printPacket(packet);
		ServerLogger.trace(node.getNodeName(), "#################Recieve Packet : DONE ###############");
	}

	private TransportObject readObject() throws IOException {
		TransportObject object = null;
		synchronized (in) {
			ServerLogger.trace(node.getNodeName(), "Packet Size : "+inPacket.getLength());
			readPacket(inPacket);
			in.setLimit(inPacket.getLength());
			in.setPosition(0);
			try {
				synchronized (kryo) {
					object = kryo.readObject(in, TransportObject.class);
				}
				object.host = inPacket.getAddress();
				object.port = inPacket.getPort();
			} catch (Exception e) {
				e.printStackTrace();
				ServerLogger.error(node.getNodeName(), "Failed to read object");
			}
		}
		return object;
	}

	public void addListener(UDPConnectionListener list) {
		synchronized (listeners) {
			listeners.add(list);
		}
	}

	public void removeListener(UDPConnectionListener list) {
		synchronized (listeners) {
			listeners.remove(list);
		}
	}

	@Override
	public void run() {
		while (alive) {
			try {
				TransportObject object = readObject();
				if (object != null) {
					ServerLogger.trace(node.getNodeName(), "Data Recieved " + object);
					notifyObjectRecieved(object);
				}else{
					ServerLogger.trace(node.getNodeName(), "No Data Recieved ");
				}
			} catch(SocketException e){
				ServerLogger.info(node.getNodeName(), "Socket Exception");
				stop();
			} catch (Exception e) {
				e.printStackTrace();
				stop();
			}
		}
	}

	private void notifyObjectRecieved(TransportObject object) {
		for (UDPConnectionListener list : listeners) {
			list.notifyObjectRecieved(this, object);
		}
	}

	public void stop() {
		ServerLogger.debug("Stopping UDP connection");
		alive = false;
		connection.close();
	}

	public void start() {
		ServerLogger.debug("Starting UDP connection");
		if (alive == true) {
			return;
		}
		alive = true;
		recieveThread = new Thread(this);
		recieveThread.start();
	}

	
}
