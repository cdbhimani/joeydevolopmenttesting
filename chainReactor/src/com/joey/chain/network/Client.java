package com.joey.chain.network;

import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;

class Client {

    public static String sendString(String request) {
    	return sendString("localhost", Server.DEFAULT_PORT, request);
    }
    public static String sendString(String server, int port, String request) {
    	return (String) send(server, port, (Object) request);
    }
    public static Object send(String server, int port, Object request) {
    	Object response;

        try {
            Socket socket = new Socket(server, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

            long sendDiff;
            long readDiff;
            long totalDiff;
            
            long startTime = System.currentTimeMillis();
            out.writeObject(request);
            out.flush();
            sendDiff = System.currentTimeMillis() - startTime;
       
            long readTime = System.currentTimeMillis();
            response = in.readObject();
            readDiff=System.currentTimeMillis()-readTime;
            
            totalDiff = System.currentTimeMillis()-startTime;
            
            
            out.close();
            in.close();
            socket.close();
        } 
        catch (Exception e) {
        	throw new RuntimeException(e);
        }        
        return response;
    }
}