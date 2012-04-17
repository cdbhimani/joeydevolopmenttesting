package com.joey.testing.spatialPartitioning;

public class Point2D {
	int x;
	int y;

	public Point2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int distanceSq(Point2D p){
		int dx = p.x-x;
		int dy = p.y-y;
		return dx*dx+dy*dy;
	}
}
