package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

public abstract class AbstractBehavior {
	Vehicle vehicle;
	
	long lastUpdate;
	public long updateTime;
	
	public Vector2D force = new Vector2D();
	public boolean enabled = false;
	
	public AbstractBehavior(Vehicle veh){
		this.vehicle = veh;
	}
	
	public void updateUsingTime(){
		if(lastUpdate+updateTime > System.nanoTime()){
			update();
		}
	}
	
	public void update(){
		lastUpdate = System.nanoTime();
		calculate(force);
	}
	
	public void resetForce(){
		force.x = 0;
		force.y = 0;
	}
	
	public abstract void calculate(Vector2D force);
}