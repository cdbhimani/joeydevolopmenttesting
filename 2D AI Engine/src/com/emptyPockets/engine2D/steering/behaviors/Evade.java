package com.emptyPockets.engine2D.steering.behaviors;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.shapes.WorldWrapper;
import com.emptyPockets.engine2D.steering.SteeringControler;

public class Evade extends AbstractBehavior{

	public Vehicle evadeVehicle;
	public boolean useEvadePanic = false;
	public float evadePanicDistance = 30;
	
	public Evade(SteeringControler steering) {
		super("Evade",steering);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		if(useEvadePanic){
			evade(vehicle, evadeVehicle,evadePanicDistance,force);
		}else{
			evade(vehicle, evadeVehicle, force);
		}
		
	}

public static void evade(Vehicle vehicle, Vehicle evade,float panicDistance, Vector2D rst) {
		
		Vector2D.subtract(evade.pos ,vehicle.pos, rst);
		if (rst.len2() > panicDistance * panicDistance) {
			rst.x = 0;
			rst.y = 0;
			return;
		}
		
		Vector2D holder = new Vector2D();
		
		float LookAheadTime = holder.len()/(vehicle.maxSpeed + evade.vel.len());
		
		holder.set(evade.vel);
		holder.mul(LookAheadTime);
		holder.add(evade.pos);
		
		Vector2D point = new Vector2D();
		WorldWrapper.moveToClosest(vehicle.pos, holder, point, vehicle.world.worldBounds);
		Flee.flee(vehicle, point, rst);
	}
	
	public static void evade(Vehicle vehicle, Vehicle evade, Vector2D rst) {
		Vector2D holder = new Vector2D();
		Vector2D.subtract(evade.pos ,vehicle.pos, rst);
		float LookAheadTime = holder.len()/(vehicle.maxSpeed + evade.vel.len());
		
		holder.set(evade.vel);
		holder.mul(LookAheadTime);
		holder.add(evade.pos);
		
		Vector2D point = new Vector2D();
		WorldWrapper.moveToClosest(vehicle.pos, holder, point, vehicle.world.worldBounds);
		Flee.flee(vehicle,point, rst);
	}
}
