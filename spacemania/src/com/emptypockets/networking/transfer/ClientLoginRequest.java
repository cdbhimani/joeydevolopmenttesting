package com.emptypockets.networking.transfer;

public class ClientLoginRequest {
	String username;

	public ClientLoginRequest() {
	}

	public ClientLoginRequest(String name) {
		setUsername(name);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
