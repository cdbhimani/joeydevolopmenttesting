package com.emptyPockets.network.transport;

import com.esotericsoftware.kryo.Kryo;

public class FrameworkMessages{
	//Variables
	public static FrameworkMessages framework = null;
	
	public static class ConnectionRequest extends FrameworkMessages{
		private String username;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}
	
	public static class ConnectionResponse extends FrameworkMessages{
		boolean accepted;
		String message;
		int serverClientId;
		public boolean isAccepted() {
			return accepted;
		}
		public void setAccepted(boolean accepted) {
			this.accepted = accepted;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public int getServerClientId() {
			return serverClientId;
		}
		public void setServerClientId(int serverClientId) {
			this.serverClientId = serverClientId;
		}
	}
	
	public static class Ping extends FrameworkMessages{
		int clientId;
		private byte id;
		boolean isResponse = false;
		String data = new String(new byte[1024]);
		public Ping(){
		}
		
		public Ping(int clientId, byte id){
			this.clientId = clientId;
			this.setId(id);
		}
		
		public boolean isResponse() {
			return isResponse;
		}
		public void setResponse(boolean isResponse) {
			this.isResponse = isResponse;
		}

		public byte getId() {
			return id;
		}

		public void setId(byte id) {
			this.id = id;
		}

		public int getClientId() {
			return clientId;
		}
	}

	private FrameworkMessages(){
	}
	
	public static FrameworkMessages getFrameWork(){
		if(framework == null){
			 framework= new FrameworkMessages();
		}
		return framework;
	}
	
	public static void register(Kryo kryo) {
		kryo.register(Ping.class);
		kryo.register(ConnectionRequest.class);
		kryo.register(ConnectionResponse.class);
	}
}
