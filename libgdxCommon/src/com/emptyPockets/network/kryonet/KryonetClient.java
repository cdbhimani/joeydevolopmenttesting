package com.emptyPockets.network.kryonet;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class KryonetClient {

	Client client;
	int connectTimeout = 5000;
	int tcpPort;
	int udpPort;
	String host;
	KryonetListener listener;

	public KryonetClient() {
		init();
	}

	public void init() {
		client = new Client();
		listener = new KryonetListener();
		listener.setName("client");
		client.addListener(listener);
	}

	public void setHost(String host) {
		this.host = host;

	}

	public void setPorts(int tcpPort, int udpPort) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
	}

	public void start() throws IOException {
		client.start();
		client.connect(connectTimeout, host, tcpPort, udpPort);
		client.updateReturnTripTime();
		client.setKeepAliveTCP(1000);
		client.setKeepAliveUDP(1000);
	}

	public void send(Object object) {
		client.sendTCP(object);
	}

	public void stop() {
		client.stop();
	}

	public static void main(String input[]) throws IOException, InterruptedException {
		Log.TRACE();

		KryonetClient client = new KryonetClient();
		client.setHost("localhost");
		client.setPorts(54555, 54777);
		client.start();
		Thread.sleep(3000);
		client.send(new Integer(2));
		while (true)
			;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

}
