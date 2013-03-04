package com.emptyPockets.box2d.shape.data;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;

public abstract class ShapeData {
	String name  = "";
	Rectangle boundingBox = new Rectangle();
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
		return boundingBox.contains(x, y);
	}
	
	public boolean containsAABB(Vector2 p){
		return containsAABB(p.x, p.y);
	}
	
	public boolean containsAABB(Rectangle rec){
		return boundingBox.contains(rec);
	}
	public boolean intersectsAABB(Rectangle rec){
		return boundingBox.overlaps(rec);
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
	
}
