package com.emptyPockets.box2d;

import java.util.Vector;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.emptyPockets.network.NetworkEntity;

public class Car {
	private Body body;
	Vector<Tire> tires = new Vector<Tire>();
	RevoluteJoint flJoint;
	RevoluteJoint frJoint;
	
	public Car(World world){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type=BodyType.DynamicBody;
		
		setBody(world.createBody(bodyDef));
		
		PolygonShape shape = new PolygonShape();
		
		Vector2[] vertices = new Vector2[8];
		vertices[0] = new Vector2( 1.5f,   0);
        vertices[1] = new Vector2(   3f, 2.5f);
        vertices[2] = new Vector2( 2.8f, 5.5f);
        vertices[3] = new Vector2(   1f,  10f);
        vertices[4] = new Vector2(  -1f,  10f);
        vertices[5] = new Vector2(-2.8f, 5.5f);
        vertices[6] = new Vector2(  -3f, 2.5f);
        vertices[7] = new Vector2(-1.5f,   0f);
		                                
		shape.set(vertices);
		
		getBody().createFixture(shape, 0.1f);
		
		//Create Tires
		//prepare common joint parameters
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = getBody();
        jointDef.enableLimit = true;
        jointDef.lowerAngle = 0;
        jointDef.upperAngle = 0;
        jointDef.localAnchorB.set(0, 0);//center of tire

        float maxForwardSpeed = 100;
        float maxBackwardSpeed = -50;
        float backTireMaxDriveForce = 30;
        float frontTireMaxDriveForce = 20;
        float backTireMaxLateralImpulse = 8.5f;
        float frontTireMaxLateralImpulse = 7.5f;

        //back left tire
        Tire tire = new Tire(world);
        tire.setCharacteristics(maxForwardSpeed, maxBackwardSpeed, backTireMaxDriveForce, backTireMaxLateralImpulse);
        jointDef.bodyB = tire.getBody();
        jointDef.localAnchorA.set( -3, 0.75f );
        world.createJoint(jointDef );
        tires.add(tire);

        //back right tire
        tire = new Tire(world);
        tire.setCharacteristics(maxForwardSpeed, maxBackwardSpeed, backTireMaxDriveForce, backTireMaxLateralImpulse);
        jointDef.bodyB = tire.getBody();
        jointDef.localAnchorA.set( 3, 0.75f );
        world.createJoint(jointDef );
        tires.add(tire);

        //front left tire
        tire = new Tire(world);
        tire.setCharacteristics(maxForwardSpeed, maxBackwardSpeed, frontTireMaxDriveForce, frontTireMaxLateralImpulse);
        jointDef.bodyB = tire.getBody();
        jointDef.localAnchorA.set( -3, 8.5f );
        flJoint = (RevoluteJoint)world.createJoint(jointDef );
        tires.add(tire);

        //front right tire
        tire = new Tire(world);
        tire.setCharacteristics(maxForwardSpeed, maxBackwardSpeed, frontTireMaxDriveForce, frontTireMaxLateralImpulse);
        jointDef.bodyB = tire.getBody();
        jointDef.localAnchorA.set( 3, 8.5f );
        frJoint = (RevoluteJoint)world.createJoint( jointDef );
        tires.add(tire);
	}
	
	public void update(float x, float y){
		for(Tire tire : tires){
			tire.updateFriction();
		}
		
		for(Tire tire : tires){
			tire.updateDrive(y);
		}
		
		 //control steering
        float lockAngle = -35 * MathUtils.degRad*x;
        float turnSpeedPerSec = 160 * MathUtils.degRad;//from lock to lock in 0.5 sec
        float turnPerTimeStep = turnSpeedPerSec / 60.0f;
        float desiredAngle = lockAngle;
        

        float angleNow = flJoint.getJointAngle();
        float angleToTurn = desiredAngle - angleNow;
        angleToTurn = MathUtils.clamp( angleToTurn, -turnPerTimeStep, turnPerTimeStep );
        float newAngle = angleNow + angleToTurn;
        flJoint.setLimits( newAngle, newAngle );
        frJoint.setLimits( newAngle, newAngle );
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}
