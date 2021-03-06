package com.emptyPockets.engine2D.steering.behaviors;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.maths.Transformations;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.shapes.WorldWrapper;
import com.emptyPockets.engine2D.steering.Deceleration;
import com.emptyPockets.engine2D.steering.SteeringControler;

public class OffsetPursuit extends AbstractBehavior {

	public Vehicle leader;
	public Vector2D offset;
	
	public OffsetPursuit(SteeringControler steering) {
		super("OffsetPursuit",steering);
	}

	@Override
	protected void calculate(Vector2D force) {
		offsetPursuit(vehicle, leader, offset, force);
	}

	public static void offsetPursuit(Vehicle vehicle, Vehicle leader,
			Vector2D offset, Vector2D rst) {
		// calculate the offset�s position in world space
		Vector2D WorldOffsetPos = new Vector2D();
		Vector2D leaderPos = new Vector2D();
		WorldWrapper.moveToClosest(vehicle.pos, leader.pos, leaderPos,vehicle.world.worldBounds);
		
		Transformations.PointToWorldSpace(offset, leader.velHead,leader.velSide, leaderPos, WorldOffsetPos);

		
		
		Vector2D temp = new Vector2D(WorldOffsetPos);
		//temp.subtract(vehicle.pos);
		// the look-ahead time is proportional to the distance between the
		// leader
		// and the pursuer; and is inversely proportional to the sum of both
		// agents� velocities
		float LookAheadTime = 0f;//temp.length()/ (vehicle.maxSpeed + leader.vel.length());
		// now arrive at the predicted future position of the offset

		temp.x = WorldOffsetPos.x + leader.vel.x * LookAheadTime;
		temp.y = WorldOffsetPos.y + leader.vel.y * LookAheadTime;
		Arrive.arrive(vehicle, temp, Deceleration.FAST,Arrive.DecelerationTweaker, rst);
//		Seek.seek(vehicle, temp, rst);
	}
}
