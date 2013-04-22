package com.emptyPockets.engine2D.steering.behaviors;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.shapes.WorldWrapper;
import com.emptyPockets.engine2D.steering.Deceleration;
import com.emptyPockets.engine2D.steering.SteeringBehaviors;
import com.emptyPockets.engine2D.steering.SteeringControler;

public class Arrive extends AbstractBehavior{
	public static final float DecelerationTweaker = .2f;
	
	public Vector2D arrivePos;
	public Vector2D arrivePosWrapped = new Vector2D();
	public int deceleration = Deceleration.FAST;
	public float decelerationTweaker = DecelerationTweaker;
	
	public Arrive(SteeringControler steering) {
		super("Arrive",steering);
	}

	@Override
	public void calculate(Vector2D force) {
		WorldWrapper.moveToClosest(vehicle.pos, arrivePos, arrivePosWrapped,vehicle.world.worldBounds);
		arrive(vehicle, arrivePosWrapped, deceleration,decelerationTweaker, force);
	}

	public static void arrive(Vehicle veh,	Vector2D TargetPos, int deceleration, Vector2D rst) {
		arrive(veh, TargetPos, Deceleration.FAST, deceleration, rst);
	}
	public static void arrive(Vehicle veh,	Vector2D TargetPos, int deceleration, float DecelerationTweaker, Vector2D rst) {
		Vector2D.subtract(TargetPos, veh.pos, rst);
		float dist = rst.len();
		if (dist > 0) {
			float speed = dist / (deceleration * DecelerationTweaker);
			speed = Math.min(speed, veh.getMaxSpeed());
			rst.mul(speed / dist);
			rst.sub(veh.vel);
		} else {
			rst.set(0, 0);
		}
	}
}
