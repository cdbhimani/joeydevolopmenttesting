package com.joey.chain;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServerTest {

	public static void main(String input[]) throws IOException{
		Server server = new Server();
		server.bind(54555, 54777);
		server.start();
		server.getKryo().register(SomeRequest.class);
		
		server.addListener(new Listener(){
			@Override
			public void received(Connection connection, Object object) {
				// TODO Auto-generated method stub
				super.received(connection, object);
				System.out.println("Recieved : "+object);
			}
		});
		
		Client client = new Client();
		client.start();
		client.getKryo().register(SomeRequest.class);
		client.connect(5000, "localhost", 54555, 54777);

		SomeRequest request = new SomeRequest();
		request.text = "Here is the request!";
		client.sendTCP(request);
	}
}

class SomeRequest{
	public String text = "";
}