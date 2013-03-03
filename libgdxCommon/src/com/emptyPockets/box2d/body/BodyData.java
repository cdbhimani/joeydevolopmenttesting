package com.emptyPockets.box2d.body;

import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.emptyPockets.box2d.shape.data.CircleShapeData;
import com.emptyPockets.box2d.shape.data.ShapeData;

public class BodyData {
	boolean flaggedForRemoval = false;

	Body body;
	
	BodyType type;
	ArrayList<ShapeData> shapes;

	Vector2 pos;
	Vector2 vel;
	float ang;
	float angVel;
	float density;
	
	public BodyData(){
		type = BodyType.DynamicBody;
		shapes = new ArrayList<ShapeData>();
		shapes.add(new CircleShapeData(new Circle(0,0,10)));
		pos = new Vector2(10,10);
		vel = new Vector2(0,0);
		ang = 0f;
		angVel = 0f;
		density= 1f;
	}
	
	public BodyData(ArrayList<ShapeData> shape){
		this();
		dispose();
		shapes = shape;
	}
	public BodyDef createBodyDef(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(pos);
		bodyDef.linearVelocity .set(vel);
		bodyDef.angle = ang;
		bodyDef.angularVelocity = angVel;
		return bodyDef;
	}
	
	public void createBody(World world){
		BodyDef def = createBodyDef();
		body = world.createBody(def);
		body.setUserData(this);
		for(ShapeData shape : shapes){
			for(Shape s : shape.getShape()){
				body.createFixture(s, density);
			}
		}
	}
	
	public void update(){
	}
	
	public void dispose(){
		if(body != null){
			body.getWorld().destroyBody(body);
			body.setUserData(null);
			body = null;
		}
	}
	
	public void disposeShape(){
		if(shapes != null){
			for(ShapeData shape : shapes){
				shape.dispose();
			}
			shapes.clear();
			shapes = null;
		}
	}
}
