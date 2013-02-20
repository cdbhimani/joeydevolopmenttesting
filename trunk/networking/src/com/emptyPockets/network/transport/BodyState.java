package com.emptyPockets.network.transport;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;


public class BodyState {
	public int id;
	public float x;
	public float y;
	public float xVel;
	public float yVel;
	public float ang;
	public float angVel;
	public ArrayList<BodyState> fixtures = new ArrayList<BodyState>();
	
	public void setState(Body body){
		setState(body.getPosition(), body.getLinearVelocity(), body.getAngle(), body.getAngularVelocity());
	}
	
	public void setState(Vector2 pos, Vector2 vel, float ang, float angVel){
		this.x = pos.x;
		this.y = pos.y;
		this.xVel = vel.x;
		this.yVel = vel.y;
		this.ang = ang;
		this.angVel = angVel;	
	}
	
	public void getState(Body body){
		
	}
}
