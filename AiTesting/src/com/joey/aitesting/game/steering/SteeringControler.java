package com.joey.aitesting.game.steering;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.joey.aitesting.game.entities.Obstacle;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.entities.Wall2D;
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
import com.joey.aitesting.game.steering.behaviors.FollowPath;
import com.joey.aitesting.game.steering.behaviors.Hide;
import com.joey.aitesting.game.steering.behaviors.Interpose;
import com.joey.aitesting.game.steering.behaviors.ObstacleAvoidance;
import com.joey.aitesting.game.steering.behaviors.OffsetPursuit;
import com.joey.aitesting.game.steering.behaviors.Persuit;
import com.joey.aitesting.game.steering.behaviors.Seek;
import com.joey.aitesting.game.steering.behaviors.Seperation;
import com.joey.aitesting.game.steering.behaviors.WallAvoidance;
import com.joey.aitesting.game.steering.behaviors.Wander;

public class SteeringControler {
	public boolean drawBehaviors = false;

	public Vehicle vehicle;

	public Flee flee;
	public Seek seek;
	public Arrive arrive;

	public Persuit persult;
	public Evade evade;
	public Hide hide;

	public Interpose interpose;
	public FollowPath followPath;
	public Wander wander;
	public OffsetPursuit offsetPursuit;

	public Alignment alignment;
	public Seperation seperation;
	public Cohesion cohesion;

	public ObstacleAvoidance obstacleAvoidance;
	public WallAvoidance wallAvoidance;

	/**
	 * These hold information about the visible vehicles
	 */
	public float neighborDetectionRadius = 100;
	private boolean visibleVehiclesFound = false;
	public Rectangle2D neighborDetectionRegion = new Rectangle2D();
	Rectangle2D regHold[] = null;// For search around edge of world
	private HashSet<Vehicle> visibleVehicles = new HashSet<Vehicle>();

	/**
	 * These variables hold informaiton about the vehicle "Feelers"
	 * 
	 * @param veh
	 */
	private boolean feelersFound = false;
	public int feelerCount = 0;
	public float feelerLength = 1;
	public float feelerFOV = 0.5f;
	List<Vector2D> localFeelers;
	List<Vector2D> worldFeelers;

	/**
	 * Holding walls
	 */
	private boolean wallsFound = false;

	private Vector2D force = new Vector2D();
	
	public SteeringControler(Vehicle veh) {
		this.vehicle = veh;
		createBehaviours();
	}

	private void createBehaviours() {
		flee = new Flee(this);
		seek = new Seek(this);
		arrive = new Arrive(this);

		persult = new Persuit(this);
		evade = new Evade(this);
		hide = new Hide(this);

		interpose = new Interpose(this);
		followPath = new FollowPath(this);
		wander = new Wander(this);
		offsetPursuit = new OffsetPursuit(this);

		alignment = new Alignment(this);
		seperation = new Seperation(this);
		cohesion = new Cohesion(this);

		obstacleAvoidance = new ObstacleAvoidance(this);
		wallAvoidance = new WallAvoidance(this);
	}

	private void calculateNeighbobors(Vehicle vehicle,HashSet<Vehicle> neighbors, Rectangle2D reg) {
		if (vehicle.world.worldBounds.contains(reg)) {
			vehicle.world.quadTree.getPointsInRegion(reg, neighbors);
		} else {
			// If not fully contained in the world search off wrapped world
			if (regHold == null) {
				regHold = new Rectangle2D[4];
				regHold[0] = new Rectangle2D(0, 0, 5, 5);
				regHold[1] = new Rectangle2D(0, 0, 5, 5);
				regHold[2] = new Rectangle2D(0, 0, 5, 5);
				regHold[3] = new Rectangle2D(0, 0, 5, 5);
			}
			int count = WorldWrapper.getOverlapRegions(reg,
					vehicle.world.worldBounds, regHold);

			for (int i = 0; i < count; i++) {
				vehicle.world.quadTree.getPointsInRegion(regHold[i], neighbors);
			}
		}
	}

	/**
	 * This function will get all visible vehicles with the region
	 * pos+-neighborDetectionRadius This will only happen on the first call or
	 * until clearVisibleVehicles is called
	 * 
	 * @return
	 */
	public HashSet<Vehicle> getVisibleVehicles() {
		if (!visibleVehiclesFound) {
			// Set Detection rectangle
			neighborDetectionRegion.x1 = vehicle.pos.x
					- neighborDetectionRadius;
			neighborDetectionRegion.y1 = vehicle.pos.y
					- neighborDetectionRadius;
			neighborDetectionRegion.x2 = vehicle.pos.x
					+ neighborDetectionRadius;
			neighborDetectionRegion.y2 = vehicle.pos.y
					+ neighborDetectionRadius;
			neighborDetectionRegion.ensureOrder();
			
			calculateNeighbobors(vehicle, visibleVehicles,
					neighborDetectionRegion);
			// Remove self
			//visibleVehicles.remove(vehicle);

			visibleVehiclesFound = true;
		}
		return visibleVehicles;
	}

	/**
	 * Reset the visible vehicles good for freeing memory and means that the
	 * next call to getVisibleVehicles will result in a new calculation of
	 * visible vehicles
	 */
	private void clearVisibleVehicles() {
		if (visibleVehiclesFound) {
			visibleVehiclesFound = false;
			visibleVehicles.clear();
		}
	}

	public List<Vector2D> getFeelers() {
		if (!feelersFound) {
			if (localFeelers == null) {
				localFeelers = WallAvoidance.createFeelers(feelerCount,
						feelerLength, feelerFOV);
				worldFeelers = WallAvoidance.createFeelers(feelerCount,
						feelerLength, feelerFOV);
			}
			Transformations.WorldTransform(localFeelers, vehicle.pos,
					vehicle.velHead, vehicle.velSide, worldFeelers);
			feelersFound = true;
		}
		return worldFeelers;
	}

	public void clearFeelers() {
		feelersFound = false;
	}

	public void clearVisibleObstacles() {
	}

	public List<Obstacle> getVisibleObstacles() {
		return vehicle.world.getObstacles();
	}

	public List<Wall2D> getVisibleWalls() {
		return vehicle.world.getWalls();
	}

	public void clearVisibleWalls() {
		wallsFound = false;
	}

	private void updateBehavior(AbstractBehavior behave) {
		behave.update();
//		if(vehicle.getId() != 0){
//			System.out.println(vehicle.getId()+" : "+behave.name+" : "+behave.force);
//		}
	}

	public void calculate(Vector2D steeringForce) {
		if(Math.random() < 0.6){
			update();
		}
		
		steeringForce.setLocation(force);
	}
	
	public void update(){
		// Clear Properties that should only be used on this itteration
		clearVisibleVehicles();
		clearFeelers();
		clearVisibleObstacles();
		clearVisibleWalls();

		force.setLocation(0, 0);
		calculateWeightSum(force);
	}

	private void calculateWeightSum(Vector2D steeringForce) {
		if (wallAvoidance.enabled) {
			updateBehavior(wallAvoidance);

			if (wallAvoidance.force.lengthSq() > 1) {
				steeringForce.setLocation(wallAvoidance.force);
				return;
			}
		}

		if (obstacleAvoidance.enabled) {
			updateBehavior(obstacleAvoidance);
			steeringForce.add(obstacleAvoidance.force);
		}

		// Calculate Flee
		if (flee.enabled) {
			updateBehavior(flee);
			steeringForce.add(flee.force);
		}
		if (seek.enabled) {
			updateBehavior(seek);
			steeringForce.add(seek.force);
		}
		if (arrive.enabled) {
			updateBehavior(arrive);
			steeringForce.add(arrive.force);
		}
		if (persult.enabled) {
			updateBehavior(persult);
			steeringForce.add(persult.force);
		}
		if (evade.enabled) {
			updateBehavior(evade);
			steeringForce.add(evade.force);
		}
		if (hide.enabled) {
			updateBehavior(hide);
			steeringForce.add(hide.force);
		}
		if (interpose.enabled) {
			updateBehavior(interpose);
			steeringForce.add(interpose.force);
		}
		if (followPath.enabled) {
			updateBehavior(followPath);
			steeringForce.add(followPath.force);
		}
		if (wander.enabled) {
			updateBehavior(wander);
			steeringForce.add(wander.force);
		}
		if (offsetPursuit.enabled) {
			updateBehavior(offsetPursuit);
			steeringForce.add(offsetPursuit.force);
		}
		if (alignment.enabled) {
			updateBehavior(alignment);
			steeringForce.add(alignment.force);
		}
		if (seperation.enabled) {
			updateBehavior(seperation);
			steeringForce.add(seperation.force);
		}
		if (cohesion.enabled) {
			updateBehavior(cohesion);
			steeringForce.add(cohesion.force);
		}

		// Limit length to max size
		if (steeringForce.lengthSq() > vehicle.maxForce * vehicle.maxForce) {
			steeringForce.normalise();
			steeringForce.scale(vehicle.maxForce);
		}
	}

}
