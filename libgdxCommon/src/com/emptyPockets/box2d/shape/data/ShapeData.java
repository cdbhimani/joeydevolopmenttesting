package com.emptyPockets.box2d.shape.data;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

public abstract class ShapeData {
	public static int count = 0;
	String name  = "Shape "+count++;
	Rectangle aaBoundingBox = new Rectangle();
	ArrayList<Shape> shapes;
	
	public abstract void updateShapes();
	public abstract void updateBoundingBox();
	public abstract boolean contains(float x, float y);

	public ArrayList<Shape> getShape(){
		updateShapes();
		return shapes;
	}
	public boolean contains(Vector2 v){
		return contains(v.x, v.y);
	}
	
	public boolean containsAABB(float x, float y){
		return aaBoundingBox.contains(x, y);
	}
	
	public boolean containsAABB(Vector2 p){
		return containsAABB(p.x, p.y);
	}
	
	public boolean containsAABB(Rectangle rec){
		return aaBoundingBox.contains(rec);
	}
	public boolean intersectsAABB(Rectangle rec){
		return aaBoundingBox.overlaps(rec);
	}
	
	public void dispose(){
		if(shapes != null){
			for(Shape s : shapes){
				s.dispose();
			}
			shapes.clear();
			shapes = null;
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Rectangle getAABoundingBox() {
		return aaBoundingBox;
	}
}
