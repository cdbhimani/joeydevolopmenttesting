package com.emptyPockets.test.nat.client;

import com.emptyPockets.test.nat.server.ServerState;

public interface ServerStateListener {
	public void notifyStateUpdate(ServerState state);
}
