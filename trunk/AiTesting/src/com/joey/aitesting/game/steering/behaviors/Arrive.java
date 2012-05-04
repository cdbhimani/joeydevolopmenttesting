package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

public class Arrive extends AbstractBehavior{
	public static final float DecelerationTweaker = .3f;
	public Arrive(Vehicle veh) {
		super(veh);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		// TODO Auto-generated method stub
		
	}

	public static void arrive(Vector2D TargetPos, Vehicle veh,	int deceleration, Vector2D rst) {
		Vector2D.subtract(TargetPos, veh.pos, rst);
		float dist = rst.length();
		if (dist > 0) {
			float speed = dist / (deceleration * DecelerationTweaker);
			speed = Math.min(speed, veh.getMaxSpeed());
			rst.scale(speed / dist);
			rst.subtract(veh.vel);
		} else {
			rst.setLocation(0, 0);
		}
	}
}
