package com.emptyPockets.network.transport;

import com.emptyPockets.network.User;

public abstract class MessageEnvelope{
	Object data;
	boolean important;
	User destination;
}
