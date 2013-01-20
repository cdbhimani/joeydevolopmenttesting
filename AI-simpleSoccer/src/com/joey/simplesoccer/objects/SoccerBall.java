package com.joey.simplesoccer.objects;

import java.util.Vector;

import com.joey.aitools.entity.MovingEntity;
import com.joey.aitools.entity.Wall2D;
import com.joey.aitools.math.Vector2D;
import com.joey.aitools.messageSystem.Telegram;

public class SoccerBall extends MovingEntity {

	Vector2D oldPos;
	PlayerBase owner;
	
	SoccerPitch pitch;
	
	public SoccerBall(Vector2D pos, float ballSize, float mass, SoccerPitch pitch){
		super(pos, 
				new Vector2D(0,0),
				-1.0f, 
				new Vector2D(0,1), 
				mass, 
				new Vector2D(1,1),
				0f,
				0f);
		this.radius = ballSize;
		this.pitch=pitch;
	}
	public void testCollissionWithWalls(Vector<Wall2D> walls){
		
	}
	
	public void update(float time_elapsed){
		
	}
	
	public void render(){
		
	}
	
	@Override
	public boolean handleMessage(Telegram t) {
		// TODO Auto-generated method stub
		return super.handleMessage(t);
	}
	
	//this method applies a directional force to the ball (kicks it!)
	public void kick(Vector2D direction, float force){
		
	}
	
	
	//given a kicking force and a distance to traverse defined by start
	//and finish points, this method calculates how long it will take the
	//ball to cover the distance.
	public float timeToCoverDistance(Vector2D from,Vector2D to,float force){
		float speed = force/mass;
		float distance=to.distance(from);
		
		float term = speed*speed+2*distance*pitch.friction;
		if(term <=0){
			return -1;
		}
		return ((float)Math.sqrt(term)-speed)/pitch.friction;
	}
	//this method calculates where the ball will be at a given time
	public Vector2D FuturePosition(float time){
		//s = ut+1/2at^2
		Vector2D term2= new Vector2D(velHead);
		term2.scale(+0.5f*(-pitch.friction)*time*time);
		
		Vector2D term1 = new Vector2D(vel);
		term1.scale(time);
		
		term1.add(pos);
		term1.add(term2);
		return term1;
	}
	//this is used by players and goalkeepers to "trap" a ball -- to stop
	//it dead. The trapping player is then assumed to be in possession of
	//the ball and m_pOwner is adjusted accordingly
	public void trap(PlayerBase owner){
		vel.set(0,0); 
		this.owner = owner;
	}
	
	public Vector2D OldPos(){
		return oldPos;
	}
	
	//this places the ball at the desired location and sets its velocity to zero
	public void placeAtPosition(Vector2D NewPos){
		setPos(NewPos);
	}

}
