package com.emptyPockets.box2d;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Tire {
	private Body body;
	float maxForwardSpeed = 25;
	float maxBackwardSpeed = -4;
	float maxDriveForce = 30;
	  
	float maxLateralImpulse = 3;
	float maxDriveTorque = 5;
	
	
	float powerDeadZone = 0.01f;
	
	public Tire(World world){
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.type=BodyType.DynamicBody;
		body = (world.createBody(bodyDef));
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f,1.25f);
		
		getBody().createFixture(shape,1);
		getBody().setUserData(this);
		
		shape.dispose();
	}
	
	public Vector2 getLateralVelocity(){
		Vector2 currentRightNormal = getBody().getWorldVector(new Vector2(1,0)).cpy();
		return currentRightNormal.mul(currentRightNormal.dot(getBody().getLinearVelocity()));
	}
	
	public Vector2 getForwardVelocity(){
		Vector2 currentRightNormal = getBody().getWorldVector(new Vector2(0,1)).cpy();
		return currentRightNormal.mul(currentRightNormal.dot(getBody().getLinearVelocity()));
	}
	
	public void setCharacteristics(float maxForwardSpeed, float maxBackwardSpeed, float maxDriveForce, float maxLateralImpulse) {
	    this.maxForwardSpeed = maxForwardSpeed;
	    this.maxBackwardSpeed = maxBackwardSpeed;
	    this.maxDriveForce = maxDriveForce;
	    this.maxLateralImpulse = maxLateralImpulse;
	}
	 
	public void updateFriction(){
		//Kill linear
		Vector2 impulse = getLateralVelocity().mul(-getBody().getMass());
		if(impulse.len()>maxLateralImpulse){
			impulse.nor().mul(maxLateralImpulse);
		}
		getBody().applyLinearImpulse(impulse, getBody().getWorldCenter());
		
		//Kill Rotation
		getBody().applyAngularImpulse(0.1f*getBody().getInertia()*-getBody().getAngularVelocity());
		
		//Apply Drag
		Vector2 currentforwardNormal = getForwardVelocity();
		float currentForwardSpeed = currentforwardNormal.len();
		float dragForceMagnitude = -2*currentForwardSpeed;
		getBody().applyForce(currentforwardNormal.nor().mul(dragForceMagnitude), getBody().getWorldCenter());
	}
	
	public void updateDrive(float power){
		float desiredSpeed = 0;
		if(Math.abs(power)<powerDeadZone){
			desiredSpeed = 0;
		}else{
			if(power>0){
				desiredSpeed = maxForwardSpeed;
			}else{
				desiredSpeed = maxBackwardSpeed;
			}
		}
		
		Vector2 currentForwardNormal = getBody().getWorldVector(new Vector2(0,1)).cpy();

		float currentSpeed = getForwardVelocity().dot(currentForwardNormal);

		float force=0;
		if(desiredSpeed  >currentSpeed){
			force = maxDriveForce;
		} else if(desiredSpeed < currentSpeed){
			force = -maxDriveForce;
		}
		
		force*=Math.abs(power);
//		System.out.println("\nVel"+body.getLinearVelocity());
//		System.out.println(desiredSpeed+" : "+force);
		currentForwardNormal.nor().mul(force);
//		System.out.println(currentForwardNormal);
		getBody().applyForce(currentForwardNormal.mul(force), getBody().getWorldCenter());
	}
	
	public void updateTurn(float power){
		float desiredtorque = maxDriveTorque;
		desiredtorque*=-power;
		getBody().applyTorque(desiredtorque);
	}
	
	public void print(){
		System.out.printf("V[%3.3f,%3.3f] A[%f] AV[%f] M[%3.3f]\n",
				body.getLinearVelocity().x, 
				body.getLinearVelocity().y, 
				body.getAngle(),
				body.getAngularVelocity(),
				body.getMass()
				);
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if(getBody() != null)
			getBody().getWorld().destroyBody(getBody());
	}

	public Body getBody() {
		return body;
	}

	public float getMaxForwardSpeed() {
		return maxForwardSpeed;
	}

	public void setMaxForwardSpeed(float maxForwardSpeed) {
		this.maxForwardSpeed = maxForwardSpeed;
	}

	public float getMaxDriveForce() {
		return maxDriveForce;
	}

	public void setMaxDriveForce(float maxDriveForce) {
		this.maxDriveForce = maxDriveForce;
	}

	public float getMaxLateralImpulse() {
		return maxLateralImpulse;
	}

	public void setMaxLateralImpulse(float maxLateralImpulse) {
		this.maxLateralImpulse = maxLateralImpulse;
	}

	public float getMaxDriveTorque() {
		return maxDriveTorque;
	}

	public void setMaxDriveTorque(float maxDriveTorque) {
		this.maxDriveTorque = maxDriveTorque;
	}

	public float getMaxBackwardSpeed() {
		return maxBackwardSpeed;
	}

	public void setMaxBackwardSpeed(float maxBackwardSpeed) {
		this.maxBackwardSpeed = maxBackwardSpeed;
	}
}
