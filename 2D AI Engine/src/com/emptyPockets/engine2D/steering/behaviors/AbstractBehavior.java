package com.emptyPockets.engine2D.steering.behaviors;

import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.shapes.Vector2D;
import com.emptyPockets.engine2D.steering.SteeringControler;

public abstract class AbstractBehavior {
	public final String name;
	protected SteeringControler steering;
	protected Vehicle vehicle;
	protected long lastUpdate = System.currentTimeMillis();
	
	public long requiredUpdateTime = 500;
	public float forceWeight = 1f;
	public Vector2D force = new Vector2D();
	public boolean enabled = false;
	
	public AbstractBehavior(String name,SteeringControler steering){
		this.steering = steering;
		this.vehicle = steering.vehicle;
		this.name = name;
	}
	
	public void updateUsingTime(){
		if(lastUpdate+requiredUpdateTime < System.currentTimeMillis()){
			update();
		}
	}
	
	public void update(){
		lastUpdate = System.currentTimeMillis();
		if(enabled){
			resetForce();
			calculate(force);
			force.mul(forceWeight);
		}else{
			resetForce();
		}
	}
	
	public void resetForce(){
		force.x = 0;
		force.y = 0;
	}
	
	public void getForce(Vector2D force){
		force.x = this.force.x;
		force.y = this.force.y;
	}

	protected abstract void calculate(Vector2D force);
}