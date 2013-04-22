package com.emptyPockets.engine2D.steering.behaviors;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.steering.SteeringControler;

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
		rst.nor();
		rst.mul(veh.maxSpeed);
		rst.sub(veh.vel);
	}
}
