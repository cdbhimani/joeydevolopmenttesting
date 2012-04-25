package com.joey.aitesting.game.shapes;

public class Rectangle2D {
	public float x1;
	public float y1;
	public float x2;
	public float y2;

	
	public Rectangle2D() {
		// TODO Auto-generated constructor stub
	}
	
	public Rectangle2D(float x1, float y1, float x2, float y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void ensureOrder(){
		float tmp;
		if(x1>x2){
			tmp= x1;
			x2 = x1;
			x1 = x2;
		}
		
		if(y1>y2){
			tmp = y1;
			y2 = y1;
			y1 = y2;
		}
	}

	public boolean contains(float x, float y) {
		return x>x1&&y>y1&&x<x2&&y<y2;
	}

	public boolean intersects(float x, float y) {
		return x>=x1&&y>=y1&&x<=x2&&y<=y2;
	}
	
	public boolean contains(Vector2D p) {
		return p.x>x1&&p.y>y1&&p.x<x2&&p.y<y2;
	}

	public boolean intersects(Vector2D p) {
		return p.x>=x1&&p.y>=y1&&p.x<=x2&&p.y<=y2;
	}

	public boolean contains(Rectangle2D p) {
		return 
				contains(p.x1, p.y1 ) &&
				contains(p.x1, p.y2 ) &&
				contains(p.x2, p.y1 ) &&
				contains(p.x2, p.y2 )
				;
	}

	public boolean intersects(Rectangle2D r2) {
		return !(  x1 >= r2.x2 ||
                r2.x1 >= x2 ||
                y1 >= r2.y2 ||
                r2.y1 >= y2);
	}
	
	public String toString(){
		return "["+x1+","+y1+"]-["+x2+","+y2+"]";
	}

	public Vector2D getRandomPos(){
		return new Vector2D((float)(x1+Math.random()*(getWidth())), (float)(y1+Math.random()*(getHeight())));
	}
	public float getWidth() {
		// TODO Auto-generated method stub
		return x2-x1;
	}
	
	public float getHeight() {
		// TODO Auto-generated method stub
		return y2-y1;
	}
}
