package com.emptyPockets.network.transport;

import java.net.InetAddress;

public class TransportObject {
	public Object data;
	public transient InetAddress host;
	public transient int port;
}
