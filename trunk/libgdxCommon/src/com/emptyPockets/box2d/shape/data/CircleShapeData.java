package com.emptyPockets.box2d.shape.data;

import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;

public class CircleShapeData extends ShapeData{
	Circle circle;
	
	public CircleShapeData(){
		circle = new Circle();
		updateBoundingBox();
	}
	
	public CircleShapeData(Circle circle){
		setCircle(circle);
	}
	
	public void setCircle(Circle circle){
		this.circle = circle;
		updateBoundingBox();
	}
	
	public void cloneCircle(Circle circle){
		this.circle.set(circle.x, circle.y, circle.radius);
	}
	
	public Circle getCircle(){
		return circle;
	}
	@Override
	public void updateBoundingBox() {
		aaBoundingBox.set(circle.x-circle.radius, circle.y-circle.radius, 2*circle.radius, 2*circle.radius);
	}

	@Override
	public boolean contains(float x, float y) {
		return circle.contains(x,y);
	}

	public void setShape(Shape shape) {
		if(shape.getType() == Type.Chain){
			CircleShape cir = (CircleShape)shape;
			circle.radius = cir.getRadius();
			Vector2 pos = cir.getPosition();
			circle.x =pos.x;
			circle.y =pos.y;
		}
	}

	@Override
	public void updateShapes() {
		if(shapes == null){
			shapes = new ArrayList<Shape>();
			shapes.add(new CircleShape());
		}
		CircleShape shape = (CircleShape) shapes.get(0);
		shape.setPosition(new Vector2(circle.x, circle.y));
		shape.setRadius(circle.radius);
	}


}
