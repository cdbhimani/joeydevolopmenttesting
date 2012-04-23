package com.me.mygdxgame.spatialPartitioning;

public class Entity2D {
	public float x;
	public float y;
	public float rad = 1;
	public float mass = 1;
	public float vx;
	public float vy;
	
	public float old_x;
	public float old_y;
	public float old_rad = 1;
	public float old_mass = 1;
	public float old_vx;
	public float old_vy;
	
	public Entity2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Entity2D() {
		this.x = 0;
		this.y = 0;
	}

	public Entity2D(Entity2D p) {
		this.x = p.x;
		this.y = p.y;
	}

	public float distanceSq(Entity2D p){
		float dx = p.x-x;
		float dy = p.y-y;
		return dx*dx+dy*dy;
	}
	
	public String toString(){
		return super.toString()+"["+x+","+y+"]";
	}
	
	public void flip(){
		old_x =x;
		old_y= y;
		old_vx = vx;
		old_vy = vy;
		old_rad= rad;
	}
}
