package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.Deceleration;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.entities.WaypointPath;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steering.SteeringBehaviors;

public class FollowPath extends AbstractBehavior {

	public FollowPath(Vehicle vehicle) {
		super(vehicle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		// TODO Auto-generated method stub
		
	}

	public static void PathFollow(Vehicle vehicle, WaypointPath path,float waypointDistance, Vector2D rst){
		
		//move to next target if close enough to current target (working in
		  //distance squared space)
		  if(path.getCurrentNode().distance(vehicle.pos)< waypointDistance){
			  path.next();
		  }

		  if (path.hasNext()){
		    Seek.seek(vehicle, path.getCurrentNode(), rst);
		  }
		  else{
		    Arrive.arrive(vehicle, path.getCurrentNode(),Deceleration.FAST,Arrive.DecelerationTweaker, rst);
		  }
	}
}
