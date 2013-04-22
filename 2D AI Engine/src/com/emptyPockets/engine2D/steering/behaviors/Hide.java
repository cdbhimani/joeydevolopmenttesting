package com.emptyPockets.engine2D.steering.behaviors;

import java.util.List;

import com.emptyPockets.engine2D.entities.types.Obstacle;
import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.steering.Deceleration;
import com.emptyPockets.engine2D.steering.SteeringControler;

public class Hide extends AbstractBehavior{
	public static final float DistanceFromBoundary = 30.0f;
	
	public Vehicle hideVehicle;
	
	public Hide(SteeringControler steering) {
		super("Hide",steering);	
	}


	@Override
	protected void calculate(Vector2D force) {
		hide(vehicle, hideVehicle, steering.getVisibleObstacles(), force);
	}
	

	public static void GetHidingPosition(Vector2D target, Obstacle obj,Vector2D rst) {
		// calculate how far away the agent is to be from the chosen obstacle’s
		// bounding radius

		float DistAway = obj.radius + DistanceFromBoundary;
		// calculate the heading toward the object from the target
		Vector2D ToOb = new Vector2D(obj.pos);
		ToOb.sub(target);
		ToOb.nor();

		// scale it to size and add to the obstacle's position to get
		// the hiding spot.
		rst.x = (ToOb.x * DistAway) + obj.pos.x;
		rst.y = (ToOb.y * DistAway) + obj.pos.y;
	}

	public static void hide(Vehicle vehicle, Vehicle target,List<Obstacle> obstacles, Vector2D rst) {
		float DistToClosest = Float.MAX_VALUE;
		boolean spotFound = false;

		Vector2D BestHidingSpot = new Vector2D();
		Vector2D currentSpot = new Vector2D();
		for (Obstacle curOb : obstacles) {
			// calculate the position of the hiding spot for this obstacle
			GetHidingPosition(target.pos, curOb, currentSpot);
			// work in distance-squared space to find the closest hiding
			// spot to the agent
			float dist = currentSpot.dst2(vehicle.pos);
			if (dist < DistToClosest) {
				DistToClosest = dist;
				BestHidingSpot.set(currentSpot);
				spotFound = true;
			}

		}// end while
			// if no suitable obstacles found then evade the target
		
		if (!spotFound) {
			System.out.println("Evading");
			Evade.evade(vehicle, target, rst);
		}
		else{		// else use Arrive on the hiding spot
			System.out.println("Arriving");
			Arrive.arrive(vehicle, BestHidingSpot, Deceleration.FAST, rst);
		}
	}

}
