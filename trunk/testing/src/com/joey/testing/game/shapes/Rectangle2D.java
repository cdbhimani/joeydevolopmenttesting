package com.joey.testing.game.shapes;

public class Rectangle2D extends Shape2D {
	public float sizeX;
	public float sizeY;

	public Rectangle2D(){
		super(Shape2DType.Rectangle2D);
		x = 0;
		y = 0;
		sizeX = 0;
		sizeY = 0;
	}
	public Rectangle2D(float x, float y, float sizeX, float sizeY){
		this();
		this.x = x;
		this.y = y;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	/**
	 * Tests if this rectangle contains the point fully
	 * @param p
	 * @return
	 */
	public boolean contains(float px, float py){
		return px > x && px < (x+sizeX) && py > y && py < (y+sizeY);
	}

	/**
	 * Tests if this rectangle contains the point fully or if its on the boundary
	 * @param p
	 * @return
	 */
	public boolean intersects(float px, float py){
		return px >= x && px <= (x+sizeX) && py >= y && py <= (y+sizeY);
	}
	/**
	 * Tests if this rectangle contains the point fully
	 * @param p
	 * @return
	 */
	public boolean contains(Vector2D p){
		return p.x > x && p.x < (x+sizeX) && p.y > y && p.y < (y+sizeY);
	}

	/**
	 * Tests if this rectangle contains the point fully or if its on the boundary
	 * @param p
	 * @return
	 */
	public boolean intersects(Vector2D p){
		return p.x >= x && p.x <= (x+sizeX) && p.y >= y && p.y <= (y+sizeY);
	}

	/**
	 * This will test to see if this rectange fully contains a second rectangle
	 * If the second rectangle touches the boundary it does not contain it
	 * @param r
	 * @return
	 */
	public boolean contains(Rectangle2D r){
		return contains(r.x,r.y) &&
				contains(r.x+r.sizeX, r.y+r.sizeY);
	}

	/**
	 * Test to see if this rectange intersects or contains the second rectangle
	 * This occours if this rectangle intersects/contains any of the points
	 *
	 * This works if either rectangle lies withing the other
	 * @param r
	 * @return
	 */
	public boolean intersects(Rectangle2D r){
		return
				intersects(r.x,r.y) ||
				intersects(r.x+r.sizeX,r.y) ||
				intersects(r.x, r.y+r.sizeY) ||
				intersects(r.x+r.sizeX, r.y+r.sizeY) ||
				r.intersects(x,y) ||
				r.intersects(x+sizeX,y) ||
				r.intersects(x,y+sizeY) ||
				r.intersects(x+sizeX,y+sizeY)
				;
	}

	public boolean contains(Ellipse2D e){
		return
				contains(e.x-e.radX, e.y) &&
				contains(e.x+e.radX, e.y) &&
				contains(e.x, e.y-e.radY) &&
				contains(e.x, e.y+e.radY);
	}

	public boolean intersects(Ellipse2D e){
		return
				intersects(e.x-e.radX, e.y) ||
				intersects(e.x+e.radX, e.y) ||
				intersects(e.x, e.y-e.radY) ||
				intersects(e.x, e.y+e.radY) ||
				e.intersects(x, y) ||
				e.intersects(x+sizeX, y) ||
				e.intersects(x, y+sizeY) ||
				e.intersects(x+sizeX, y+sizeY);
	}
	
	public void translate(float x, float y){
		this.x+=x;
		this.y+=y;
	}
	public void moveCenterTo(Vector2D p) {
		x = p.x;
		y = p.y;
	}
	@Override
	public boolean contains(Circle2D e) {
		return
		contains(e.x-e.rad, e.y) &&
		contains(e.x+e.rad, e.y) &&
		contains(e.x, e.y-e.rad) &&
		contains(e.x, e.y+e.rad);
	}
	@Override
	public boolean intersects(Circle2D e) {
		// TODO Auto-generated method stub
		return
				intersects(e.x-e.rad, e.y) ||
				intersects(e.x+e.rad, e.y) ||
				intersects(e.x, e.y-e.rad) ||
				intersects(e.x, e.y+e.rad) ||
				e.intersects(x, y) ||
				e.intersects(x+sizeX, y) ||
				e.intersects(x, y+sizeY) ||
				e.intersects(x+sizeX, y+sizeY);
	}
	
	

}
