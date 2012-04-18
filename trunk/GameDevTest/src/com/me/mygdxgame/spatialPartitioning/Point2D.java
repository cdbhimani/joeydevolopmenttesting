package com.me.mygdxgame.spatialPartitioning;

public class Point2D {
	public float x;
	public float y;
	float vx;
	float vy;

	public Point2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Point2D() {
		this.x = 0;
		this.y = 0;
	}

	public Point2D(Point2D p) {
		this.x = p.x;
		this.y = p.y;
	}

	public float distanceSq(Point2D p){
		float dx = p.x-x;
		float dy = p.y-y;
		return dx*dx+dy*dy;
	}
}
