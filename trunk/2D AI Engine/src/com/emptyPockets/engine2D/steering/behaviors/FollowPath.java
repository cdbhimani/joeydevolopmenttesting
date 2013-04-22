package com.emptyPockets.engine2D.steering.behaviors;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.entities.types.WaypointPath;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.steering.Deceleration;
import com.emptyPockets.engine2D.steering.SteeringBehaviors;
import com.emptyPockets.engine2D.steering.SteeringControler;

public class FollowPath extends AbstractBehavior {

	public float waypointDistance = 10;
	public WaypointPath path;
	
	public FollowPath(SteeringControler steering) {
		super("FollowPath",steering);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		PathFollow(vehicle, path, waypointDistance, force);
		
	}

	public static void PathFollow(Vehicle vehicle, WaypointPath path,float waypointDistance, Vector2D rst){
		
		//move to next target if close enough to current target (working in
		  //distance squared space)
		  if(path.getCurrentNode().dst(vehicle.pos)< waypointDistance){
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
