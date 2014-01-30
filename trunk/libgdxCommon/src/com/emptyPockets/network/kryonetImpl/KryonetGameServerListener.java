package com.emptyPockets.network.kryonetImpl;

import com.emptyPockets.network.framework.User;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

public class KryonetGameServerListener extends Listener{
	KryonetGameServer server;
	
	public KryonetGameServerListener(KryonetGameServer server){
		this.server = server;
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
		User user = server.getUser(connection);
		if(user == null){
			user = server.createUser(connection);
			server.connected(user);
		}
		
		if(user.isRequestPing()){
			connection.updateReturnTripTime();
			user.setRequestPing(false);
		}
		user.setPing(connection.getReturnTripTime());
		
		if(object instanceof FrameworkMessage){
			//These are sunk
		}else{
			server.recieved(user,object);
		}
	}
	
}
