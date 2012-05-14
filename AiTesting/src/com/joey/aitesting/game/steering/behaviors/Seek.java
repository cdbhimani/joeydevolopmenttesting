package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steering.SteeringControler;

public class Seek extends AbstractBehavior{

	Vector2D target;
	
	public Seek(SteeringControler steering) {
		super("Seek",steering);
	}

	@Override
	public void calculate(Vector2D force) {
		seek(vehicle, target, force);
	}

	public static void seek(Vehicle veh,Vector2D targetPos,Vector2D rst) {
		Vector2D.subtract(targetPos, veh.pos, rst);
		rst.normalise();
		rst.scale(veh.maxSpeed);

		rst.subtract(veh.vel);
	}
}
