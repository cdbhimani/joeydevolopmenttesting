package com.emptyPockets.network.framework;


public class User {
	
	static transient int userCount = 1;
	String userName = "Guest-"+userCount++;
	boolean guest = true;
	private boolean requestPing = false;
	int connectionId;
	int ping;

	public String getUserName() {
		return userName;
	}

	public int getConnectionId() {
		return connectionId;
	}

	public int getPing() {
		return ping;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

	public boolean isRequestPing() {
		return requestPing;
	}

	public void setRequestPing(boolean requestPing) {
		this.requestPing = requestPing;
	}
}
