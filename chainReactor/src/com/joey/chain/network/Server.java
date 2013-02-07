package com.joey.chain.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Logger;

public class Server implements Runnable, SimpleSocketHandler {
	public static final int DEFAULT_PORT = 8022;
	Logger log = new Logger(Server.class.toString(),Logger.DEBUG);
	boolean serverAlive = false;
	boolean acceptConnections = false;
	
	int port;
	ServerSocket socketServer;
	
	ArrayList<ServerConnectionManager> activeConnections = new ArrayList<ServerConnectionManager>();
	
	public Server(){
		this(DEFAULT_PORT);
	}
	public Server(int portNumber) {
		this.port = portNumber;
	}
	
	public void startServer() throws IOException{
		log.debug("Starting Server");
		this.socketServer = new ServerSocket(port);
		this.serverAlive = true;
		this.acceptConnections=true;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void stopServer() throws IOException{
		log.debug("Stopping Server");
		acceptConnections = false;
		serverAlive = false;
		socketServer.close();
	}
	
	
	public void startAcceptingConnections(){
		acceptConnections = false;
	}
	
	public void stopAcceptingConnections(){
		acceptConnections = false;
	}
	
	@Override
	public void run() {
		log.info("Server Active");
		while(serverAlive){
			try {
				
				if(acceptConnections){
					log.debug("Awaiting Connection");
					socketServer.setSoTimeout(1000);
					Socket socket= socketServer.accept();
					log.debug("Connection Recieved : "+socket.getRemoteSocketAddress());
					ServerConnectionManager manager = new ServerConnectionManager(socket, this);
					manager.start();
				}else{
					Thread.sleep(1000);
				}	
			}catch(SocketException e){
				if(!socketServer.isClosed()){
					log.error("Socket Error");
					e.printStackTrace();
				}
			}
			catch(Exception e){
				log.error("Error Getting Connection");
				e.printStackTrace();
			}
		}
		log.info("Server Shutdown");
	}

	@Override
	public Object handleSimpleSocketRequest(Object message) {
		// TODO Auto-generated method stub
		return message;
	}

}