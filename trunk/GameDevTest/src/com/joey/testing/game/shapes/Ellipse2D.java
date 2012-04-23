package com.joey.testing.game.shapes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Ellipse2D extends Shape2D{
	float radX;
	float radY;

	public Ellipse2D(float x, float y, float radX, float radY){
		super(Shape2DType.Ellipse2D);
		this.x =x;
		this.y =y;
		this.radX = radX;
		this.radY = radY;
	}
	

	public void moveTo(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public boolean contains(float x, float y){
		float dx = x-this.x;
		float dy = y-this.y;
		return (dx*dx)/(radX*radX)+(dy*dy)/(radY*radY) < 1;
	}
	
	public boolean intersects(float x, float y){
		float dx = x-this.x;
		float dy = y-this.y;
		return (dx*dx)/(radX*radX)+(dy*dy)/(radY*radY) <= 1;
	}
	
	public boolean contains(Vector2D p){
		float dx = p.x-x;
		float dy = p.y-y;
		return (dx*dx)/(radX*radX)+(dy*dy)/(radY*radY) < 1;
	}
	
	public boolean intersects(Vector2D p){
		float dx = p.x-this.x;
		float dy = p.y-this.y;
		return (dx*dx)/(radX*radX)+(dy*dy)/(radY*radY) <= 1;
	}

	public boolean contains(Rectangle2D r){
		return
				contains(r.x,r.y) &&
				contains(r.x+r.sizeX,r.y) &&
				contains(r.x,r.y+r.sizeY) &&
				contains(r.x+r.sizeX,r.y+r.sizeY);
	}

	public boolean intersects(Rectangle2D r){
		return r.intersects(this);
	}
	
	@Override
	public boolean contains(Ellipse2D e) {
		// TODO Auto-generated method stub
		return contains(e.x-e.radX, e.y) &&
				contains(e.x+e.radX, e.y) &&
				contains(e.x, e.y-e.radY) &&
				contains(e.x, e.y+e.radY);
	}
	@Override
	public boolean intersects(Ellipse2D p) {
		float dx = p.x-this.x;
		float dy = p.y-this.y;
		dx = dx*dx;
		dy = dy*dy;
		
		return dx < radX*radX && dy < radY*radY;
	}


	@Override
	public boolean contains(Circle2D p) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean intersects(Circle2D p) {
		// TODO Auto-generated method stub
		return false;
	}


}
