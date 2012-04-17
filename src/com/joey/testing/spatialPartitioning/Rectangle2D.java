package com.joey.testing.spatialPartitioning;

public class Rectangle2D {
	public int x1;
	public int y1;
	public int x2;
	public int y2;

	public Rectangle2D(int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	public boolean contains(Point2D p) {
		return p.x>x1&&p.y>y1&&p.x<x2&&p.y<y2;
	}

	public boolean intersects(Point2D p) {
		return p.x>=x1&&p.y>=y1&&p.x<=x2&&p.y<=y2;
	}

}
