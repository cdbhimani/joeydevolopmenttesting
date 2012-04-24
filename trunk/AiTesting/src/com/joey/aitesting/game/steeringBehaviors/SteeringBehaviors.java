package com.joey.aitesting.game.steeringBehaviors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

public class SteeringBehaviors {
	public static final float DecelerationTweaker = 0.3f;
	Vehicle vehicle;

	boolean useSeek = false;
	Vector2D seekPos;

	public boolean useAttract = false;
	public Vector2D attract;

	public boolean useRepel = false;
	public Vector2D repel;

	public SteeringBehaviors(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public boolean isSpacePartitioningOn() {
		return false;
	}

	public static void moveToClosest(Vehicle veh, Vector2D pos, Vector2D rst,
			float sizeX, float sizeY) {

		// Prevents reallocation of memory
		Vector2D.subtract(veh.pos, pos, rst);

		if (Math.abs(rst.x) > sizeX / 2) {
			if (rst.x > 0) {
				rst.x = pos.x + sizeX;
			} else {
				rst.x = pos.x - sizeX;
			}
		} else {
			rst.x = pos.x;
		}

		if (Math.abs(rst.y) > sizeY / 2) {
			if (rst.y > 0) {
				rst.y = pos.y + sizeY;
			} else {
				rst.y = pos.y - sizeY;
			}
		} else {
			rst.y = pos.y;
		}

	}

	public Vector2D calculate() {
		Vector2D rst = new Vector2D();
		Vector2D tmp = new Vector2D();
		Vector2D point = new Vector2D();
		
		HashSet<Vehicle> enty = new HashSet<Vehicle>();
		vehicle.world.getCellSpacePartition().getNearEntities(vehicle,30, enty);

		if (useAttract) {
			moveToClosest(vehicle, attract, point, vehicle.world.getWideX(),
					vehicle.world.getWideY());
			seek(point, vehicle, tmp);
		}
		rst.add(tmp);

		if (useRepel) {
			
				moveToClosest(vehicle, repel, point, vehicle.world.getWideX(),
						vehicle.world.getWideY());
				flee(point, vehicle, tmp);
			
			rst.add(tmp);
		}

		if(rst.lengthSq() > vehicle.maxSpeed*vehicle.maxSpeed){
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
