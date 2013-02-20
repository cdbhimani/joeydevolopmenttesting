package com.emptyPockets.network;

import com.badlogic.gdx.physics.box2d.Body;
import com.emptyPockets.network.transport.BodyState;

public abstract class NetworkEntity {
	static int count = 0;
	public BodyState state;
	public int id = count++;
	
	public NetworkEntity(){
		state = new BodyState();
	}
	
	public void update(){
		state.setState(getBody());
	}
	
	public abstract Body getBody();
}
