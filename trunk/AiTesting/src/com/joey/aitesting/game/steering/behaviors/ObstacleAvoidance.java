package com.joey.aitesting.game.steering.behaviors;

import java.util.List;

import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.entities.Obstacle;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.maths.Transformations;
import com.joey.aitesting.game.shapes.Vector2D;

public class ObstacleAvoidance extends AbstractBehavior{

	public ObstacleAvoidance(Vehicle veh) {
		super(veh);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void calculate(Vector2D force) {
		// TODO Auto-generated method stub
		
	}

	//---------------------- ObstacleAvoidance -------------------------------
		//
		//  Given a vector of CObstacles, this method returns a steering force
		//  that will prevent the agent colliding with the closest obstacle
		//------------------------------------------------------------------------
		public static void obstacleAvoidance(Vehicle veh, List<Obstacle> obstacles, float minDetectionBox, Vector2D rst)
		{
		  //the detection box length is proportional to the agent's velocity
		  float m_dDBoxLength = minDetectionBox + 
		                  (veh.getVel().length()/veh.maxSpeed) *
		                  minDetectionBox;

		  //tag all obstacles within range of the box for processing
//		  m_pVehicle->World()->TagObstaclesWithinViewRange(m_pVehicle, m_dDBoxLength);

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
		                                             veh.velHead,
		                                             veh.velSide,
		                                             veh.pos,
		                                             LocalPos);

		      //if the local position has a negative x value then it must lay
		      //behind the agent. (in which case it can be ignored)
		      if (LocalPos.x >= 0)
		      {
		        //if the distance from the x axis to the object's position is less
		        //than its radius + half the width of the detection box then there
		        //is a potential intersection.
		        double ExpandedRadius = (curOb).radius + veh.radius;

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

		  Transformations.VectorToWorldSpace(SteeringForce, veh.velHead, veh.velSide, rst);
		}

}
