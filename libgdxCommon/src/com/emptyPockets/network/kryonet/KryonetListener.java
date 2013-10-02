package com.emptyPockets.network.kryonet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class KryonetListener extends Listener{
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void connected(Connection connection) {
		System.out.println(name+" Connect");
		super.connected(connection);
	}

	@Override
	public void disconnected(Connection connection) {
		System.out.println(name+" Disconnect");
		super.disconnected(connection);
	}

	@Override
	public void idle(Connection connection) {
		System.out.println(name+" Idle");
		super.idle(connection);
	}

	@Override
	public void received(Connection connection, Object object) {
		System.out.println(name+" recieved object");
		super.received(connection, object);
	}
	
}
