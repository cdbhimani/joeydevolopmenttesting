package com.emptyPockets.network.framework;

import java.io.IOException;
import java.util.ArrayList;

import com.emptyPockets.network.framework.exceptions.TooManyUsersException;
import com.emptyPockets.network.framework.messages.ServerFullMessage;
import com.emptyPockets.network.framework.routing.MessageRouter;

public abstract class GameServer extends UserHub {
	protected int tcpPort;
	protected int udpPort;

	ArrayList<MessageRouter> routers;
	
	public GameServer() {
		super();
		routers = new ArrayList<MessageRouter>();
	}

	public void setPorts(int tcpPort, int udpPort) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
	}

	public void registerRouter(MessageRouter router){
		synchronized (routers) {
			routers.add(router);	
		}
	}
	public void connected(User user) {
		try {
			addUser(user);
		} catch (TooManyUsersException e) {
			sendTCP(user, new ServerFullMessage());
		}
	}

	public void disconnected(User user) {
		removeUser(user);
	}

	public void recieved(User user, Object data) {
		synchronized (routers) {
			for(MessageRouter router : routers){
				if(router.accepts(data)){
					if(router.recieveMessage(data, user)){
						return;
					}
				}
			}
		}
	}

	public void updatePing() {
		synchronized (users) {
			for (User user : users) {
				ping(user);
			}
		}

	}

	public void ping(User user){
		user.setRequestPing(true);
	}
	public abstract void sendUDP(User user, Object object);
	public abstract void sendTCP(User user, Object object);
	public abstract void sendUDP(Object object);
	public abstract void send(Object object);
	public abstract void stop();
	public abstract void start() throws IOException;

}