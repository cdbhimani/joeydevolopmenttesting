package com.emptyPockets.engine2D.entities.types;

import java.util.ArrayList;

import com.emptyPockets.engine2D.GameWorld;
import com.emptyPockets.engine2D.HeadingSmoother;
import com.emptyPockets.engine2D.maths.Transformations;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.steering.SteeringBehaviors;
import com.emptyPockets.engine2D.steering.SteeringControler;

public class Vehicle extends MovingEntity {
	public GameWorld world;
	//public SteeringBehaviors steeringOld;
	public SteeringControler steering;
	public Vector2D smoothedHeading;
	public HeadingSmoother headingSmoother;
	public boolean smoothingOn;
	public float timeElapsed;

	ArrayList<Vector2D> localVehicleShape = new ArrayList<Vector2D>();
	public ArrayList<Vector2D> transformedVehicleShape = new ArrayList<Vector2D>();
	public Vehicle(GameWorld world) {
		this(world, new Vector2D(), 1, new Vector2D(), 1, 10, 10, 10);
	}

	public Vehicle(GameWorld world, Vector2D position, float rotation,
			Vector2D velocity, float mass, float max_force, float max_speed,
			float max_turn_rate) {

		super(position, velocity, max_speed, new Vector2D(
				(float) Math.sin(rotation), -(float) Math.cos(rotation)), mass,
				new Vector2D(1, 1), max_turn_rate, max_force);

		this.world = world;
		this.smoothedHeading = new Vector2D(0, 0);
		this.smoothingOn = false;
		this.timeElapsed = 0;
		//this.steeringOld = new SteeringBehaviors(this);
		this.steering = new SteeringControler(this);
		setupShape(5f);
	}

	public void setupShape(float scale) {
		radius = scale;
		localVehicleShape.add(new Vector2D(-1.0f*scale, 0.6f*scale));
		localVehicleShape.add(new Vector2D(1.0f*scale, 0.0f*scale));
		localVehicleShape.add(new Vector2D(-1.0f*scale, -0.6f*scale));
		
		transformedVehicleShape.add(new Vector2D(-1.0f*scale, 0.6f*scale));
		transformedVehicleShape.add(new Vector2D(1.0f*scale, 0.0f*scale));
		transformedVehicleShape.add(new Vector2D(-1.0f*scale, -0.6f*scale));
	}

	@Override
	public void update(float time_elapsed) {
		// update the time elapsed
		this.timeElapsed = time_elapsed;

		Vector2D acceleration = new Vector2D();
		// calculate the combined force from each steering behavior in the
		// vehicle's list
//		steeringForce = steeringOld.calculate(time_elapsed);
		
		
		steering.calculate(acceleration);
		// Acceleration = Force/Mass
		acceleration.x = acceleration.x / mass;
		acceleration.y = acceleration.y / mass;

		// update velocity
		vel.x += acceleration.x * time_elapsed;
		vel.y += acceleration.y * time_elapsed;

		// make sure vehicle does not exceed maximum velocity
		vel.truncate(maxSpeed);

//		 update the position
		pos.x += vel.x * time_elapsed;
		pos.y += vel.y * time_elapsed;
		
//		System.out.println("Time : "+time_elapsed);
//		System.out.println("Pos : "+pos);
//		System.out.println("Vel : "+vel);
//		System.out.println("Acl : "+acceleration);
		// update the heading if the vehicle has a non zero velocity
		if (vel.len2() > 0.00000001) {
			velHead.set(vel);
			velHead.nor();

			velSide.setPerp(velHead);

		}
		angle = (float) vel.getAngle();
		// EnforceNonPenetrationConstraint(this, World()->Agents());

		Vector2D.wrapAround(pos, world.worldBounds);

		if (isSmoothingOn()) {
			smoothedHeading = headingSmoother.update(velHead);
		}

		Transformations.WorldTransform(localVehicleShape, pos, velHead,velSide, transformedVehicleShape);

	}

	public GameWorld getWorld() {
		return world;
	}

	public void setWorld(GameWorld world) {
		this.world = world;
	}

	public boolean isSmoothingOn() {
		return smoothingOn;
	}

	public void setSmoothingOn(boolean smoothingOn) {
		this.smoothingOn = smoothingOn;
	}

	public SteeringControler getSteering() {
		return steering;
	}

	public Vector2D getSmoothedHeading() {
		return smoothedHeading;
	}

	public float getTimeElapsed() {
		return timeElapsed;
	}

}
