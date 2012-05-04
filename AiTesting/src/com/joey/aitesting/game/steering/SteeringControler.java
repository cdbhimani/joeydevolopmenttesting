package com.joey.aitesting.game.steering;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steering.behaviors.AbstractBehavior;
import com.joey.aitesting.game.steering.behaviors.Alignment;
import com.joey.aitesting.game.steering.behaviors.Arrive;
import com.joey.aitesting.game.steering.behaviors.Cohesion;
import com.joey.aitesting.game.steering.behaviors.Evade;
import com.joey.aitesting.game.steering.behaviors.Flee;
import com.joey.aitesting.game.steering.behaviors.ObstacleAvoidance;
import com.joey.aitesting.game.steering.behaviors.Persuit;
import com.joey.aitesting.game.steering.behaviors.Seek;
import com.joey.aitesting.game.steering.behaviors.Seperation;
import com.joey.aitesting.game.steering.behaviors.Wander;


public class SteeringControler {
	Vehicle vehicle;
	
	public Flee flee;
	public Seek seek;
	public Arrive arrive;
	
	public Persuit persult;
	public Evade evade;
	
	public Alignment alignment;
	public Seperation seperation;
	public Cohesion cohesion;

	public Wander wander;
	public ObstacleAvoidance obstacleAvoidance;
	
	public SteeringControler(Vehicle veh){
		this.vehicle = veh;
	}
	
	private void updateBehavior(AbstractBehavior behave){
		behave.updateUsingTime();
	}
	
	public void calculateForce(Vector2D steeringForce){
		
		if(flee.enabled){
			updateBehavior(flee);
			steeringForce.add(flee.force);
		}
		
		if (steeringForce.lengthSq() > vehicle.maxForce * vehicle.maxForce) {
			steeringForce.normalise();
			steeringForce.scale(vehicle.maxForce);
		}
	}
}
