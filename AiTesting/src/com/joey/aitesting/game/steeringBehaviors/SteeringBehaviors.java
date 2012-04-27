package com.joey.aitesting.game.steeringBehaviors;

import java.util.ArrayList;
import java.util.HashSet;
import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.shapes.Rectangle2D;

public class SteeringBehaviors {
	public static final float DecelerationTweaker = .3f;
	Vehicle vehicle;

	public boolean useFlee = false;
	public boolean useFleePanic = false;
	public boolean useSeek = false;
	public boolean useArrive = false;
	public boolean usePersuit = false;
	public boolean useEvade = false;
	public boolean useWander = false;
	public boolean useCohesion = false;
	public boolean useSeperation = false;
	public boolean useAlignment = false;
	
	
	
	public Vector2D seekPos;
	public Vector2D arrivePos;
	public Vector2D fleePos;
	public Vehicle persuitVehicle;
	public Vehicle evadeVehicle;
	
	public float fleePanicDistance;
	public float wanderRadius;
	public float wanderDistance;
	public float wanderJitter;
	public Vector2D wanderVector;
	
	public float neighborRadius = 100;
	
	ArrayList<Vehicle> neighbors = new ArrayList<Vehicle>();
	
	public SteeringBehaviors(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public boolean isSpacePartitioningOn() {
		return false;
	}

	public static void moveToClosest(Vector2D p1, Vector2D p2, Vector2D rst,
			Rectangle2D rec) {

		// Prevents reallocation of memory
		Vector2D.subtract(p1, p2, rst);

		if (Math.abs(rst.x) > rec.getWidth() / 2) {
			if (rst.x > 0) {
				rst.x = p2.x + rec.getWidth();
			} else {
				rst.x = p2.x - rec.getWidth();
			}
		} else {
			rst.x = p2.x;
		}

		if (Math.abs(rst.y) > rec.getHeight() / 2) {
			if (rst.y > 0) {
				rst.y = p2.y + rec.getHeight();
			} else {
				rst.y = p2.y - rec.getHeight();
			}
		} else {
			rst.y = p2.y;
		}

	}

	public void calculateNeighbobors(ArrayList<Vehicle> neighbors, Rectangle2D reg){
		vehicle.world.quadTree.getPointsInRegion(reg, neighbors);
	}
	
	public Vector2D calculate(float updateTime) {
		Vector2D 
		Vector2D hold = new Vector2D();
		Vector2D point = new Vector2D();

		if(useCohesion||useAlignment||useSeperation){
			neighbors.clear();
			Rectangle2D reg = new Rectangle2D(
					vehicle.pos.x-neighborRadius, vehicle.pos.y-neighborRadius, 
					vehicle.pos.x+neighborRadius, vehicle.pos.y+neighborRadius);
			calculateNeighbobors(neighbors, reg);
			//Remove self
			neighbors.remove(vehicle);
		}
		if (useSeek) {
			moveToClosest(vehicle.pos, seekPos, point,
					vehicle.world.worldBounds);
			seek(point, vehicle, hold);
		}

		if (useFlee) {
			moveToClosest(vehicle.pos, fleePos, point,
					vehicle.world.worldBounds);
			flee(point, vehicle, hold);
		}

		if (useFleePanic) {
			moveToClosest(vehicle.pos, fleePos, point,
					vehicle.world.worldBounds);
			flee(point, vehicle, fleePanicDistance, hold);
		}

		if (useArrive) {
			moveToClosest(vehicle.pos, arrivePos, point,
					vehicle.world.worldBounds);
			arrive(point, vehicle, 1, hold);
		}

		if (usePersuit) {
			persuit(vehicle, persuitVehicle, hold);
		}
		
		if (useEvade) {
			evade(vehicle, evadeVehicle, hold);
		}
		
		if(useWander){
			if(wanderVector == null){
				wanderVector = new Vector2D(vehicle.vel);
				wanderVector.normalise();
			}
			wander(vehicle,updateTime, wanderJitter, wanderRadius, wanderDistance, wanderVector, hold);
		}

		if(useSeperation){
			seperation(vehicle, neighbors, hold);
			System.out.println(hold);
		}
		if (hold.lengthSq() > vehicle.maxSpeed * vehicle.maxSpeed) {
			hold.normalise();
			hold.scale(vehicle.maxSpeed);
		}
		return hold;
	}

	public static void seperation(Vehicle vehicle, ArrayList<Vehicle> neighbors, Vector2D rst){
		Vector2D hold = new Vector2D();
		float length = 0;
		
		rst.x = 0;
		rst.y = 0;
		System.out.println(neighbors.size());
		for(Vehicle other : neighbors){
			Vector2D.subtract(vehicle.pos,other.pos, hold);
			length = hold.length();
			hold.normalise();
			
			rst.x+= hold.x/length;
			rst.y+= hold.y/length;
		}
	}
	public static void seek(Vector2D targetPos, Vehicle veh, Vector2D rst) {
		Vector2D.subtract(targetPos, veh.pos, rst);
		rst.normalise();
		rst.scale(veh.maxSpeed);

		rst.subtract(veh.vel);
	}

	public static void flee(Vector2D targetPos, Vehicle veh, Vector2D rst) {
		Vector2D.subtract(veh.pos, targetPos, rst);
		rst.normalise();
		rst.scale(veh.maxSpeed);

		rst.subtract(veh.vel);
	}

	public static void flee(Vector2D targetPos, Vehicle veh,
			float fleeDistance, Vector2D rst) {
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

	public static void persuit(Vehicle vehicle, Vehicle persuit, Vector2D rst) {
		Vector2D.subtract(persuit.pos, vehicle.pos, rst);
		float RelativeHeading = vehicle.velHead.dot(persuit.velHead);
		if ((rst.dot(vehicle.velHead) > 0) && (RelativeHeading < -0.95)) { 
			Vector2D point = new Vector2D();
			moveToClosest(vehicle.pos, persuit.pos, point,
					vehicle.world.worldBounds);
			seek(point, vehicle, rst);
			return;
		}
		float LookAheadTime = rst.length()
				/ (vehicle.maxSpeed + persuit.vel.length());
		Vector2D vec = new Vector2D(persuit.vel);
		vec.scale(LookAheadTime);
		vec.add(persuit.pos);

		Vector2D point = new Vector2D();
		moveToClosest(vehicle.pos, vec, point, vehicle.world.worldBounds);
		seek(point, vehicle, rst);
	}

	public static void evade(Vehicle vehicle, Vehicle evade, Vector2D rst) {
		Vector2D holder = new Vector2D();
		Vector2D.subtract(evade.pos ,vehicle.pos, rst);
		float LookAheadTime = holder.length()/(vehicle.maxSpeed + evade.vel.length());
		
		holder.setLocation(evade.vel);
		holder.scale(LookAheadTime);
		holder.add(evade.pos);
		
		Vector2D point = new Vector2D();
		moveToClosest(vehicle.pos, holder, point, vehicle.world.worldBounds);
		flee(point, vehicle, rst);
	}
	public static float turnaroundTime(Vehicle pAgent, Vector2D TargetPos, float coefficient) {
		Vector2D toTarget = new Vector2D(TargetPos);
		toTarget.subtract(pAgent.pos);
		float dot = pAgent.velHead.dot(toTarget);
		return (dot - 1.0f) * -coefficient;
	}
	
	public static void wander(Vehicle vehicle,float updateTime, float wanderJitter, 
			float wanderRadius,float wanderDistance, Vector2D wanderVector, 
			Vector2D rst){
		//this behavior is dependent on the update rate, so this line must
		  //be included when using time independent framerate.
		  double JitterThisTimeSlice = wanderJitter;

		  //first, add a small random vector to the target's position
		  wanderVector.x += (1-2*Math.random()) * JitterThisTimeSlice;
		  wanderVector.y += (1-2*Math.random()) * JitterThisTimeSlice;

		  //reproject this new vec2tor back on to a unit circle
		  wanderVector.normalise();
		  wanderVector.scale(wanderRadius);

		  //move the target into a position WanderDist in front of the agent
		  Vector2D wanderPos = new Vector2D(vehicle.vel);
		  wanderPos.normalise();
		  wanderPos.scale(wanderDistance);
		  wanderPos.add(vehicle.pos);
		  wanderPos.add(wanderVector);
		  
		  seek(wanderPos, vehicle, rst);
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public boolean isUseSeek() {
		return useSeek;
	}

	public void setUseSeek(boolean useSeek) {
		this.useSeek = useSeek;
	}

	public Vector2D getSeekPos() {
		return seekPos;
	}

	public void setSeekPos(Vector2D seekPos) {
		this.seekPos = seekPos;
	}
}
