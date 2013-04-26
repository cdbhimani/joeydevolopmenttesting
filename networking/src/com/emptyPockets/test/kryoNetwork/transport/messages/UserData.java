package com.emptyPockets.test.kryoNetwork.transport.messages;

import com.badlogic.gdx.math.Vector2;

public class UserData {
	public String serverTCPAddress;
	public String serverUDPAddress;
	public int serverPing;
	
	public String name;
	
	public Vector2 pos;
	public Vector2 vel;
	
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
