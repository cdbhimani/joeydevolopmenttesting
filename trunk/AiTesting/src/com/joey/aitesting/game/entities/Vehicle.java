package com.joey.aitesting.game.entities;

import java.util.ArrayList;

import com.joey.aitesting.game.GameWorld;
import com.joey.aitesting.game.HeadingSmoother;
import com.joey.aitesting.game.maths.Transformations;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steeringBehaviors.SteeringBehaviors;

public class Vehicle extends MovingEntity {
	public GameWorld world;
	public SteeringBehaviors steering;
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
		this.steering = new SteeringBehaviors(this);
		
		setupShape(5f);
	}

	public void setupShape(float scale) {
		localVehicleShape.add(new Vector2D(-1.0f*scale, 0.6f*scale));
		localVehicleShape.add(new Vector2D(1.0f*scale, 0.0f*scale));
		localVehicleShape.add(new Vector2D(-1.0f*scale, -0.6f*scale));
		
		transformedVehicleShape.add(new Vector2D(-1.0f*scale, 0.6f*scale));
		transformedVehicleShape.add(new Vector2D(1.0f*scale, 0.0f*scale));
		transformedVehicleShape.add(new Vector2D(-1.0f*scale, -0.6f*scale));
	}

	@Override
	public void update(float time_elapsed) {
		
		//System.out.println("\n\nEntity : "+id);
		// update the time elapsed
		this.timeElapsed = time_elapsed;

		// keep a record of its old position so we can update its cell later
		// in this method
		Vector2D OldPos = (Vector2D) pos.clone();

		Vector2D steeringForce;
		Vector2D acceleration = new Vector2D();
		// calculate the combined force from each steering behavior in the
		// vehicle's list
		steeringForce = steering.calculate(time_elapsed);
		
		// Acceleration = Force/Mass
		acceleration.x = steeringForce.x / mass;
		acceleration.y = steeringForce.y / mass;

		// update velocity
		vel.x += acceleration.x * time_elapsed;
		vel.y += acceleration.y * time_elapsed;

		// make sure vehicle does not exceed maximum velocity
		vel.truncate(maxSpeed);

		// update the position
		pos.x += vel.x * time_elapsed;
		pos.y += vel.y * time_elapsed;
		
//		System.out.println("Steering Force : "+steeringForce);
//		System.out.println("Time : "+time_elapsed);
//		System.out.println("Pos : "+pos);
//		System.out.println("Vel : "+vel);
//		System.out.println("Acl : "+acceleration);
		// update the heading if the vehicle has a non zero velocity
		if (vel.lengthSq() > 0.00000001) {
			velHead.setLocation(vel);
			velHead.normalise();

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

	public SteeringBehaviors getSteering() {
		return steering;
	}

	public Vector2D getSmoothedHeading() {
		return smoothedHeading;
	}

	public float getTimeElapsed() {
		return timeElapsed;
	}

}
