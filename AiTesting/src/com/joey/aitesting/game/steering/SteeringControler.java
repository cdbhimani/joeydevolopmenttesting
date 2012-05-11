package com.joey.aitesting.game.steering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.maths.Transformations;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.shapes.WorldWrapper;
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
import com.joey.aitesting.game.steering.behaviors.WallAvoidance;
import com.joey.aitesting.game.steering.behaviors.Wander;


public class SteeringControler {
	public Vehicle vehicle;
	
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
	
	
	/**
	 * These hold information about the visible vehicles
	 */
	public float neighborDetectionRadius = 100;
	private boolean visibleVehiclesFound = false;
	Rectangle2D neighborDetectionRegion = new Rectangle2D();
	Rectangle2D regHold[] = null;//For search around edge of world
	private HashSet<Vehicle> visibleVehicles = new HashSet<Vehicle>();
	
	/**
	 * These variables hold informaiton about the vehicle 
	 * "Feelers"
	 * @param veh
	 */
	private boolean localFeelersFound = false;
	int feelerCount = 0;
	float feelerLength = 1;
	float feelerFOV = 0.5f;
	List<Vector2D> localFeelers;
	List<Vector2D> worldFeelers;
	
	public SteeringControler(Vehicle veh){
		this.vehicle = veh;
		createBehaviours();
	}
	
	private void createBehaviours(){
	}
		
	private void calculateNeighbobors(Vehicle vehicle, HashSet<Vehicle> neighbors, Rectangle2D reg){
		if(vehicle.world.worldBounds.contains(reg)){
			vehicle.world.quadTree.getPointsInRegion(reg, neighbors);
		}else{
			//If not fully contained in the world search off wrapped world
			if(regHold == null){
				regHold = new Rectangle2D[4];
				regHold[0] = new Rectangle2D(0,0,5,5);
				regHold[1] = new Rectangle2D(0,0,5,5);
				regHold[2] = new Rectangle2D(0,0,5,5);
				regHold[3] = new Rectangle2D(0,0,5,5);
			}
			int count = WorldWrapper.getOverlapRegions(reg, vehicle.world.worldBounds, regHold);
			
			
			for(int i = 0; i < count; i++){
				vehicle.world.quadTree.getPointsInRegion(regHold[i], neighbors);
			}
		}
	}
	
	/**
	 * This function will get all visible vehicles with
	 * the region pos+-neighborDetectionRadius
	 * This will only happen on the first call or 
	 * until clearVisibleVehicles is called
	 * @return
	 */
	public HashSet<Vehicle> getVisibleVehicles(){
		if(!visibleVehiclesFound){
			visibleVehicles.clear();
			//Set Detection rectangle
			neighborDetectionRegion.x1=vehicle.pos.x-neighborDetectionRadius;
			neighborDetectionRegion.y1= vehicle.pos.y-neighborDetectionRadius; 
			neighborDetectionRegion.x2=vehicle.pos.x+neighborDetectionRadius;
			neighborDetectionRegion.y2=vehicle.pos.y+neighborDetectionRadius;
			neighborDetectionRegion.ensureOrder();
			calculateNeighbobors(vehicle,visibleVehicles, neighborDetectionRegion);

			//Remove self
			visibleVehicles.remove(vehicle);
			
			visibleVehiclesFound = true;
		}
		
		return visibleVehicles;
	}
	
	/**
	 * Reset the visible vehicles 
	 * good for freeing memory and means 
	 * that the next call to getVisibleVehicles
	 * will result in a new calculation of visible vehicles
	 */
	private void clearVisibleVehicles(){
		if(visibleVehiclesFound){
			visibleVehiclesFound = false;
			visibleVehicles.clear();
		}
	}
	
	public List<Vector2D> getFeelers(){
		if(!localFeelersFound){
			if(localFeelers == null){
				localFeelers = WallAvoidance.createFeelers(feelerCount, feelerLength, feelerFOV);
				worldFeelers = WallAvoidance.createFeelers(feelerCount, feelerLength, feelerFOV);
			}
			Transformations.WorldTransform(localFeelers, vehicle.pos, vehicle.velHead, vehicle.velSide, worldFeelers);
			localFeelersFound = true;
		}
		return worldFeelers;
	}
	
	public void clearFeelers(){
		localFeelersFound = false;
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
		
		//Clear Properties that should only be used on this itteration
		clearVisibleVehicles();
		clearFeelers();
	}
}
