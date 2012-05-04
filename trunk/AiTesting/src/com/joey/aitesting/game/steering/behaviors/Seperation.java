package com.joey.aitesting.game.steering.behaviors;

import java.util.HashSet;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

public class Seperation extends AbstractBehavior{

	public Seperation(Vehicle veh) {
		super(veh);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		// TODO Auto-generated method stub
		
	}

	public static void seperation(Vehicle vehicle, HashSet<Vehicle> neighbors, Vector2D rst){
		Vector2D hold = new Vector2D();
		float length = 0;
		
		rst.x = 0;
		rst.y = 0;
		
		for(Vehicle other : neighbors){
			Vector2D.subtract(vehicle.pos,other.pos, hold);
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
