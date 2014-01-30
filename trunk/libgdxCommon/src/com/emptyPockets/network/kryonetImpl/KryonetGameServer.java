package com.emptyPockets.network.kryonetImpl;

import java.io.IOException;
import java.util.HashMap;

import com.emptyPockets.network.framework.GameServer;
import com.emptyPockets.network.framework.User;
import com.emptyPockets.network.framework.exceptions.TooManyUsersException;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class KryonetGameServer extends GameServer {
	Server server;
	KryonetGameServerListener listener;

	HashMap<Integer, User> userMap;

	public KryonetGameServer() {
		init();
		userMap = new HashMap<Integer, User>();
	}

	public void init() {
		server = new Server();
		listener = new KryonetGameServerListener(this);
		server.addListener(listener);
	}

	@Override
	public void start() throws IOException {
		server.start();
		server.bind(tcpPort, udpPort);
	}

	@Override
	public void stop() {
		server.stop();
	}

	@Override
	public void send(Object object) {
		server.sendToAllTCP(object);
	}

	@Override
	public void sendUDP(Object object) {
		server.sendToAllUDP(object);
	}

	@Override
	public void sendTCP(User user, Object object) {
		server.sendToTCP(user.getConnectionId(), object);
	}

	@Override
	public void sendUDP(User user, Object object) {
		server.sendToUDP(user.getConnectionId(), object);
	}

	public User getUser(Connection connection) {
		return userMap.get(connection.getID());
	}

	@Override
	public void addUser(User user) throws TooManyUsersException {
		super.addUser(user);
		userMap.put(user.getConnectionId(), user);
	}

	@Override
	public void removeUser(User user) {
		super.removeUser(user);
		userMap.remove(user.getConnectionId());
	}

	public User createUser(Connection connection) {
		User user = new User();
		user.setConnectionId(connection.getID());
		return user;
	}

	public Server getServer() {
		return server;
	}

}
