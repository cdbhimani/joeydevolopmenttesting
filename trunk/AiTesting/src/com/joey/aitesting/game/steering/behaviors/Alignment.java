package com.joey.aitesting.game.steering.behaviors;

import java.util.HashSet;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

public class Alignment extends AbstractBehavior{

	public Alignment(Vehicle veh) {
		super(veh);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		// TODO Auto-generated method stub
		
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
