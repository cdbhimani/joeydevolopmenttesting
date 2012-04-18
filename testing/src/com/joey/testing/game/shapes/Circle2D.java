package com.joey.testing.game.shapes;

public class Circle2D extends Shape2D {

	public float rad = 0;

	
	public Circle2D() {
		super(Shape2DType.Circle2D);
		// TODO Auto-generated constructor stub
	}
	
	public Circle2D(float x, float y, float rad){
		this();
		this.x = x;
		this.y = y;
		this.rad = rad;
	}
	@Override
	public boolean contains(float x, float y) {
		float dx = this.x -x;
		float dy = this.y-y;
		return dx*dx+dy*dy<rad*rad;
	}

	@Override
	public boolean intersects(float x, float y) {
		float dx = this.x-x;
		float dy = this.y-y;
		return dx*dx+dy*dy<=rad*rad;
	}

	@Override
	public boolean contains(Vector2D p) {
		float dx = this.x-p.x;
		float dy = this.y-p.y;
		return dx*dx+dy*dy<rad*rad;
	}
	@Override
	public boolean intersects(Vector2D p) {
		float dx = this.x-p.x;
		float dy = this.y-p.y;
		return dx*dx+dy*dy<=rad*rad;
	}

	@Override
	public boolean contains(Ellipse2D p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Ellipse2D p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Rectangle2D p) {
		// TODO Auto-generated method stub
		return 
				contains(p.x, p.y) &&
				contains(p.x+p.sizeX, p.y) &&
				contains(p.x, p.y+p.sizeY) &&
				contains(p.x+p.sizeX, p.y+p.sizeY)
				
				;
	}

	@Override
	public boolean intersects(Rectangle2D p) {
		return 
				intersects(p.x, p.y) ||
				intersects(p.x+p.sizeX, p.y) ||
				intersects(p.x, p.y+p.sizeY) ||
				intersects(p.x+p.sizeX, p.y+p.sizeY)  ||
				p.intersects(this)
				;
	}

	@Override
	public boolean contains(Circle2D p) {
		if(p.rad > rad){
			return false;
		}
		
		float dx = p.x-this.x;
		float dy = p.y-this.y;
		float r =  (rad-2*p.rad)+p.rad;
		

		return dx*dx+dy*dy <=  r*r;
	}


	@Override
	public boolean intersects(Circle2D p) {
		float dx = this.x-p.x;
		float dy = this.y-p.y;
		float r = p.rad+rad;
		return dx*dx+dy*dy<=r*r;
	}
	
}
