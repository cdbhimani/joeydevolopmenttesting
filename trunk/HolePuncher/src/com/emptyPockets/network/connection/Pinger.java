package com.emptyPockets.network.connection;

import com.emptyPockets.network.server.NetworkNode;
import com.emptyPockets.network.transport.FrameworkMessages.Ping;

public class Pinger {
	long maxTimeout;
	long maxTimeoutCount;
	
	long pingPeroid;
	int ping;
	byte pingId;
	long lastPingTime;
	boolean pingActive;
	int pingTimeoutCount;
	Object pingLock = new Object();
	
	NetworkConnection connection;
	public Pinger(NetworkConnection connection){
		pingPeroid = 5000;
		maxTimeout = 1000;
		maxTimeoutCount = 3;
		pingId = 0;
		pingActive = false;
		this.connection = connection;
	}
	
	
	public void sendPing(NetworkNode server){
		//Get Next Ping Id
		synchronized (pingLock) {
			pingId++;
			Ping p = new Ping(connection.clientId, pingId);
			if(server.getConnection().sendObject(p, connection.clientAddress, connection.clientPort)){
				pingActive = true;
			} else{
				pingActive = false;
			}
		}
	}
	
	public void recievePing(Ping pingData){
		synchronized (pingLock) {
			if(pingData.getId() == pingId){
				ping = (int) (System.currentTimeMillis()-lastPingTime);
				pingActive = false;	
				pingTimeoutCount = 0;
			}
		}
	}

	
	public void update(NetworkNode server){
		/**
		 * Testing Pinging and keep alive
		 */
		synchronized (pingLock) {
			if(pingActive){
				//Check if ping has timed out and send again
				if(lastPingTime+maxTimeout > System.currentTimeMillis()){
					pingTimeoutCount++;
					if(pingTimeoutCount < maxTimeoutCount){
						sendPing(server);	
					}else{
						connection.notifyClientTimeout(server);
					}
				}
			}else{
				if(lastPingTime+pingPeroid> System.currentTimeMillis()){
					sendPing(server);
				}
			}
		}	
	}


	public int getPing() {
		return ping;
	}
}
