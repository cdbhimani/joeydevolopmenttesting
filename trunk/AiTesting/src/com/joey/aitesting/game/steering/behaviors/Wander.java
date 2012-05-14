package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steering.SteeringControler;

public class Wander extends AbstractBehavior{

	public float wanderRadius;
	public float wanderDistance;
	public float wanderJitter;
	public Vector2D wanderVector;
	
	public Wander(SteeringControler steering) {
		super("Wander",steering);
		wanderVector = new Vector2D();
	}

	@Override
	public void calculate(Vector2D force) {
		wander(vehicle, 1, wanderJitter, wanderRadius, wanderDistance, wanderVector, force);
	}

	public static void wander(Vehicle vehicle,float updateTime, float wanderJitter, 
			float wanderRadius,float wanderDistance, Vector2D wanderVector, 
			Vector2D rst){
		
		
		
//		  //this behavior is dependent on the update rate, so this line must
//		  //be included when using time independent framerate.
//		  double JitterThisTimeSlice = wanderJitter * updateTime*10;
//
//		  //first, add a small random vector to the target's position
//		//first, add a small random vector to the target's position
//		  wanderVector.x += (1-2*Math.random()) * JitterThisTimeSlice;
//		  wanderVector.y += (1-2*Math.random()) * JitterThisTimeSlice;
//
//		  //reproject this new vector back on to a unit circle
//		  wanderVector.normalise();
//
//		  //increase the length of the vector to the same as the radius
//		  //of the wander circle
//		  wanderVector.scale(wanderRadius);
//
//		  //move the target into a position WanderDist in front of the agent
//		  Vector2D target = new Vector2D(wanderVector);
//		  target.x +=wanderDistance;
//
//		  //project the target into world space
//		  Transformations.PointToWorldSpace(target,
//		                                       vehicle.velHead,
//		                                       vehicle.velSide, 
//		                                       vehicle.pos, rst);
//
//		  //and steer towards it
//		  rst.subtract(vehicle.pos);
		
		
		
		  //this behavior is dependent on the update rate, so this line must
		  //be included when using time independent framerate.
		  double JitterThisTimeSlice = wanderJitter;

		  //first, add a small random vector to the target's position
		  wanderVector.x += (1-2*Math.random()) * JitterThisTimeSlice;
		  wanderVector.y += (1-2*Math.random()) * JitterThisTimeSlice;

		  //reproject this new vec2tor back on to a unit circle
		  wanderVector.normalise();
		  wanderVector.scale(wanderRadius);

		  //move the target into a position WanderDist in front of the agent
		  Vector2D wanderPos = new Vector2D(vehicle.vel);
		  wanderPos.normalise();
		  wanderPos.scale(wanderDistance);
		  wanderPos.add(vehicle.pos);
		  wanderPos.add(wanderVector);
		  
		  Seek.seek(vehicle, wanderPos, rst);
	}
}
