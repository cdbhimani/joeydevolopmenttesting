package com.emptyPockets.test.nat.transport.messages;

public class LoginData {
	public String serverTCPAddress;
	public String serverUDPAddress;
	public int serverPing;
	public String name;
	
	@Override
	public String toString() {
		StringBuilder data = new StringBuilder();
		data.append("LoginData[");
		data.append("serverTCPAddress=");
		data.append(serverTCPAddress);
		data.append(",serverUDPAddress=");
		data.append(serverUDPAddress);
		data.append(",serverPing=");
		data.append(serverPing);
		data.append(",name=");
		data.append(name);
		data.append("]");
		return data.toString();
	}
}
