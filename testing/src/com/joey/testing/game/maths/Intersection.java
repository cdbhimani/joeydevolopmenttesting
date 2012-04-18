//package com.joey.testing.game.maths;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import com.joey.testing.game.shapes.Vector2D;
//
///*****
// *
// * Intersection.js
// *
// * copyright 2002-2003, Kevin Lindsey
// *
// *****/
//
//public class Intersection {
//	public static int TYPE_OUTSIDE = 0;
//	public static int TYPE_INSIDE = 1;
//	public static int TYPE_INTERSECT = 2;
//
//	public int intersectionType = TYPE_OUTSIDE;
//	public ArrayList<Vector2D> points;
//
//	/*****
//	*
//	*   intersectEllipseLine
//	*
//	*   NOTE: Rotation will need to be added to this function
//	*
//	*****/
//	public void intersectEllipseLine(Vector2D center, float rx, float ry, Vector2D a1, Vector2D a2, Intersection intersection) {
//	    //var result;
//	    Vector2D origin = new Vector2D(a1.x, a1.y);
//	    Vector2D dir    = Vector2D.fromPoints(a1, a2);
//	    Vector2D diff   = new Vector2D(origin);
//	    diff.subtract(center);
//	    Vector2D mDir   = new Vector2D( dir.x/(rx*rx),  dir.y/(ry*ry)  );
//	    Vector2D mDiff  = new Vector2D( diff.x/(rx*rx), diff.y/(ry*ry) );
//
//	    float a = dir.dot(mDir);
//	    float b = dir.dot(mDiff);
//	    float c = diff.dot(mDiff) - 1.0f;
//	    float d = b*b - a*c;
//
//	    if ( d < 0 ) {
//	    	intersection.intersectionType = TYPE_OUTSIDE;
//	        return;
//	    } else if ( d > 0 ) {
//	        float root = (float)Math.sqrt(d);
//	        float t_a  = (-b - root) / a;
//	        float t_b  = (-b + root) / a;
//
//	        if ( (t_a < 0 || 1 < t_a) && (t_b < 0 || 1 < t_b) ) {
//	            if ( (t_a < 0 && t_b < 0) || (t_a > 1 && t_b > 1) )
//	            	intersection.intersectionType = TYPE_OUTSIDE;
//	            else
//	            	intersection.intersectionType = TYPE_INSIDE;
//	        } else {
//	        	intersection.intersectionType = TYPE_INTERSECT;
//	            if ( 0 <= t_a && t_a <= 1 )
//	            	intersection.appendPoint( a1.lerp(a2, t_a) );
//	            if ( 0 <= t_b && t_b <= 1 )
//	            	intersection.appendPoint( a1.lerp(a2, t_b) );
//	        }
//	    } else {
//	        float t = -b/a;
//	        if ( 0 <= t && t <= 1 ) {
//	        	intersection.intersectionType = TYPE_INTERSECT;
//	        	intersection.appendPoint( a1.lerp(a2, t) );
//	        } else {
//	        	intersection.intersectionType = TYPE_OUTSIDE;
//	        }
//	    }
//
//	    return;
//	};
//}
