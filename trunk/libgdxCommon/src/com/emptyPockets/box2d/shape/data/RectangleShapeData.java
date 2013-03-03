package com.emptyPockets.box2d.shape.data;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class RectangleShapeData extends ShapeData{
	Rectangle rectangle;
	
	public RectangleShapeData(){
		rectangle = new Rectangle();
	}
	
	public RectangleShapeData(Rectangle rectangle){
		setRectangle(rectangle);
	}
	
	public void setRectangle(Rectangle rectangle){
		this.rectangle = rectangle;
	}

	public Rectangle getRectangle(){
		return rectangle;
	}
	@Override
	public void updateBoundingBox() {
		boundingBox.set(rectangle);
	}

	@Override
	public boolean contains(float x, float y) {
		return rectangle.contains(x,y);
	}

//	@Override
	public void setShape(Shape shape) {
		throw new InvalidParameterException("Not yet implemented");
	}


	@Override
	public void updateShapes() {
		if(shapes == null){
			shapes = new ArrayList<Shape>();
			shapes.add(new PolygonShape());
		}
		PolygonShape shape = (PolygonShape) shapes.get(0);
		Vector2 pos = new Vector2();
		pos.x = rectangle.x+rectangle.width/2;
		pos.y = rectangle.y+rectangle.height/2;
		shape.setAsBox(rectangle.width, rectangle.height,pos, 0);
	}
}
