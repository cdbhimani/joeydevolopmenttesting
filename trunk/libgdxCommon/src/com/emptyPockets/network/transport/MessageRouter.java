package com.emptyPockets.network.transport;

public interface MessageRouter {
	public boolean messageRecieved(MessageEnvelope message);
}
