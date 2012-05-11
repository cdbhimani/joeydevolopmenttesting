package com.joey.aitesting.game.steering.behaviors;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steering.SteeringControler;

public abstract class AbstractBehavior {
	protected SteeringControler steering;
	protected Vehicle vehicle;
	protected long lastUpdate;
	
	public long requiredUpdateTime;
	public float forceWeight = 1f;
	public Vector2D force = new Vector2D();
	public boolean enabled = false;
	
	public AbstractBehavior(SteeringControler steering){
		this.steering = steering;
		this.vehicle = steering.vehicle;
	}
	
	public void updateUsingTime(){
		if(lastUpdate+requiredUpdateTime > System.currentTimeMillis()){
			update();
		}
	}
	
	public void update(){
		lastUpdate = System.currentTimeMillis();
		if(enabled){
			calculate(force);
			force.scale(forceWeight);
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