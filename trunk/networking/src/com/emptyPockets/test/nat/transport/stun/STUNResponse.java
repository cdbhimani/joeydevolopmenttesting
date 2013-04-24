package com.emptyPockets.test.nat.transport.stun;

public class STUNResponse {

	public String message;
	public int tcpPort;
	public int udpPort;
	
	public String tcpHost;
	public String udpHost;

	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(super.toString());
		result.append(message);
		result.append(",");
		result.append(tcpPort);
		result.append(",");
		result.append(tcpHost);
		result.append(",");
		result.append(udpPort);
		result.append(",");
		result.append(udpHost);
		return result.toString();
	}
}
