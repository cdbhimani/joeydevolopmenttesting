package com.emptypockets.network.transport;

import com.esotericsoftware.kryo.Kryo;

public class FrameworkMessages{
	//Variables
	public static FrameworkMessages framework = null;
	
	public static class DisconnectNotification extends FrameworkMessages{
		String nodeName;

		public String toString() {
			StringBuilder rst = new StringBuilder();
			rst.append("{ DisconnectNotification:");
			rst.append(" nodeName=[");
			rst.append(nodeName);
			rst.append("] }");
			return rst.toString();
		}

		
		public DisconnectNotification() {
		}
	
		public DisconnectNotification(String name) {
			this.nodeName = name;
		}
	
		
		public String getNodeName() {
			return nodeName;
		}

		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}
	}
	
	public static class ConnectionRequest extends FrameworkMessages{
		private String nodeName;

		@Override
		public String toString() {
			StringBuilder rst = new StringBuilder();
			rst.append("{ConnectionRequest:");
			rst.append(" nodeName=[");
			rst.append(nodeName);
			rst.append("] }");
			return rst.toString();
		}

		public String getNodeName() {
			return nodeName;
		}

		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}
	}
	
	public static class ConnectionResponse extends FrameworkMessages{
		String nodeName;
		boolean accepted;
		String message;
		int serverClientId;
		
		@Override
		public String toString() {
			StringBuilder rst = new StringBuilder();
			rst.append("{ConnectionResponse:");
			rst.append(" nodeName=[");
			rst.append(nodeName);
			rst.append("], serverClientId=[");
			rst.append(serverClientId);
			rst.append("], accepted=[");
			rst.append(accepted);
			rst.append("], message=[");
			rst.append(message);
			rst.append("] }");
			return rst.toString();
		}
		
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
		public String getNodeName() {
			return nodeName;
		}
		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}

	}
	
	public static class Ping extends FrameworkMessages{
		int clientId;
		private byte id;
		boolean isResponse = false;
		public Ping(){
		}
		
		public Ping(int clientId, byte id){
			this.clientId = clientId;
			this.setId(id);
		}
		
		
		@Override
		public String toString() {
			StringBuilder rst = new StringBuilder();
			rst.append("{ Ping:");
			rst.append(" clientId=[");
			rst.append(clientId);
			rst.append("] pingId=[");
			rst.append(id);
			rst.append("] }");
			return rst.toString();
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
		kryo.register(DisconnectNotification.class);
	}
}
