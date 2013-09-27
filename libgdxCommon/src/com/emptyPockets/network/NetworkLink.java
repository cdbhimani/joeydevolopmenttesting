package com.emptyPockets.network;

import com.emptyPockets.network.transport.MessageEnvelope;

public abstract class NetworkLink {

	public abstract void sendMessage(MessageEnvelope message);
	
	public abstract void messageRecieved(MessageEnvelope message);
}
