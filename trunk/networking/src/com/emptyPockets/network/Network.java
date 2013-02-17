package com.emptyPockets.network;

import com.emptyPockets.network.transport.BodyState;
import com.emptyPockets.network.transport.WorldState;
import com.esotericsoftware.kryonet.EndPoint;


public class Network {
	static Network network;
	public int udpPort = 9080;
	public int tcpPort = 9081;
	public int connectionTimeout = 5000;
	public int discoveryTimeout = 5000;
	
	private Network(){
		
	}
	
	public static Network getNetwork(){
		if(network == null){
			network = new Network();
		}
		return network;
	}
	public void register(EndPoint endPoint){
		endPoint.getKryo().register(BodyState.class);
		endPoint.getKryo().register(WorldState.class);
	}
}
