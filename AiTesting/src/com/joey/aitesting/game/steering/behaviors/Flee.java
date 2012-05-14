package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steering.SteeringControler;

public class Flee extends AbstractBehavior{
	public boolean useFleePanic = false;
	public float fleePanicDistance;
	public Vector2D fleePos;
	
	public Flee(SteeringControler steering) {
		super("Flee",steering);
	}

	@Override
	public void calculate(Vector2D force) {
		if(useFleePanic){
			flee(vehicle, fleePos, force);
		}
		else{
			flee(vehicle, fleePos, fleePanicDistance,force);
		}
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
