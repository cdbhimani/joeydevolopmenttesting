package com.emptyPockets.test.kryoNetwork.client;

import com.emptyPockets.test.kryoNetwork.server.ServerState;

public interface ServerStateListener {
	public void notifyStateUpdate(ServerState state);
}
