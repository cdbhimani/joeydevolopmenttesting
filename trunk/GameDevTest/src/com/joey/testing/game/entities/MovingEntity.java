package com.joey.testing.game.entities;

import com.joey.testing.game.maths.C2DMatrix;
import com.joey.testing.game.shapes.Vector2D;

import java.awt.geom.Point2D;

public abstract class MovingEntity extends BaseGameEntity {
	public Vector2D vel;
	public Vector2D velHead;
	public Vector2D velSide;
	public float angle = 0;
	
	public float mass;
	public float maxSpeed;
	public float maxForce;
	public float maxTurnRate;
	

	public MovingEntity() {
		this.vel = new Vector2D();
		this.velHead = new Vector2D();
		this.velSide = new Vector2D();
		this.mass = 1;
		this.maxSpeed = 1;
		this.maxForce = 1;
		this.maxTurnRate = 1;
	}

	MovingEntity(Vector2D pos, Vector2D velocity,
			float max_speed, Vector2D heading, float mass, Vector2D scale,
			float turn_rate, float max_force) {
		super(0, pos);
		this.velHead = heading;
		this.vel = velocity;
		this.mass = mass;
		this.velSide = heading.perp();
		this.maxSpeed = max_speed;
		this.maxTurnRate = turn_rate;
		this.maxForce = max_force;
		this.scale = scale;
	}

	public boolean rotateHeadingToFacePosition(Vector2D target) {
		Vector2D toTarget = new Vector2D(target.x - pos.x, target.y - pos.y);

		// first determine the angle between the heading vector and the target
		float angle = (float) Math.acos(velHead.dot(toTarget));

		// return true if the player is facing the target
		if (angle < 0.00001)
			return true;

		// clamp the amount to turn to the max turn rate
		if (angle > maxTurnRate)
			angle = maxTurnRate;

		// The next few lines use a rotation matrix to rotate the player's
		// heading
		// vector accordingly
		C2DMatrix RotationMatrix = new C2DMatrix();

		// notice how the direction of rotation has to be determined when
		// creating
		// the rotation matrix
		RotationMatrix.rotate(angle * velHead.sign(toTarget));
		RotationMatrix.transformVector2Ds(velHead);
		RotationMatrix.transformVector2Ds(vel);

		// finally recreate m_vSide
		velSide.setPerp(velHead);

		return false;
	}

	public void setHeading(Vector2D newHeading) {
		assert ((newHeading.lengthSq() - 1.0) < 0.00001);

		velHead = newHeading;

		// the side vector must always be perpendicular to the heading
		velSide.setPerp(velHead);
	}

	public boolean isSpeedMaxedOut() {
		return maxSpeed * maxSpeed >= vel.lengthSq();
	}

	public Vector2D getVel() {
		return vel;
	}

	public void setVel(Vector2D vel) {
		this.vel = vel;
	}

	public Vector2D getVelHead() {
		return velHead;
	}

	public void setVelHead(Vector2D velHead) {
		this.velHead = velHead;
	}

	public Vector2D getVelSide() {
		return velSide;
	}

	public void setVelSide(Vector2D velSide) {
		this.velSide = velSide;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}

	public float getMaxTurnRate() {
		return maxTurnRate;
	}

	public void setMaxTurnRate(float maxTurnRate) {
		this.maxTurnRate = maxTurnRate;
	}

	public float getAngle() {
		// TODO Auto-generated method stub
		return angle;
	}

}
