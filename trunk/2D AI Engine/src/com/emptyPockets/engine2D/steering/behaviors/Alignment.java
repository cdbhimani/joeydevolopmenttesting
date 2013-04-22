package com.emptyPockets.engine2D.steering.behaviors;

import java.util.HashSet;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.steering.SteeringControler;

public class Alignment extends AbstractBehavior{

	public Alignment(SteeringControler steering) {
		super("Alignment",steering);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		alignment(vehicle, vehicle.steering.getVisibleVehicles(), force);
	}

	public static void alignment(Vehicle vehicle, HashSet<Vehicle> neighbors, Vector2D rst){
		float count = 0;
		
		rst.x = 0;
		rst.y = 0;
		
		for(Vehicle other : neighbors){
			rst.add(other.vel);
			
			count++;
		}
		
		if(count >0){
			rst.mul(1/count);
			rst.sub(vehicle.velHead);
		}
	}
	

}
