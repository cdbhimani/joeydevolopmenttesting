package com.joey.aitesting.game.steering;

import java.util.HashSet;
import java.util.List;

import com.joey.aitesting.game.Deceleration;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.entities.WaypointPath;
import com.joey.aitesting.game.maths.Transformations;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.shapes.WorldWrapper;
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

public class SteeringBehaviors {
	
	Vehicle vehicle;

	public boolean drawBehaviour = false;
	//Hide
	public boolean useHide = false;
	public float hideWeight = 1f;
	public Vehicle hideVehicle;
	
	//Offset Pursuit
	public boolean useOffsetPursuit = false;
	public float offsetPursuitWeight = 1f;
	public Vehicle offsetPursuitVehicle;
	public Vector2D offsetPursuitOffset;
	
	//Interpose Variables
	public boolean useInterpose = false;
	public float interposeWeight = 1f;
	public Vehicle interposeA;
	public Vehicle interposeB;
	
	//Waypoint Path
	public boolean useFollowPath = false;
	public float followPathWeight = 1;
	public float followPathWaypointDistance = 10;
	public WaypointPath path;
	
	
	// Wall Avoidance
	public boolean useWallAvoidance = false;
	public float wallAvoidanceWeight = 1;
	
	//Feelers
	public int feelerCount = 0;
	public float feelerLength = 1;
	public float feelerFOV = 0.5f;
	public List<Vector2D> localFeelers;
	public List<Vector2D> worldFeelers;
	
	//Fleeing Paramaters
	public float fleeWeight = 1;
	public boolean useFlee = false;
	public boolean useFleePanic = false;
	public float fleePanicDistance;
	public Vector2D fleePos;
	
	//Seek Parameters
	public float seekWeight = 1;
	public boolean useSeek = false;
	public Vector2D seekPos;

	//Arrive Parameters
	public float arriveWeight = 1;
	public boolean useArrive = false;
	public Vector2D arrivePos;
	
	//Persuit Parameters
	public float persuitWeight = 1;
	public boolean usePersuit = false;
	public Vehicle persuitVehicle;
	
	//Evade Parameters
	public float evadeWeight = 1;
	public float evadePanicDistance = 30;
	public boolean useEvade = false;
	public boolean useEvadePanic = false;
	public Vehicle evadeVehicle;

	//Wander Parameters
	public float wanderWeight = 1;
	public boolean useWander = false;
	public float wanderRadius;
	public float wanderDistance;
	public float wanderJitter;
	public Vector2D wanderVector;
	
	//Flocking Parameters	
	public float cohesionWeight = 1f;
	public boolean useCohesion = false;
	
	public float seperationWeight = 1f;
	public boolean useSeperation = false;
	
	public float alignmentWeight = 1f;
	public boolean useAlignment = false;
	
	public float neighborRadius = 100;
	HashSet<Vehicle> neighbors = new HashSet<Vehicle>();
	Rectangle2D regHold[] = null;//For search around edge of world
	
	public float obstacleAvoidanceWeight =1f;
	public boolean useObstacleAvoidance = false;
	public float obstacleSearchBoxDistance = 100;
	
	public SteeringBehaviors(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public boolean isSpacePartitioningOn() {
		return true;
	}

	public void calculateNeighbobors(Vehicle vehicle, HashSet<Vehicle> neighbors, Rectangle2D reg){
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
	
	public Vector2D calculate(float updateTime) {
		Vector2D rst = new Vector2D();
		
		
		Vector2D hold = new Vector2D();
		Vector2D point = new Vector2D();

		if(useCohesion||useAlignment||useSeperation){
			neighbors.clear();
			Rectangle2D reg = new Rectangle2D(
					vehicle.pos.x-neighborRadius, vehicle.pos.y-neighborRadius, 
					vehicle.pos.x+neighborRadius, vehicle.pos.y+neighborRadius);
			reg.ensureOrder();
			calculateNeighbobors(vehicle,neighbors, reg);
			//Remove self
			neighbors.remove(vehicle);
			//System.out.println("Finding Neighbours: "+neighbors.size());
		}else{
			neighbors.clear();
		}
		
		
		if(useWallAvoidance){
			if(localFeelers == null){
				localFeelers = WallAvoidance.createFeelers(feelerCount, feelerLength, feelerFOV);
				worldFeelers = WallAvoidance.createFeelers(feelerCount, feelerLength, feelerFOV);
			}
			Transformations.WorldTransform(localFeelers, vehicle.pos, vehicle.velHead, vehicle.velSide, worldFeelers);
			
			hold.setLocation(0,0);
			WallAvoidance.WallAvoidance(vehicle, worldFeelers, vehicle.world.getWalls(), hold);
			hold.scale(wallAvoidanceWeight);
			if(hold.lengthSq() > 1){
				return hold;
			}
			rst.add(hold);
		}
		
		if(useObstacleAvoidance){
			hold.setLocation(0,0);
			ObstacleAvoidance.obstacleAvoidance(vehicle,vehicle.world.getObstacles(), obstacleSearchBoxDistance,hold);
			hold.scale(obstacleAvoidanceWeight);
			
			rst.add(hold);		
			if(hold.lengthSq() > 1){
				return hold;
			}
			//System.out.println("Cohesion : "+hold);
		}
		
		
		if(useHide){
			hold.setLocation(0,0);
			Hide.hide(vehicle, hideVehicle, vehicle.getWorld().getObstacles(), hold);
			hold.scale(offsetPursuitWeight);
			rst.add(hold);
		}
		
		if(useOffsetPursuit){
			hold.setLocation(0,0);
			OffsetPursuit.offsetPursuit(vehicle, offsetPursuitVehicle, offsetPursuitOffset, hold);
			hold.scale(offsetPursuitWeight);
			rst.add(hold);
		}
		if(useInterpose){
			hold.setLocation(0,0);
			Interpose.interpose(vehicle, interposeA, interposeB, hold);
			hold.scale(interposeWeight);
			rst.add(hold);
		}
		if(useFollowPath){
			hold.setLocation(0,0);
			FollowPath.PathFollow(vehicle, path, followPathWaypointDistance, hold);
			hold.scale(followPathWeight);
			rst.add(hold);
		}
		if (useSeek) {
			hold.setLocation(0,0);
			WorldWrapper.moveToClosest(vehicle.pos, seekPos, point,
					vehicle.world.worldBounds);
			Seek.seek(vehicle, point, hold);
			hold.scale(seekWeight);
			rst.add(hold);
			
			//System.out.println("Seek : "+rst);
		}

		if (useFlee) {
			hold.setLocation(0,0);
			WorldWrapper.moveToClosest(vehicle.pos, fleePos, point,
					vehicle.world.worldBounds);
			if(useFleePanic){
				Flee.flee(vehicle, point, fleePanicDistance, hold);
			}else{
				Flee.flee(vehicle, point, hold);
			}
			hold.scale(fleeWeight);
			rst.add(hold);
			
			//System.out.println("Flee : "+rst);
		}

		if (useArrive) {
			hold.setLocation(0,0);
			WorldWrapper.moveToClosest(vehicle.pos, arrivePos, point,
					vehicle.world.worldBounds);
			Arrive.arrive(vehicle,point,Deceleration.FAST,Arrive.DecelerationTweaker,hold);
			hold.scale(arriveWeight);
			rst.add(hold);
			
			//System.out.println("Arrive : "+rst);
		}

		if (usePersuit) {
			hold.setLocation(0,0);
			Persuit.persuit(vehicle, persuitVehicle, hold);
			hold.scale(persuitWeight);
			rst.add(hold);
			
			//System.out.println("Persuit : "+rst);
		}
		
		if (useEvade) {
			hold.setLocation(0,0);
			if(useEvadePanic){
				Evade.evade(vehicle, evadeVehicle,evadePanicDistance, hold);
			}else{
				Evade.evade(vehicle, evadeVehicle, hold);	
			}
			
			hold.scale(evadeWeight);
			rst.add(hold);
			
			//System.out.println("Evade : "+rst);
		}
		
		if(useWander){
			hold.setLocation(0,0);
			if(wanderVector == null){
				wanderVector = new Vector2D(vehicle.vel);
				wanderVector.normalise();
			}
			Wander.wander(vehicle,updateTime, wanderJitter, wanderRadius, wanderDistance, wanderVector, hold);
			
			hold.scale(wanderWeight);
			rst.add(hold);
			
			//System.out.println("Wander : "+rst);
		}

		if(useSeperation){
			hold.setLocation(0,0);
			Seperation.seperation(vehicle, neighbors, hold);
			hold.scale(seperationWeight);
			rst.add(hold);
			
			//System.out.println("Seperation : "+hold);
		}
		if(useAlignment){
			hold.setLocation(0,0);
			Alignment.alignment(vehicle, neighbors, hold);
			hold.scale(alignmentWeight);
			rst.add(hold);
			
			//System.out.println("Alignment : "+hold);
		}
		if(useCohesion){
			hold.setLocation(0,0);
			Cohesion.cohesion(vehicle, neighbors, hold);
			hold.scale(cohesionWeight);
			rst.add(hold);
			
			
			//System.out.println("Cohesion : "+hold);
		}
		
		
		
		if (rst.lengthSq() > vehicle.maxForce * vehicle.maxForce) {
//			System.out.println("Rescale : "+rst);
			rst.normalise();
			rst.scale(vehicle.maxForce);
		}
		
//		System.out.println("End : "+rst);
		return rst;
	}

	public static float turnaroundTime(Vehicle pAgent, Vector2D TargetPos, float coefficient) {
		Vector2D toTarget = new Vector2D(TargetPos);
		toTarget.subtract(pAgent.pos);
		float dot = pAgent.velHead.dot(toTarget);
		return (dot - 1.0f) * -coefficient;
	}
		
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public boolean isUseSeek() {
		return useSeek;
	}

	public void setUseSeek(boolean useSeek) {
		this.useSeek = useSeek;
	}

	public Vector2D getSeekPos() {
		return seekPos;
	}

	public void setSeekPos(Vector2D seekPos) {
		this.seekPos = seekPos;
	}
}
