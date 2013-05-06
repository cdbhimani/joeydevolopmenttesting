package com.emptyPockets.network.server;

import com.emptyPockets.network.DataAverager;
import com.emptyPockets.network.log.ServerLogger;
import com.emptyPockets.network.transport.FrameworkMessages.Ping;

public class Pinger {
	long maxTimeout;
	long maxTimeoutCount;
	
	long pingPeroid;
	DataAverager<Integer> pingHistory;
	byte pingId;
	long lastPingTime;
	boolean pingActive;
	int pingTimeoutCount;
	Object pingLock = new Object();
	
	NetworkConnection connection;
	public Pinger(NetworkConnection connection){
		pingPeroid = 5000;
		maxTimeout = 15000;
		maxTimeoutCount = 1;
		pingId = 0;
		pingHistory = new DataAverager<Integer>(5);
		pingActive = false;
		this.connection = connection;
	}
	
	
	public void sendPing(NetworkNode server){
		//Get Next Ping Id
		synchronized (pingLock) {
			pingId++;
			ServerLogger.trace("Sending Ping:"+pingId+" - ClientId:"+connection.clientId);
			lastPingTime = System.currentTimeMillis();
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
			ServerLogger.trace("Ping Recieved :"+pingData.getId()+" clientId:"+connection.clientId);
			if(pingData.getId() == pingId){
				int ping = (int) (System.currentTimeMillis()-lastPingTime);
				pingHistory.addRecord(ping);
				pingActive = false;	
				pingTimeoutCount = 0;
			}
		}
	}

	
	public void update(NetworkNode server){
//		ServerLogger.info("Ping : "+ping);
		/**
		 * Testing Pinging and keep alive
		 */
		synchronized (pingLock) {
			if(pingActive){
				//Check if ping has timed out and send again
				if((System.currentTimeMillis()-lastPingTime) > maxTimeout ){
					pingTimeoutCount++;
					if(pingTimeoutCount < maxTimeoutCount){
						sendPing(server);	
					}else{
						connection.notifyClientTimeout(server);
					}
				}
			}else{
				
				if(lastPingTime+pingPeroid< System.currentTimeMillis()){
					sendPing(server);
				}
			}
		}	
	}


	public int getPing() {
		return (int) pingHistory.getAvg();
	}
}
