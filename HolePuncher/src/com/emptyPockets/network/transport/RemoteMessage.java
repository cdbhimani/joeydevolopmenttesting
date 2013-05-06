package com.emptyPockets.network.transport;

public class RemoteMessage {
	String node;
	String message;

	public RemoteMessage() {
	}

	public RemoteMessage(String node, String msg) {
		this.node = node;
		this.message = msg;
	}

	@Override
	public String toString() {
		StringBuilder rst = new StringBuilder();
		rst.append("{ RemoteMessage : node=[");
		rst.append(node);
		rst.append("], message=[");
		rst.append(message);
		rst.append("] }");
		return rst.toString();
	}
	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
