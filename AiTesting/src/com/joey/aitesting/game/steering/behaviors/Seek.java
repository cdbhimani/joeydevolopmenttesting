package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

public class Seek extends AbstractBehavior{

	public Seek(Vehicle veh) {
		super(veh);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		// TODO Auto-generated method stub
		
	}

	public static void seek(Vehicle veh,Vector2D targetPos,Vector2D rst) {
		Vector2D.subtract(targetPos, veh.pos, rst);
		rst.normalise();
		rst.scale(veh.maxSpeed);

		rst.subtract(veh.vel);
	}
}
