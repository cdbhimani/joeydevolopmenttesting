package com.joey.aitesting.game.maths;

import java.util.List;

import com.joey.aitesting.game.shapes.Vector2D;

public class Geometry {
	
	//given a plane and a ray this function determins how far along the ray 
	//an interestion occurs. Returns negative if the ray is parallel
	public static float DistanceToRayPlaneIntersection(Vector2D RayOrigin,
	                                             Vector2D RayHeading,
	                                             Vector2D PlanePoint,  //any point on the plane
	                                             Vector2D PlaneNormal)
	{
	  
	  float d     = - PlaneNormal.dot(PlanePoint);
	  float numer = PlaneNormal.dot(RayOrigin) + d;
	  float denom = PlaneNormal.dot(RayHeading);
	  
	  // normal is parallel to vector
	  if ((denom < 0.000001) && (denom > -0.000001))
	  {
	   return (-1.0f);
	  }

	  return -(numer / denom);	
	}

	//------------------------- WhereIsPoint --------------------------------------
	public enum span_type{plane_backside, plane_front, on_plane};
	public static span_type WhereIsPoint(Vector2D point,
	                              Vector2D PointOnPlane, //any point on the plane
	                              Vector2D PlaneNormal) 
	{
	 Vector2D dir = new Vector2D(PointOnPlane);
	 dir.subtract(point);

	 float d = dir.dot(PlaneNormal);
	 
	 if (d<-0.000001)
	 {
	  return span_type.plane_front;	
	 }

	 else if (d>0.000001)
	 {
	  return span_type.plane_backside;	
	 }

	  return span_type.on_plane;	
	}


	
	//-------------------------- GetRayCircleIntersec -----------------------------
	public static float GetRayCircleIntersect(Vector2D RayOrigin,
	                                    Vector2D RayHeading,
	                                    Vector2D CircleOrigin,
	                                    float  radius)
	{
		
	   Vector2D ToCircle = new Vector2D(CircleOrigin);
	   ToCircle.subtract(RayOrigin);
	   float length      = ToCircle.length();
	   float v           = ToCircle.dot(RayHeading);
	   float d           = radius*radius - (length*length - v*v);

	   // If there was no intersection, return -1
	   if (d < 0.0) return (-1.0f);

	   // Return the distance to the [first] intersecting point
	   return (float)(v - Math.sqrt(d));
	}

	//----------------------------- DoRayCircleIntersect --------------------------
	public boolean DoRayCircleIntersect(Vector2D RayOrigin,
	                                 Vector2D RayHeading,
	                                 Vector2D CircleOrigin,
	                                 float     radius)
	{
		
	   Vector2D ToCircle = new Vector2D(CircleOrigin);
	   ToCircle.subtract(RayOrigin);
	   float length      = ToCircle.length();
	   float v           = ToCircle.dot(RayHeading);
	   float d           = radius*radius - (length*length - v*v);

	   // If there was no intersection, return -1
	   return (d < 0.0);
	}


	//------------------------------------------------------------------------
	//  Given a point P and a circle of radius R centered at C this function
	//  determines the two points on the circle that intersect with the 
	//  tangents from P to the circle. Returns false if P is within the circle.
	//
	//  thanks to Dave Eberly for this one.
	//------------------------------------------------------------------------
	public boolean GetTangentPoints (Vector2D C, float R, Vector2D P, Vector2D T1, Vector2D T2)
	{
	  Vector2D PmC = new Vector2D(P);
	  PmC.subtract(C);
	  float SqrLen = PmC.lengthSq();
	  float RSqr = R*R;
	  if ( SqrLen <= RSqr )
	  {
	      // P is inside or on the circle
	      return false;
	  }

	  float InvSqrLen = 1/SqrLen;
	  float Root =(float) Math.sqrt(Math.abs(SqrLen - RSqr));

	  T1.x = C.x + R*(R*PmC.x - PmC.y*Root)*InvSqrLen;
	  T1.y = C.y + R*(R*PmC.y + PmC.x*Root)*InvSqrLen;
	  T2.x = C.x + R*(R*PmC.x + PmC.y*Root)*InvSqrLen;
	  T2.y = C.y + R*(R*PmC.y - PmC.x*Root)*InvSqrLen;

	  return true;
	}




	//------------------------- DistToLineSegment ----------------------------
	//
	//  given a line segment AB and a point P, this function calculates the 
	//  perpendicular distance between them
	//------------------------------------------------------------------------
	public static float DistToLineSegment(Vector2D A,
	                                Vector2D B,
	                                Vector2D P)
	{
	  //if the angle is obtuse between PA and AB is obtuse then the closest
	  //vertex must be A
	  float dotA = (P.x - A.x)*(B.x - A.x) + (P.y - A.y)*(B.y - A.y);

	  if (dotA <= 0) return A.distance(P);

	  //if the angle is obtuse between PB and AB is obtuse then the closest
	  //vertex must be B
	  float dotB = (P.x - B.x)*(A.x - B.x) + (P.y - B.y)*(A.y - B.y);
	 
	  if (dotB <= 0) return B.distance(P);
	    
	  //calculate the point along AB that is the closest to P
	  Vector2D Point = new Vector2D();
	  Point.x = A.x + ((B.x - A.x) * dotA)/(dotA + dotB);
	  Point.y = A.y + ((B.y - A.y) * dotA)/(dotA + dotB);
	  
	  //calculate the distance P-Point
	  return P.distance(Point);
	}

	//------------------------- DistToLineSegmentSq ----------------------------
	//
	//  as above, but avoiding sqrt
	//------------------------------------------------------------------------
	public static float DistToLineSegmentSq(Vector2D A,
	                                 Vector2D B,
	                                 Vector2D P)
	{
	  //if the angle is obtuse between PA and AB is obtuse then the closest
	  //vertex must be A
	  float dotA = (P.x - A.x)*(B.x - A.x) + (P.y - A.y)*(B.y - A.y);

	  if (dotA <= 0) return A.distanceSq(P);

	  //if the angle is obtuse between PB and AB is obtuse then the closest
	  //vertex must be B
	  float dotB = (P.x - B.x)*(A.x - B.x) + (P.y - B.y)*(A.y - B.y);
	 
	  if (dotB <= 0) return B.distanceSq(P);
	    
	  //calculate the point along AB that is the closest to P
	  Vector2D Point = new Vector2D();
	  Point.x = A.x + ((B.x - A.x) * dotA)/(dotA + dotB);
	  Point.y = A.y + ((B.y - A.y) * dotA)/(dotA + dotB);
	  
	  //calculate the distance P-Point
	  return P.distanceSq(Point);
	}


	//--------------------LineIntersection2D-------------------------
	//
//		Given 2 lines in 2D space AB, CD this returns true if an 
//		intersection occurs.
	//
	//----------------------------------------------------------------- 

	public static boolean LineIntersection2D(Vector2D A,
	                               Vector2D B,
	                               Vector2D C, 
	                               Vector2D D)
	{
	  float rTop = (A.y-C.y)*(D.x-C.x)-(A.x-C.x)*(D.y-C.y);
	  float sTop = (A.y-C.y)*(B.x-A.x)-(A.x-C.x)*(B.y-A.y);

		float Bot = (B.x-A.x)*(D.y-C.y)-(B.y-A.y)*(D.x-C.x);

	  if (Bot == 0)//parallel
	  {
	    return false;
	  }

	  float invBot = 1.0f/Bot;
		float r = rTop * invBot;
		float s = sTop * invBot;

		if( (r > 0) && (r < 1) && (s > 0) && (s < 1) )
	  {
	    //lines intersect
	    return true;
	  }

	  //lines do not intersect
	  return false;
	}

	//--------------------LineIntersection2D-------------------------
	//
//		Given 2 lines in 2D space AB, CD this returns true if an 
//		intersection occurs and sets dist to the distance the intersection
	//  occurs along AB
	//
	//----------------------------------------------------------------- 

	public static  boolean LineIntersection2D(Vector2D A,
	                        Vector2D B,
	                        Vector2D C, 
	                        Vector2D D,
	                        float dist[])
	{

	  float rTop = (A.y-C.y)*(D.x-C.x)-(A.x-C.x)*(D.y-C.y);
	  float sTop = (A.y-C.y)*(B.x-A.x)-(A.x-C.x)*(B.y-A.y);

		float Bot = (B.x-A.x)*(D.y-C.y)-(B.y-A.y)*(D.x-C.x);


	  if (Bot == 0)//parallel
	  {
	    if ((rTop==0) && (sTop== 0))
	    {
	      return true;
	    }
	    return false;
	  }

		float r = rTop/Bot;
		float s = sTop/Bot;

		if( (r > 0) && (r < 1) && (s > 0) && (s < 1) )
	  {
	  	dist[0] = A.distance(B) * r;

	    return true;
	  }

		else
	  {
			dist[0] = 0;

	    return false;
	  }
	}

	//-------------------- LineIntersection2D-------------------------
	//
//		Given 2 lines in 2D space AB, CD this returns true if an 
//		intersection occurs and sets dist to the distance the intersection
	//  occurs along AB. Also sets the 2d vector point to the point of
	//  intersection
	//----------------------------------------------------------------- 
	public static  boolean LineIntersection2D(Vector2D   A,
	                               Vector2D   B,
	                               Vector2D   C, 
	                               Vector2D   D,
	                               float[]     dist,
	                               Vector2D  point)
	{

	  float rTop = (A.y-C.y)*(D.x-C.x)-(A.x-C.x)*(D.y-C.y);
		float rBot = (B.x-A.x)*(D.y-C.y)-(B.y-A.y)*(D.x-C.x);

		float sTop = (A.y-C.y)*(B.x-A.x)-(A.x-C.x)*(B.y-A.y);
		float sBot = (B.x-A.x)*(D.y-C.y)-(B.y-A.y)*(D.x-C.x);

		if ( (rBot == 0) || (sBot == 0))
		{
			//lines are parallel
			return false;
		}

		float r = rTop/rBot;
		float s = sTop/sBot;

		if( (r > 0) && (r < 1) && (s > 0) && (s < 1) )
	  {
	  	dist[0] =A.distance(B) * r;

	    point.x = A.x + r * (B.x - A.x);
	    point.y = A.y + r * (B.y - A.y);
	    return true;
	  }

		else
	  {
			dist[0] = 0;

	    return false;
	  }
	}

	//----------------------- ObjectIntersection2D ---------------------------
	//
	//  tests two polygons for intersection. *Does not check for enclosure*
	//------------------------------------------------------------------------
	public static  boolean ObjectIntersection2D(List<Vector2D> object1,
	                                 List<Vector2D> object2)
	{
	  //test each line segment of object1 against each segment of object2
	  for (int r=0; r<object1.size()-1; ++r)
	  {
	    for (int t=0; t<object2.size()-1; ++t)
	    {
	      if (LineIntersection2D(object2.get(t),
	                             object2.get(t+1),
	                             object1.get(r),
	                             object1.get(r+1)))
	      {
	        return true;
	      }
	    }
	  }

	  return false;
	}

	//----------------------- SegmentObjectIntersection2D --------------------
	//
	//  tests a line segment against a polygon for intersection
	//  *Does not check for enclosure*
	//------------------------------------------------------------------------
	public static  boolean SegmentObjectIntersection2D(Vector2D A,
	                                 Vector2D B,
	                                 List<Vector2D> object)
	{
	  //test AB against each segment of object
	  for (int r=0; r<object.size()-1; ++r)
	  {
	    if (LineIntersection2D(A, B, object.get(r), object.get(r+1)))
	    {
	      return true;
	    }
	  }

	  return false;
	}


	//----------------------------- TwoCirclesOverlapped ---------------------
	//
	//  Returns true if the two circles overlap
	//------------------------------------------------------------------------
	public static  boolean TwoCirclesOverlapped(float x1, float y1, float r1,
	                          float x2, float y2, float r2)
	{
	  float DistBetweenCenters =(float) Math.sqrt( (x1-x2) * (x1-x2) +
	                                    (y1-y2) * (y1-y2));

	  if ((DistBetweenCenters < (r1+r2)) || (DistBetweenCenters < Math.abs(r1-r2)))
	  {
	    return true;
	  }

	  return false;
	}

	//----------------------------- TwoCirclesOverlapped ---------------------
	//
	//  Returns true if the two circles overlap
	//------------------------------------------------------------------------
	public static  boolean TwoCirclesOverlapped(Vector2D c1, float r1,
	                          Vector2D c2, float r2)
	{
	  float DistBetweenCenters = (float)Math.sqrt( (c1.x-c2.x) * (c1.x-c2.x) +
	                                    (c1.y-c2.y) * (c1.y-c2.y));

	  if ((DistBetweenCenters < (r1+r2)) || (DistBetweenCenters < Math.abs(r1-r2)))
	  {
	    return true;
	  }

	  return false;
	}

	//--------------------------- TwoCirclesEnclosed ---------------------------
	//
	//  returns true if one circle encloses the other
	//-------------------------------------------------------------------------
	public static  boolean TwoCirclesEnclosed(float x1, float y1, float r1,
	                        float x2, float y2, float r2)
	{
	  float DistBetweenCenters = (float)Math.sqrt( (x1-x2) * (x1-x2) +
	                                    (y1-y2) * (y1-y2));

	  if (DistBetweenCenters < Math.abs(r1-r2))
	  {
	    return true;
	  }

	  return false;
	}

	//------------------------ TwoCirclesIntersectionPoints ------------------
	//
	//  Given two circles this function calculates the intersection points
	//  of any overlap.
	//
	//  returns false if no overlap found
	//
	// see http://astronomy.swin.edu.au/~pbourke/geometry/2circle/
	//------------------------------------------------------------------------ 
	public static  boolean TwoCirclesIntersectionPoints(float x1, float y1, float r1,
	                                  float x2, float y2, float r2,
	                                  Vector2D p1,
	                                  Vector2D p2)
	{
	  //first check to see if they overlap
	  if (!TwoCirclesOverlapped(x1,y1,r1,x2,y2,r2))
	  {
	    return false;
	  }

	  //calculate the distance between the circle centers
	  float d = (float)Math.sqrt( (x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));
	  
	  //Now calculate the distance from the center of each circle to the center
	  //of the line which connects the intersection points.
	  float a = (r1 - r2 + (d * d)) / (2 * d);
	  float b = (r2 - r1 + (d * d)) / (2 * d);
	  

	  //MAYBE A TEST FOR EXACT OVERLAP? 

	  //calculate the point P2 which is the center of the line which 
	  //connects the intersection points
	  float p2X, p2Y;

	  p2X = x1 + a * (x2 - x1) / d;
	  p2Y = y1 + a * (y2 - y1) / d;

	  //calculate first point
	  float h1 = (float)Math.sqrt((r1 * r1) - (a * a));

	  p1.x = p2X - h1 * (y2 - y1) / d;
	  p1.y = p2Y + h1 * (x2 - x1) / d;


	  //calculate second point
	  float h2 = (float)Math.sqrt((r2 * r2) - (a * a));

	  p2.x = p2X + h2 * (y2 - y1) / d;
	  p2.y = p2Y - h2 * (x2 - x1) / d;

	  return true;

	}

	//------------------------ TwoCirclesIntersectionArea --------------------
	//
	//  Tests to see if two circles overlap and if so calculates the area
	//  defined by the union
	//
	// see http://mathforum.org/library/drmath/view/54785.html
	//-----------------------------------------------------------------------
	public static  float TwoCirclesIntersectionArea(float x1, float y1, float r1,
	                                  float x2, float y2, float r2)
	{
	  //first calculate the intersection points
	  Vector2D p1 = new Vector2D();
	  Vector2D p2 = new Vector2D();

	  if(!TwoCirclesIntersectionPoints(x1,y1,r1,x2,y2,r2,p1,p2))
	  {
	    return 0.0f; //no overlap
	  }

	  //calculate the distance between the circle centers
	  float d = (float)Math.sqrt( (x1-x2) * (x1-x2) + (y1-y2) * (y1-y2));

	  //find the angles given that A and B are the two circle centers
	  //and C and D are the intersection points
	  float CBD = (float)(2 * Math.acos((r2*r2 + d*d - r1*r1) / (r2 * d * 2))); 

	  float CAD = (float)( 2 * Math.acos((r1*r1 + d*d - r2*r2) / (r1 * d * 2)));


	  //Then we find the segment of each of the circles cut off by the 
	  //chord CD, by taking the area of the sector of the circle BCD and
	  //subtracting the area of triangle BCD. Similarly we find the area
	  //of the sector ACD and subtract the area of triangle ACD.

	  float area =(float)( 0.5f*CBD*r2*r2 - 0.5f*r2*r2*Math.sin(CBD) +
	                0.5f*CAD*r1*r1 - 0.5f*r1*r1*Math.sin(CAD));

	  return area;
	}

	//-------------------------------- CircleArea ---------------------------
	//
	//  given the radius, calculates the area of a circle
	//-----------------------------------------------------------------------
	public static  float CircleArea(float radius)
	{
	  return (float)Math.PI * radius * radius;
	}


	//----------------------- PointInCircle ----------------------------------
	//
	//  returns true if the point p is within the radius of the given circle
	//------------------------------------------------------------------------
	public static  boolean PointInCircle(Vector2D Pos,
							  float    radius,
	                          Vector2D p)
	{
		
	  float DistFromCenterSquared = (p.distanceSq(Pos));

	  if (DistFromCenterSquared < (radius*radius))
	  {
	    return true;
	  }

	  return false;
	}

	//--------------------- LineSegmentCircleIntersection ---------------------------
	//
	//  returns true if the line segemnt AB intersects with a circle at
	//  position P with radius radius
	//------------------------------------------------------------------------
	public static  boolean   LineSegmentCircleIntersection(Vector2D A,
	                                            Vector2D B,
	                                            Vector2D P,
	                                            float    radius)
	{
	  //first determine the distance from the center of the circle to
	  //the line segment (working in distance squared space)
	  float DistToLineSq = DistToLineSegmentSq(A, B, P);

	  if (DistToLineSq < radius*radius)
	  {
	    return true;
	  }

	  else
	  {
	    return false;
	  }

	}

	//------------------- GetLineSegmentCircleClosestIntersectionPoint ------------
	//
	//  given a line segment AB and a circle position and radius, this function
	//  determines if there is an intersection and stores the position of the 
	//  closest intersection in the reference IntersectionPoint
	//
	//  returns false if no intersection point is found
	//-----------------------------------------------------------------------------
	public static  boolean GetLineSegmentCircleClosestIntersectionPoint(Vector2D A,
	                                                         Vector2D B,
	                                                         Vector2D pos,
	                                                         float    radius,
	                                                         Vector2D IntersectionPoint)
	{
	  Vector2D toBNorm = new Vector2D(B);
	  toBNorm.subtract(A);

	  //move the circle into the local space defined by the vector B-A with origin
	  //at A
	  Vector2D LocalPos = new Vector2D();
	  Transformations.PointToLocalSpace(pos, toBNorm, toBNorm.perp(), A, LocalPos);

	  boolean ipFound = false;

	  //if the local position + the radius is negative then the circle lays behind
	  //point A so there is no intersection possible. If the local x pos minus the 
	  //radius is greater than length A-B then the circle cannot intersect the 
	  //line segment
	  if ( (LocalPos.x+radius >= 0) &&
	     ( (LocalPos.x-radius)*(LocalPos.x-radius) <= (B.distanceSq( A)) ))
	  {
	     //if the distance from the x axis to the object's position is less
	     //than its radius then there is a potential intersection.
	     if (Math.abs(LocalPos.y) < radius)
	     {
	        //now to do a line/circle intersection test. The center of the 
	        //circle is represented by A, B. The intersection points are 
	        //given by the formulae x = A +/-sqrt(r^2-B^2), y=0. We only 
	        //need to look at the smallest positive value of x.
	        float a = LocalPos.x;
	        float b = LocalPos.y;       

	        float ip = a - (float)Math.sqrt(radius*radius - b*b);

	        if (ip <= 0)
	        {
	          ip = a + (float)Math.sqrt(radius*radius - b*b);
	        }

	        ipFound = true;

	        IntersectionPoint.setLocation(toBNorm);
	        IntersectionPoint.scale(ip);
	        IntersectionPoint.add(A);
	     }
	   }

	  return ipFound;
	}
}
