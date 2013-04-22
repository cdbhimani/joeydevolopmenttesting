package com.emptyPockets.engine2D.steering.behaviors;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.shapes.WorldWrapper;
import com.emptyPockets.engine2D.steering.SteeringControler;

public class Persuit extends AbstractBehavior{

	public Vehicle persuit;
	
	public Persuit(SteeringControler steering) {
		super("Persuit",steering);
	}

	@Override
	public void calculate(Vector2D force) {
		persuit(vehicle, persuit, force);
	}

	public static void persuit(Vehicle vehicle, Vehicle persuit, Vector2D rst) {
		Vector2D.subtract(persuit.pos, vehicle.pos, rst);
		float RelativeHeading = vehicle.velHead.dot(persuit.velHead);
		if ((rst.dot(vehicle.velHead) > 0) && (RelativeHeading < -0.95)) { 
			Vector2D point = new Vector2D();
			WorldWrapper.moveToClosest(vehicle.pos, persuit.pos, point,
					vehicle.world.worldBounds);
			Seek.seek(vehicle,point,  rst);
			return;
		}
		float LookAheadTime = rst.len()
				/ (vehicle.maxSpeed + persuit.vel.len());
		Vector2D vec = new Vector2D(persuit.vel);
		vec.mul(LookAheadTime);
		vec.add(persuit.pos);

		Vector2D point = new Vector2D();
		WorldWrapper.moveToClosest(vehicle.pos, vec, point, vehicle.world.worldBounds);
		Seek.seek(vehicle, point, rst);
	}
}
