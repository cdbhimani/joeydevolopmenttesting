package com.emptyPockets.gui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptyPockets.box2d.Box2DScreen;

public class Network extends Box2DScreen{

	public Network(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		setShowDebug(true);
		setClearColor(Color.BLACK);
	}


	@Override
	public void createWorld(World world) {
		float rA = 0.5f;
		float rB = 1f;
		
		BodyDef bdA = new BodyDef();
		bdA.position.x = -1;
		Body bodyA = world.createBody(bdA);
		CircleShape shapeA = new CircleShape();
		shapeA.setRadius(rA);
		bodyA.createFixture(shapeA, 1);
		shapeA.dispose();
		
		BodyDef bdB = new BodyDef();
		bdB.position.x = 1;
		Body bodyB = world.createBody(bdB);
		CircleShape shapeB = new CircleShape();
		shapeB.setRadius(rB);
		bodyB.createFixture(shapeB, 1);
		shapeB.dispose();
		
		DistanceJointDef jd = new DistanceJointDef();
		jd.bodyA = bodyA;
		jd.bodyB = bodyB;
		jd.localAnchorA.set(0,0);
		jd.localAnchorB.set(0,0);
		jd.type = JointType.DistanceJoint;
		jd.length = 100;
		
		DistanceJoint dj = (DistanceJoint)world.createJoint(jd);
		
	}

	@Override
	public void updateWorldCamera(OrthographicCamera worldCamera) {
		// TODO Auto-generated method stub
		super.updateWorldCamera(worldCamera);
		worldCamera.position.x = 0;
		worldCamera.position.y = 0;
	}
	@Override
	public void createStage(Stage stage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub
		
	}
}
