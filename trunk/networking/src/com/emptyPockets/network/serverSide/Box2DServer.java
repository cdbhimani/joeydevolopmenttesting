package com.emptyPockets.network.serverSide;

import java.io.IOException;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.emptyPockets.network.Network;
import com.emptyPockets.network.NetworkEntity;
import com.emptyPockets.network.transport.WorldState;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class Box2DServer extends Listener {
	World world;
	WorldState worldState;
	Server server;
	public Box2DServer(World world){
		createServer();
	}
	
	public void createServer(){
		server = new Server(){
			@Override
			protected Connection newConnection() {
				return new ClientConnection();
			}
		};
		Network.getNetwork().register(server);
	}
	
	public void start(){
		server.start();
		try {
			server.bind(Network.getNetwork().tcpPort, Network.getNetwork().udpPort);
			server.addListener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void updateWorldState(){
		worldState.bodyList.clear();
		Iterator<Body> bodies = world.getBodies();
		while(bodies.hasNext()){
			Body body = bodies.next();
			
			if(body.getUserData() instanceof NetworkEntity){
				NetworkEntity entity = (NetworkEntity) body.getUserData();
				entity.update();
				worldState.bodyList.add(entity.state);
			}
		}
	}
	
	public void sendWorldState(){
		server.sendToAllTCP(worldState);
	}
}
