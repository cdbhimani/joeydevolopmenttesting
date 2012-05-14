package com.joey.aitesting.game.steering.behaviors;

import java.util.HashSet;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.shapes.WorldWrapper;
import com.joey.aitesting.game.steering.SteeringControler;

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
				
				length = hold.length();
				hold.normalise();
				
				if(length > 0){
					length = 0.01f;
				}
				rst.x+= hold.x/length;
				rst.y+= hold.y/length;
			}
		}
	}

}
