package com.joey.chain.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ServerConnectionManager extends Thread{
	Socket socket;
	boolean alive = true;
	boolean reading = true;
	SimpleSocketHandler simpleSocketHandler;
	public ServerConnectionManager(Socket socket, SimpleSocketHandler handler){
		this.socket = socket;
		this.simpleSocketHandler=handler;
	}

	public void run() {
   	
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Object input = null;
            Object output = null;

            input = in.readObject();

            output = simpleSocketHandler.handleSimpleSocketRequest(input); 

            out.writeObject(output);
            
            out.close();
            in.close();
            socket.close();

        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}