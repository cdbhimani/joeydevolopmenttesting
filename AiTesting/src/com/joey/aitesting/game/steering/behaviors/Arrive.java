package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.Deceleration;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.shapes.WorldWrapper;
import com.joey.aitesting.game.steering.SteeringBehaviors;
import com.joey.aitesting.game.steering.SteeringControler;

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
