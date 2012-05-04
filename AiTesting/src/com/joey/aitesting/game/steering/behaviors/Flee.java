package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

public class Flee extends AbstractBehavior{

	public Flee(Vehicle veh) {
		super(veh);
	}

	@Override
	public void calculate(Vector2D force) {
		
	}

	public static void flee(Vehicle veh,Vector2D targetPos,Vector2D rst) {
		Vector2D.subtract(veh.pos, targetPos, rst);
		rst.normalise();
		rst.scale(veh.maxSpeed);
		rst.subtract(veh.vel);
	}

	public static void flee(Vehicle veh,Vector2D targetPos,float fleeDistance, Vector2D rst) {
		Vector2D.subtract(veh.pos, targetPos, rst);
		if (rst.lengthSq() > fleeDistance * fleeDistance) {
			rst.x = 0;
			rst.y = 0;
			return;
		}
		rst.normalise();
		rst.scale(veh.maxSpeed);
		rst.subtract(veh.vel);
	}
}
