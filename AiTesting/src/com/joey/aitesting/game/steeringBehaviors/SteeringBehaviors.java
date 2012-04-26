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
	
	public Vector2D seekPos;
	public Vector2D arrivePos;
	public Vector2D fleePos;
	
	public float fleePanicDistance;

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

	public Vector2D calculate() {
		Vector2D rst = new Vector2D();
		Vector2D point = new Vector2D();

		if (useSeek) {
			moveToClosest(vehicle.pos, seekPos, point,vehicle.world.worldBounds);
			seek(point, vehicle, rst);
		}

		if (useFlee) {
			moveToClosest(vehicle.pos, fleePos, point, vehicle.world.worldBounds);
			flee(point, vehicle, rst);
		}

		if (useFleePanic) {
			moveToClosest(vehicle.pos, fleePos, point, vehicle.world.worldBounds);
			flee(point, vehicle,fleePanicDistance, rst);
		}
		
		if (useArrive) {
			moveToClosest(vehicle.pos, arrivePos, point, vehicle.world.worldBounds);
			arrive(point, vehicle,1, rst);
		}
		
		if (rst.lengthSq() > vehicle.maxSpeed * vehicle.maxSpeed) {
			rst.normalise();
			rst.scale(vehicle.maxSpeed);
		}
		return rst;
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
	
	public static void flee(Vector2D targetPos, Vehicle veh, float fleeDistance, Vector2D rst) {
		Vector2D.subtract(veh.pos, targetPos, rst);
		if(rst.lengthSq() > fleeDistance*fleeDistance){
			rst.x = 0;
			rst.y = 0;
			return; 
		}
		rst.normalise();
		rst.scale(veh.maxSpeed);

		rst.subtract(veh.vel);
	}

	public static void arrive(Vector2D TargetPos, Vehicle veh,
			int deceleration, Vector2D rst) {
		Vector2D.subtract(TargetPos, veh.pos, rst);

		// calculate the distance to the target position
		float dist = rst.length();
		if (dist > 0) {
			// because Deceleration is enumerated as an int, this value is
			// required
			// to provide fine tweaking of the deceleration.

			// calculate the speed required to reach the target given the
			// desired
			// deceleration
			float speed = dist / (deceleration * DecelerationTweaker);
			// make sure the velocity does not exceed the max
			speed = Math.min(speed, veh.getMaxSpeed());
			// from here proceed just like Seek except we don't need to
			// normalize
			// the ToTarget vector because we have already gone to the trouble
			// of calculating its length: dist.
			rst.scale(speed / dist);
			// return (DesiredVelocity - m_pVehicle.Velocity());
			rst.subtract(veh.vel);
		} else {
			rst.setLocation(0, 0);
		}
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
