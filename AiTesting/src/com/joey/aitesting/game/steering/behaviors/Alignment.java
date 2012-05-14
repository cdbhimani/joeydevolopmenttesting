package com.joey.aitesting.game.steering.behaviors;

import java.util.HashSet;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steering.SteeringControler;

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
			rst.scale(1/count);
			rst.subtract(vehicle.velHead);
		}
	}
	

}
