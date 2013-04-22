package com.emptyPockets.engine2D.steering.behaviors;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.steering.SteeringControler;

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
		rst.nor();
		rst.mul(veh.maxSpeed);
		rst.sub(veh.vel);
	}

	public static void flee(Vehicle veh,Vector2D targetPos,float fleeDistance, Vector2D rst) {
		Vector2D.subtract(veh.pos, targetPos, rst);
		if (rst.len2() > fleeDistance * fleeDistance) {
			rst.x = 0;
			rst.y = 0;
			return;
		}
		rst.nor();
		rst.mul(veh.maxSpeed);
		rst.sub(veh.vel);
	}
}
