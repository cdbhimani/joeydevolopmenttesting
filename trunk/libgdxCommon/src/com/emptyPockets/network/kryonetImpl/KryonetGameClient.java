package com.emptyPockets.network.kryonetImpl;

import java.io.IOException;

import com.emptyPockets.network.framework.GameClient;
import com.esotericsoftware.kryonet.Client;

public class KryonetGameClient extends GameClient {
	Client client;
	KryonetListener listener;

	public KryonetGameClient() {
		init();
	}

	public void init() {
		client = new Client();
		listener = new KryonetListener();
		listener.setName("client");
		client.addListener(listener);
	}

	@Override
	public void startConnection() throws IOException{
		client.start();
		client.connect(connectTimeout, host, tcpPort, udpPort);
	}

	@Override
	public void stopConnection() {
		client.stop();
	}

	@Override
	public void sendTCP(Object object) {
		client.sendTCP(object);
	}

	@Override
	public void sendUDP(Object object) {
		client.sendUDP(object);
	}

	public Client getClient() {
		return client;
	}

}
