package com.joey.testing.game.shapes;

public abstract class Shape2D {
	public float x;
	public float y;
	public Shape2DType type;
	
	public Shape2D(Shape2DType type){
		this.type = type;
	}
	
	public Shape2DType getType(){
		return type;
	}
	
	
	public boolean contains(Shape2D p) {
		switch (p.type) {
			case Rectangle2D:return contains((Rectangle2D)p);
			case Ellipse2D:return contains((Ellipse2D)p);
			case Circle2D:return contains((Circle2D)p);
			case Vector2D:return contains((Vector2D)p);
		}
		return false;
	}
	
	public boolean intersects(Shape2D p) {
		switch (p.type) {
		case Rectangle2D:return intersects((Rectangle2D)p);
		case Ellipse2D:return intersects((Ellipse2D)p);
		case Circle2D:return intersects((Circle2D)p);
		case Vector2D:return intersects((Vector2D)p);
	}
	return false;
}
	
	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract boolean contains(float x, float y);
	public abstract boolean intersects(float x, float y);
	
	public abstract boolean contains(Vector2D p);
	public abstract boolean intersects(Vector2D p);

	public abstract boolean contains(Ellipse2D p);
	public abstract boolean intersects(Ellipse2D p);

	public abstract boolean contains(Circle2D p);
	public abstract boolean intersects(Circle2D p);
	
	public abstract boolean contains(Rectangle2D p);
	public abstract boolean intersects(Rectangle2D p);

}
