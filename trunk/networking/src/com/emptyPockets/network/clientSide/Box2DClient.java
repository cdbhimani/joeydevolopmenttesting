package com.emptyPockets.network.clientSide;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.emptyPockets.gui.Scene2DToolkit;
import com.emptyPockets.network.Network;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Box2DClient extends Listener {
	World world;
	Client client;
	
	public Box2DClient(){
		world = new World(new Vector2(), true);
		createClient();
	}
	
	public void createClient(){
		client = new Client();
		client.addListener(this);
		Network.getNetwork().register(client);
	}
	
	public void start(String host){
		client.start();
		try {
			client.connect(Network.getNetwork().connectionTimeout, host, Network.getNetwork().tcpPort, Network.getNetwork().udpPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
	}
}
