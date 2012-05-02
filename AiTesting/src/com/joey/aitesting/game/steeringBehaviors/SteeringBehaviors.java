package com.joey.aitesting.game.steeringBehaviors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.entities.Obstacle;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.maths.Transformations;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.joey.aitesting.game.shapes.WorldWrapper;

public class SteeringBehaviors {
	public static final float DecelerationTweaker = .3f;
	Vehicle vehicle;

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
	public boolean useEvade = false;
	public Vehicle evadeVehicle;

	//Wander Parameters
	public float wanderWeight = 1;
	public boolean useWander = false;
	public float wanderRadius;
	public float wanderDistance;
	public float wanderJitter;
	public Vector2D wanderVector;
	
	//Flocking Parameters	
	public float cohesionWeight = 1;
	public boolean useCohesion = false;
	
	public float seperationWeight = 1;
	public boolean useSeperation = false;
	
	public float alignmentWeight = 1;
	public boolean useAlignment = false;
	
	public float neighborRadius = 100;
	HashSet<Vehicle> neighbors = new HashSet<Vehicle>();
	Rectangle2D regHold[] = null;//For search around edge of world
	
	public boolean useObstacleAvoidance = false;
	public float obstacleSearchBoxDistance = 100;
	
	public SteeringBehaviors(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public boolean isSpacePartitioningOn() {
		return true;
	}

	/**
	 * Determins the closest position of P2 (assuming
	 * its on a wrapped world given by worldBounds)
	 * 
	 *  This is required for following entites
	 *  of the edge of the world
	 * @param p1 - The entities position
	 * @param p2 - The point in the world
	 * @param rst - The point mapped on the world
	 * @param worldBounds - The world rectangle
	 */
	public static void moveToClosest(Vector2D p1, Vector2D p2, Vector2D rst,
			Rectangle2D worldBounds) {
		Vector2D.subtract(p1, p2, rst);

		if (Math.abs(rst.x) > worldBounds.getWidth() / 2) {
			if (rst.x > 0) {
				rst.x = p2.x + worldBounds.getWidth();
			} else {
				rst.x = p2.x - worldBounds.getWidth();
			}
		} else {
			rst.x = p2.x;
		}

		if (Math.abs(rst.y) > worldBounds.getHeight() / 2) {
			if (rst.y > 0) {
				rst.y = p2.y + worldBounds.getHeight();
			} else {
				rst.y = p2.y - worldBounds.getHeight();
			}
		} else {
			rst.y = p2.y;
		}
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
		int count = 0;
		
		Vector2D hold = new Vector2D();
		Vector2D point = new Vector2D();

		if(useCohesion||useAlignment||useSeperation){
			neighbors.clear();
			Rectangle2D reg = new Rectangle2D(
					vehicle.pos.x-neighborRadius, vehicle.pos.y-neighborRadius, 
					vehicle.pos.x+neighborRadius, vehicle.pos.y+neighborRadius);
			calculateNeighbobors(vehicle,neighbors, reg);
			//Remove self
			neighbors.remove(vehicle);
			//System.out.println("Finding Neighbours: "+neighbors.size());
		}
		
		if (useSeek) {
			moveToClosest(vehicle.pos, seekPos, point,
					vehicle.world.worldBounds);
			seek(point, vehicle, hold);
			count++;
			rst.add(hold);
			
			//System.out.println("Seek : "+rst);
		}

		if (useFlee) {
			moveToClosest(vehicle.pos, fleePos, point,
					vehicle.world.worldBounds);
			if(useFleePanic){
				flee(point, vehicle, fleePanicDistance, hold);
			}else{
				flee(point, vehicle, hold);
			}
			count++;
			rst.add(hold);
			
			//System.out.println("Flee : "+rst);
		}

		if (useArrive) {
			moveToClosest(vehicle.pos, arrivePos, point,
					vehicle.world.worldBounds);
			arrive(point, vehicle, 1, hold);
			count++;
			rst.add(hold);
			
			//System.out.println("Arrive : "+rst);
		}

		if (usePersuit) {
			persuit(vehicle, persuitVehicle, hold);
			count++;
			rst.add(hold);
			
			//System.out.println("Persuit : "+rst);
		}
		
		if (useEvade) {
			evade(vehicle, evadeVehicle, hold);
			count++;
			rst.add(hold);
			
			//System.out.println("Evade : "+rst);
		}
		
		if(useWander){
			if(wanderVector == null){
				wanderVector = new Vector2D(vehicle.vel);
				wanderVector.normalise();
			}
			wander(vehicle,updateTime, wanderJitter, wanderRadius, wanderDistance, wanderVector, hold);
			
			count++;
			rst.add(hold);
			
			//System.out.println("Wander : "+rst);
		}

		if(useSeperation){
			seperation(vehicle, neighbors, hold);
			count++;
			rst.add(hold);
			
			//System.out.println("Seperation : "+rst);
		}
		if(useAlignment){
			alignment(vehicle, neighbors, hold);
			count++;
			rst.add(hold);
			
			//System.out.println("Alignment : "+rst);
		}
		if(useCohesion){
			cohesion(vehicle, neighbors, hold);
			count++;
			rst.add(hold);
			
			
			//System.out.println("Cohesion : "+rst);
		}
		
		if(useObstacleAvoidance){
			obstacleAvoidance(vehicle.world.getObstacles(), vehicle,obstacleSearchBoxDistance,hold);
			count++;
			rst.add(hold);
			
			
			//System.out.println("Cohesion : "+rst);
		}
		
		//System.out.println("Scale Testing : "+rst);
		if(count > 0){
			rst.scale(1f/count);
		}
		if (rst.lengthSq() > vehicle.maxForce * vehicle.maxForce) {
			rst.normalise();
			rst.scale(vehicle.maxForce);
		}
		
		//System.out.println("End : "+rst);
		return rst;
	}

	public static void alignment(Vehicle vehicle, HashSet<Vehicle> neighbors, Vector2D rst){
		float count = 0;
		
		rst.x = 0;
		rst.y = 0;
		
		for(Vehicle other : neighbors){
			rst.add(other.vel);
			count++;
		}
		if(count >0){
			rst.scale(1/count);
		}
		rst.subtract(vehicle.velHead);
	}
	
	public static void seperation(Vehicle vehicle, HashSet<Vehicle> neighbors, Vector2D rst){
		Vector2D hold = new Vector2D();
		float length = 0;
		
		rst.x = 0;
		rst.y = 0;
		
		for(Vehicle other : neighbors){
			Vector2D.subtract(vehicle.pos,other.pos, hold);
			length = hold.length();
			hold.normalise();
			
			if(length > 0){
				length = 0.01f;
			}
			rst.x+= hold.x/length;
			rst.y+= hold.y/length;
		}
	}
	public static void cohesion(Vehicle vehicle, HashSet<Vehicle> neighbors, Vector2D rst){
		//first find the center of mass of all the agents
		Vector2D hold = new Vector2D();
		int NeighborCount = 0;
		//iterate through the neighbors and sum up all the position vectors
		for(Vehicle other : neighbors){
			hold.add(other.pos);
			NeighborCount++;
		}
		
		if (NeighborCount > 0)
		{
			//the center of mass is the average of the sum of positions
			hold.scale(1f/NeighborCount);			
			seek(hold, vehicle, rst);
		}
	}
	public static void seek(Vector2D targetPos, Vehicle veh, Vector2D rst) {
		Vector2D.subtract(targetPos, veh.pos, rst);
		rst.normalise();
		rst.scale(veh.maxSpeed);

		rst.subtract(veh.vel);
	}

	public static void flee(Vector2D targetPos, Vehicle veh, Vector2D rst) {
		Vector2D.subtract(veh.pos, targetPos, rst);
		rst.normalise();
		rst.scale(veh.maxSpeed);

		rst.subtract(veh.vel);
	}

	public static void flee(Vector2D targetPos, Vehicle veh,
			float fleeDistance, Vector2D rst) {
		Vector2D.subtract(veh.pos, targetPos, rst);
		if (rst.lengthSq() > fleeDistance * fleeDistance) {
			rst.x = 0;
			rst.y = 0;
			return;
		}
		rst.normalise();
		rst.scale(veh.maxSpeed);

		rst.subtract(veh.vel);
	}

	public static void arrive(Vector2D TargetPos, Vehicle veh,	int deceleration, Vector2D rst) {
		Vector2D.subtract(TargetPos, veh.pos, rst);
		float dist = rst.length();
		if (dist > 0) {
			float speed = dist / (deceleration * DecelerationTweaker);
			speed = Math.min(speed, veh.getMaxSpeed());
			rst.scale(speed / dist);
			rst.subtract(veh.vel);
		} else {
			rst.setLocation(0, 0);
		}
	}

	public static void persuit(Vehicle vehicle, Vehicle persuit, Vector2D rst) {
		Vector2D.subtract(persuit.pos, vehicle.pos, rst);
		float RelativeHeading = vehicle.velHead.dot(persuit.velHead);
		if ((rst.dot(vehicle.velHead) > 0) && (RelativeHeading < -0.95)) { 
			Vector2D point = new Vector2D();
			moveToClosest(vehicle.pos, persuit.pos, point,
					vehicle.world.worldBounds);
			seek(point, vehicle, rst);
			return;
		}
		float LookAheadTime = rst.length()
				/ (vehicle.maxSpeed + persuit.vel.length());
		Vector2D vec = new Vector2D(persuit.vel);
		vec.scale(LookAheadTime);
		vec.add(persuit.pos);

		Vector2D point = new Vector2D();
		moveToClosest(vehicle.pos, vec, point, vehicle.world.worldBounds);
		seek(point, vehicle, rst);
	}

	public static void evade(Vehicle vehicle, Vehicle evade, Vector2D rst) {
		Vector2D holder = new Vector2D();
		Vector2D.subtract(evade.pos ,vehicle.pos, rst);
		float LookAheadTime = holder.length()/(vehicle.maxSpeed + evade.vel.length());
		
		holder.setLocation(evade.vel);
		holder.scale(LookAheadTime);
		holder.add(evade.pos);
		
		Vector2D point = new Vector2D();
		moveToClosest(vehicle.pos, holder, point, vehicle.world.worldBounds);
		flee(point, vehicle, rst);
	}
	public static float turnaroundTime(Vehicle pAgent, Vector2D TargetPos, float coefficient) {
		Vector2D toTarget = new Vector2D(TargetPos);
		toTarget.subtract(pAgent.pos);
		float dot = pAgent.velHead.dot(toTarget);
		return (dot - 1.0f) * -coefficient;
	}
	
	public static void wander(Vehicle vehicle,float updateTime, float wanderJitter, 
			float wanderRadius,float wanderDistance, Vector2D wanderVector, 
			Vector2D rst){
		
		
		
//		  //this behavior is dependent on the update rate, so this line must
//		  //be included when using time independent framerate.
//		  double JitterThisTimeSlice = wanderJitter * updateTime*10;
//
//		  //first, add a small random vector to the target's position
//		//first, add a small random vector to the target's position
//		  wanderVector.x += (1-2*Math.random()) * JitterThisTimeSlice;
//		  wanderVector.y += (1-2*Math.random()) * JitterThisTimeSlice;
//
//		  //reproject this new vector back on to a unit circle
//		  wanderVector.normalise();
//
//		  //increase the length of the vector to the same as the radius
//		  //of the wander circle
//		  wanderVector.scale(wanderRadius);
//
//		  //move the target into a position WanderDist in front of the agent
//		  Vector2D target = new Vector2D(wanderVector);
//		  target.x +=wanderDistance;
//
//		  //project the target into world space
//		  Transformations.PointToWorldSpace(target,
//		                                       vehicle.velHead,
//		                                       vehicle.velSide, 
//		                                       vehicle.pos, rst);
//
//		  //and steer towards it
//		  rst.subtract(vehicle.pos);
		
		
		
		  //this behavior is dependent on the update rate, so this line must
		  //be included when using time independent framerate.
		  double JitterThisTimeSlice = wanderJitter;

		  //first, add a small random vector to the target's position
		  wanderVector.x += (1-2*Math.random()) * JitterThisTimeSlice;
		  wanderVector.y += (1-2*Math.random()) * JitterThisTimeSlice;

		  //reproject this new vec2tor back on to a unit circle
		  wanderVector.normalise();
		  wanderVector.scale(wanderRadius);

		  //move the target into a position WanderDist in front of the agent
		  Vector2D wanderPos = new Vector2D(vehicle.vel);
		  wanderPos.normalise();
		  wanderPos.scale(wanderDistance);
		  wanderPos.add(vehicle.pos);
		  wanderPos.add(wanderVector);
		  
		  seek(wanderPos, vehicle, rst);
	}
	
	//---------------------- ObstacleAvoidance -------------------------------
	//
	//  Given a vector of CObstacles, this method returns a steering force
	//  that will prevent the agent colliding with the closest obstacle
	//------------------------------------------------------------------------
	public static void obstacleAvoidance(List<Obstacle> obstacles, Vehicle m_pVehicle, float minDetectionBox, Vector2D rst)
	{
	  //the detection box length is proportional to the agent's velocity
	  float m_dDBoxLength = minDetectionBox + 
	                  (m_pVehicle.getVel().length()/m_pVehicle.maxSpeed) *
	                  minDetectionBox;

	  //tag all obstacles within range of the box for processing
//	  m_pVehicle->World()->TagObstaclesWithinViewRange(m_pVehicle, m_dDBoxLength);

	  //this will keep track of the closest intersecting obstacle (CIB)
	  BaseGameEntity ClosestIntersectingObstacle = null;
	 
	  //this will be used to track the distance to the CIB
	  float DistToClosestIP = Float.MAX_VALUE;

	  //this will record the transformed local coordinates of the CIB
	  Vector2D LocalPosOfClosestObstacle = null;

	
      for(BaseGameEntity curOb : obstacles ){
	    
	      //calculate this obstacle's position in local space
	      Vector2D LocalPos = new Vector2D();
	      
	      Transformations.PointToLocalSpace((curOb).pos,
	                                             m_pVehicle.velHead,
	                                             m_pVehicle.velSide,
	                                             m_pVehicle.pos,
	                                             LocalPos);

	      //if the local position has a negative x value then it must lay
	      //behind the agent. (in which case it can be ignored)
	      if (LocalPos.x >= 0)
	      {
	        //if the distance from the x axis to the object's position is less
	        //than its radius + half the width of the detection box then there
	        //is a potential intersection.
	        double ExpandedRadius = (curOb).radius + m_pVehicle.radius;

	        if (Math.abs(LocalPos.y) < ExpandedRadius)
	        {
	          //now to do a line/circle intersection test. The center of the 
	          //circle is represented by (cX, cY). The intersection points are 
	          //given by the formula x = cX +/-sqrt(r^2-cY^2) for y=0. 
	          //We only need to look at the smallest positive value of x because
	          //that will be the closest point of intersection.
	          float cX = LocalPos.x;
	          float cY = LocalPos.y;
	          
	          //we only need to calculate the sqrt part of the above equation once
	          float SqrtPart = (float)Math.sqrt(ExpandedRadius*ExpandedRadius - cY*cY);

	          float ip = cX - SqrtPart;

	          if (ip <= 0.0)
	          {
	            ip = cX + SqrtPart;
	          }

	          //test to see if this is the closest so far. If it is keep a
	          //record of the obstacle and its local coordinates
	          if (ip < DistToClosestIP)
	          {
	            DistToClosestIP = ip;

	            ClosestIntersectingObstacle = curOb;

	            LocalPosOfClosestObstacle = LocalPos;
	          }         
	        }
	      }
	  }

	  //if we have found an intersecting obstacle, calculate a steering 
	  //force away from it
	  Vector2D SteeringForce = new Vector2D();

	  if (ClosestIntersectingObstacle!= null)
	  {
	    //the closer the agent is to an object, the stronger the 
	    //steering force should be
	    float multiplier = 1.0f + (m_dDBoxLength - LocalPosOfClosestObstacle.x) /
	                        m_dDBoxLength;

	    //calculate the lateral force
	    SteeringForce.y = (ClosestIntersectingObstacle.radius-
	                       LocalPosOfClosestObstacle.y)  * multiplier;   

	    //apply a braking force proportional to the obstacles distance from
	    //the vehicle. 
	    float BrakingWeight = 0.2f;

	    SteeringForce.x = (ClosestIntersectingObstacle.radius - 
	                       LocalPosOfClosestObstacle.x) * 
	                       BrakingWeight;
	  }

	  Transformations.VectorToWorldSpace(SteeringForce, m_pVehicle.velHead, m_pVehicle.velSide, rst);
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

//class SumMethod{
//	Vector2D rst = new Ve;
//	public SumMethod(){
//		
//	}
//}
