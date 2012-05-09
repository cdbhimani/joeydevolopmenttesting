package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.Deceleration;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

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
		// first we need to figure out where the two agents are going to be at
		// time T in the future. This is approximated by determining the time
		// taken to reach the midway point at the current time at max speed.
		Vector2D MidPoint = new Vector2D();		
		MidPoint.x = (AgentA.pos.x + AgentB.pos.x) / 2.0f;
		MidPoint.y = (AgentA.pos.y + AgentB.pos.y) / 2.0f;

		float TimeToReachMidPoint = m_pVehicle.pos.distance(MidPoint)
				/ m_pVehicle.maxSpeed;
		// now we have T, we assume that agent A and agent B will continue on a
		// straight trajectory and extrapolate to get their future positions

		Vector2D APos = new Vector2D();
		APos.x = AgentA.pos.x+AgentA.vel.x*TimeToReachMidPoint;
		APos.y = AgentA.pos.y+AgentA.vel.y*TimeToReachMidPoint;
		
		Vector2D BPos = new Vector2D();
		BPos.x = AgentB.pos.x+AgentB.vel.x*TimeToReachMidPoint;
		BPos.y = AgentB.pos.y+AgentB.vel.y*TimeToReachMidPoint;
		
		MidPoint.x = (APos.x + BPos.x) / 2.0f;
		MidPoint.y = (APos.y + BPos.y) / 2.0f;
		// then steer to arrive at it
		Arrive.arrive(m_pVehicle, MidPoint, Deceleration.FAST, rst);
	}
}
