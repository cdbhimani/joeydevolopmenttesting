package com.emptyPockets.box2d.body;

import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.emptyPockets.box2d.shape.data.CircleShapeData;
import com.emptyPockets.box2d.shape.data.ShapeData;

public class BodyData {
	boolean flaggedForRemoval = false;

	private Body body;
	
	BodyType type;
	Rectangle aaBoundingBox;
	private ArrayList<ShapeData> shapes;

	Vector2 pos;
	Vector2 vel;
	float ang;
	float angVel;
	float density;
	
	public BodyData(){
		type = BodyType.DynamicBody;
		setShapes(new ArrayList<ShapeData>());
		getShapes().add(new CircleShapeData(new Circle(0,0,10)));
		pos = new Vector2(0,0);
		vel = new Vector2(0,0);
		ang = 0f;
		angVel = 0f;
		density= 1f;
		aaBoundingBox= new Rectangle();
	}
	
	public BodyData(ArrayList<ShapeData> shape){
		this();
		dispose();
		setShapes(shape);
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
		setBody(world.createBody(def));
		getBody().setUserData(this);
		for(ShapeData shape : getShapes()){
			for(Shape s : shape.getShape()){
				Fixture f = getBody().createFixture(s, density);
				
			}
		}
	}
	
	public void updateAABoundingBox(){
		synchronized (getShapes()) {
			boolean first = true;
			
			for(ShapeData s : getShapes()){
				if(first){
					aaBoundingBox.set(s.getAABoundingBox());
					first = false;
				}else
				{
					aaBoundingBox.merge(s.getAABoundingBox());
				}
			}	
			
			if(first){
				aaBoundingBox.set(0,0,0,0);
			}
		}
	}
	
	public void update(){
	}
	
	public void dispose(){
		if(getBody() != null){
			getBody().getWorld().destroyBody(getBody());
			getBody().setUserData(null);
			setBody(null);
		}
	}
	
	public boolean inside(float x, float y){
		for(ShapeData s : getShapes()){
			if(s.contains(x,y)){
				return true;
			}
		}
		return false;
	}
	public void disposeShape(){
		if(getShapes() != null){
			for(ShapeData shape : getShapes()){
				shape.dispose();
			}
			getShapes().clear();
			setShapes(null);
		}
	}

	public ArrayList<ShapeData> getShapes() {
		return shapes;
	}

	public void setShapes(ArrayList<ShapeData> shapes) {
		this.shapes = shapes;
	}

	public Rectangle getAABoundingBox() {
		return aaBoundingBox;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}
