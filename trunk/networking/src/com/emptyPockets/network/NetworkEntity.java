package com.emptyPockets.network;

import com.badlogic.gdx.physics.box2d.Body;
import com.emptyPockets.network.transport.BodyState;

public class NetworkEntity {
	static int count = 0;
	public BodyState state;
	public Body body;
	public int id = count++;
	
	public NetworkEntity(){
	}
	
	public void update(){
		state.setState(body);
	}
}
