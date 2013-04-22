package com.emptyPockets.engine2D.steering.behaviors;

import java.util.HashSet;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.shapes.WorldWrapper;
import com.emptyPockets.engine2D.steering.SteeringControler;

public class Seperation extends AbstractBehavior{

	public Seperation(SteeringControler steering) {
		super("Seperation",steering);
	}

	@Override
	public void calculate(Vector2D force) {
		seperation(vehicle, steering.getVisibleVehicles(), force);
	}

	public static void seperation(Vehicle vehicle, HashSet<Vehicle> neighbors, Vector2D rst){
		Vector2D hold = new Vector2D();
		
		float length = 0;
		
		rst.x = 0;
		rst.y = 0;
		
		for(Vehicle other : neighbors){
			if(other != vehicle){
				WorldWrapper.moveToClosest(vehicle.pos, other.pos, hold,vehicle.world.worldBounds);
				hold.x = vehicle.pos.x-hold.x;
				hold.y = vehicle.pos.y-hold.y;
				
				length = hold.len();
				hold.nor();
				
				if(length > 0){
					length = 0.01f;
				}
				rst.x+= hold.x/length;
				rst.y+= hold.y/length;
			}
		}
	}

}
