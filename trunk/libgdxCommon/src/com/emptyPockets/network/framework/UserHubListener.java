package com.emptyPockets.network.framework;

public interface UserHubListener {
	public void userConnected(User user);
	public void userDisconnected(User user);
}
