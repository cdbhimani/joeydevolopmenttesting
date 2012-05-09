package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.Deceleration;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.shapes.WorldWrapper;

public class Interpose extends AbstractBehavior {

	public Interpose(Vehicle veh) {
		super(veh);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		// TODO Auto-generated method stub

	}

	public static void interpose(Vehicle m_pVehicle, Vehicle AgentA, Vehicle AgentB,
			Vector2D rst) {
		Vector2D posA = new Vector2D();
		Vector2D posB = new Vector2D();
		

		WorldWrapper.moveToClosest(m_pVehicle.pos, AgentA.pos, posA,m_pVehicle.world.worldBounds);
		WorldWrapper.moveToClosest(m_pVehicle.pos, AgentB.pos, posB,m_pVehicle.world.worldBounds);
		
		// first we need to figure out where the two agents are going to be at
		// time T in the future. This is approximated by determining the time
		// taken to reach the midway point at the current time at max speed.
		Vector2D MidPoint = new Vector2D();		
		MidPoint.x = (posA.x + posB.x) / 2.0f;
		MidPoint.y = (posA.y + posB.y) / 2.0f;

		float TimeToReachMidPoint = m_pVehicle.pos.distance(MidPoint)
				/ m_pVehicle.maxSpeed;
		// now we have T, we assume that agent A and agent B will continue on a
		// straight trajectory and extrapolate to get their future positions

		Vector2D APos = new Vector2D();
		APos.x = posA.x+AgentA.vel.x*TimeToReachMidPoint;
		APos.y = posA.y+AgentA.vel.y*TimeToReachMidPoint;
		
		Vector2D BPos = new Vector2D();
		BPos.x = posB.x+AgentB.vel.x*TimeToReachMidPoint;
		BPos.y = posB.y+AgentB.vel.y*TimeToReachMidPoint;
		
		MidPoint.x = (APos.x + BPos.x) / 2.0f;
		MidPoint.y = (APos.y + BPos.y) / 2.0f;
		// then steer to arrive at it
		Arrive.arrive(m_pVehicle, MidPoint, Deceleration.FAST,Arrive.DecelerationTweaker, rst);
	}
}
