package com.emptyPockets.engine2D.steering.behaviors;

import java.util.HashSet;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.shapes.WorldWrapper;
import com.emptyPockets.engine2D.steering.SteeringControler;

public class Cohesion extends AbstractBehavior{

	public Cohesion(SteeringControler steering) {
		super("Cohesion",steering);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		cohesion(vehicle, steering.getVisibleVehicles(), force);
	}

	public static void cohesion(Vehicle vehicle, HashSet<Vehicle> neighbors, Vector2D rst){
		//first find the center of mass of all the agents
		Vector2D hold = new Vector2D();
		int NeighborCount = 0;
		//iterate through the neighbors and sum up all the position vectors
		for(Vehicle other : neighbors){
			if(other != vehicle){
				
				WorldWrapper.moveToClosest(vehicle.pos, other.pos, rst,vehicle.world.worldBounds);
				
				hold.add(rst);
				NeighborCount++;
			}
		}
		
		if (NeighborCount > 0)
		{
			//the center of mass is the average of the sum of positions
			hold.mul(1f/NeighborCount);			
			Seek.seek(vehicle, hold, rst);
		}
	}
}
