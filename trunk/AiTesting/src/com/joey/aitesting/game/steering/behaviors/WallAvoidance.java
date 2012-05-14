package com.joey.aitesting.game.steering.behaviors;

import java.util.ArrayList;
import java.util.List;

import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.entities.Wall2D;
import com.joey.aitesting.game.maths.Geometry;
import com.joey.aitesting.game.maths.Transformations;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steering.SteeringControler;

public class WallAvoidance extends AbstractBehavior{

	public WallAvoidance(SteeringControler steering) {
		super("WallAvoidance",steering);
	}

	@Override
	public void calculate(Vector2D force) {
		WallAvoidance(vehicle, steering.getFeelers(), steering.getVisibleWalls(), force);
	}

	public static ArrayList<Vector2D> createFeelers(int feelerCount, float feelerLength, float feelerFOV){
		ArrayList<Vector2D> feelers= new ArrayList<Vector2D>(feelerCount);
		 //Transformations.CreateWhiskers(feelerCount, feelerLength, feelerFOV, new Vector2D(1,0), new Vector2D(), feelers);
		for(int i = 0; i < feelerCount; i++){
			Vector2D f = new Vector2D();
			
			float angle = -feelerFOV/2f+(i/(float)(feelerCount-1))*feelerFOV;
			f.x = (float) (feelerLength*Math.cos(angle));
			f.y = (float) (feelerLength*Math.sin(angle));
			
			feelers.add(f);
		}
		return feelers;
	}
	
	public static void WallAvoidance(Vehicle m_pVehicle,List<Vector2D> m_Feelers,List<Wall2D> walls, Vector2D rst)
	{
//	  System.out.println("\n\n");
	  float DistToThisIP[]    = new float[]{0.0f};
	  float DistToClosestIP = Float.MAX_VALUE;

	  //this will hold an index into the vector of walls
	  Wall2D ClosestWall = null;

	  Vector2D point = new Vector2D();         //used for storing temporary info
	  Vector2D ClosestPoint = null;  //holds the closest intersection point

	  int count = 0;
	  //examine each feeler in turn
	  for ( Vector2D feeler : m_Feelers)
	  {
	    //run through each wall checking for any intersection points
	    for ( Wall2D w : walls)
	    {
	      if (Geometry.LineIntersection2D(m_pVehicle.pos,
	    		  				 feeler,
	                             w.p1,
	                             w.p2,
	                             DistToThisIP,
	                             point))
	      {
	        //is this the closest found so far? If so keep a record
	        if (DistToThisIP[0] < DistToClosestIP)
	        {
	          DistToClosestIP = DistToThisIP[0];

	          ClosestWall = w;

	          ClosestPoint = point;
	        }
	      }
	    }//next wall

	  
	    //if an intersection point has been detected, calculate a force  
	    //that will direct the agent away
	    if (ClosestWall != null)
	    {
	      //calculate by what distance the projected position of the agent
	      //will overshoot the wall
	      Vector2D OverShoot = new Vector2D(feeler);
	      OverShoot.subtract(ClosestPoint);

	      Vector2D hold = new Vector2D();
	      //create a force in the direction of the wall normal, with a 
	      //magnitude of the overshoot
	      hold.setLocation(ClosestWall.vN);
	      hold.scale(OverShoot.length());
	      
	      rst.add(hold);
	      count++;
	    }

	  }//next feeler
//	  System.out.println("Count"+count);
	  if(count != 0){
		  rst.scale(1f/count);
	  }
	}
}
