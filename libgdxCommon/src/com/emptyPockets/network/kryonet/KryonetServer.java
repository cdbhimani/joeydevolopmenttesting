package com.emptyPockets.network.kryonet;

import java.io.IOException;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.minlog.Log.Logger;

public class KryonetServer {
	int tcpPort;
	int udpPort;
	Server server;
	KryonetListener listener;
	
	public KryonetServer() {
		init();
	}

	public void init() {
		server = new Server();
		listener = new KryonetListener();
		listener.setName("server");
		server.addListener(listener);
	}

	public void setPorts(int tcpPort, int udpPort) {
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
	}

	public void start() throws IOException {
		server.start();
		server.bind(tcpPort, udpPort);
	}

	public void stop() {
		server.stop();
	}
	
	public static void main(String input[]) throws IOException{
		Log.setLogger(new Logger(){@Override
		protected void print(String arg0) {
			System.out.println("Printing Shit");
				super.print(arg0);
		}});
		Log.TRACE();
		
		KryonetServer server = new KryonetServer();
		server.setPorts(54555, 54777);
		server.start();
	}

}
