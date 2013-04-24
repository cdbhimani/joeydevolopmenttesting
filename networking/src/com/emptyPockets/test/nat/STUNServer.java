package com.emptyPockets.test.nat;

import java.io.IOException;

import com.emptyPockets.test.nat.transport.Network;
import com.emptyPockets.test.nat.transport.stun.STUNRequest;
import com.emptyPockets.test.nat.transport.stun.STUNResponse;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class STUNServer {
	Server server;
	int udpPort;
	int tcpPort;

	public STUNServer(int tcpPort, int udpPort) {
		this.udpPort = udpPort;
		this.tcpPort = tcpPort;
	}

	public void startServer() throws IOException {
		server = new Server();
		server.start();
		server.bind(tcpPort, udpPort);
		Network.registerClasses(server);
		
		server.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				System.out.printf("HOST MANAGER:Recieved Object from [%s] : %s\n",connection.toString(),object.toString());
				if (object instanceof STUNRequest) {
					STUNResponse response = new STUNResponse();
					response.tcpPort = connection.getRemoteAddressTCP().getPort();
					response.tcpHost = connection.getRemoteAddressTCP().getHostName();
					
					response.udpPort = connection.getRemoteAddressUDP().getPort();
					response.udpHost = connection.getRemoteAddressUDP().getHostName();
					response.message = "TCP";
					System.out.printf("HOST MANAGER: Sending Stun Response : "+response);
					connection.sendTCP(response);
				}
			}
		});
	}

	public static void main(String input[]) throws IOException {
		STUNServer server = new STUNServer(8080, 8081);
		server.startServer();
	}
}
