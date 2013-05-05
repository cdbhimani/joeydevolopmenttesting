package com.emptyPockets.network.server;

public interface NetworkNodeListener {
	public void nodeConnected(NetworkConnection node);
	public void nodeDisconnected(NetworkConnection node);
	public void nodeTimeout(NetworkConnection node);
	public void dataRecieved(NetworkNode owner, NetworkConnection node, Object data);
}
