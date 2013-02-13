package com.joey.chain.network.utils;

import com.esotericsoftware.kryonet.EndPoint;
import com.joey.chain.network.transport.EntityState;

public class NetworkUtils {
	
	static NetworkUtils manager = null;
	private int udpPort = 8921;
	private int tcpPort = 8921;
	private int connectionTimeout = 5000;
	private int discoveryTimeout = 5000;
	
	private NetworkUtils(){
		
	}
	
	public static NetworkUtils getNetworkUtils(){
		if(manager == null){
			manager = new NetworkUtils();
		}
		return manager;
	}
	
	public void registerClasses(EndPoint ep){
		ep.getKryo().register(EntityState.class);
	}

	public int getUdpPort() {
		return udpPort;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public int getDiscoveryTimeout() {
		return discoveryTimeout;
	}
}

