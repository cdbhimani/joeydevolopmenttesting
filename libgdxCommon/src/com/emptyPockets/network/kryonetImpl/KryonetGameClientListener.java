package com.emptyPockets.network.kryonetImpl;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class KryonetGameClientListener extends Listener{
	KryonetGameClient client;
	
	public KryonetGameClientListener(KryonetGameClient client){
		this.client = client;
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
	}

	@Override
	public void idle(Connection connection) {
		super.idle(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		client.recieved(object);
	}
	
}
