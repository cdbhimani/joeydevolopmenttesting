package com.emptyPockets.network.transport;

import java.net.InetAddress;

public class TransportObject {
	public Object data;
	public transient InetAddress host;
	public transient int port;
	
	@Override
	public String toString() {
		StringBuilder rst = new StringBuilder();
		rst.append("[host:");
		rst.append(host);
		rst.append(" port:");
		rst.append(port);
		rst.append(" data:");
		rst.append(data);
		rst.append("]");
		return rst.toString();
	}
}
