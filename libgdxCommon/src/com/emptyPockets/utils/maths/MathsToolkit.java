package com.emptyPockets.utils.maths;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MathsToolkit {

	public static boolean SegmentIntersectRectangle(Rectangle rec, Vector2 p1, Vector2 p2) {
		// Find min and max X for the segment

		float a_rectangleMinX = rec.x;
		float a_rectangleMinY = rec.y;
		float a_rectangleMaxX = rec.x+rec.width;
		float a_rectangleMaxY = rec.y+rec.height;
		
		float minX = p1.x;
		float maxX = p2.x;

		if (p1.x > p2.x) {
			minX = p2.x;
			maxX = p1.x;
		}

		// Find the intersection of the segment's and rectangle's x-projections

		if (maxX > a_rectangleMaxX) {
			maxX = a_rectangleMaxX;
		}

		if (minX < a_rectangleMinX) {
			minX = a_rectangleMinX;
		}

		if (minX > maxX) // If their projections do not intersect return false
		{
			return false;
		}

		// Find corresponding min and max Y for min and max X we found before

		float minY = p1.y;
		float maxY = p2.y;

		float dx = p2.x - p1.x;

		if (Math.abs(dx) > 0.0000001) {
			float a = (p2.y - p1.y) / dx;
			float b = p1.y - a * p1.x;
			minY = a * minX + b;
			maxY = a * maxX + b;
		}

		if (minY > maxY) {
			float tmp = maxY;
			maxY = minY;
			minY = tmp;
		}

		// Find the intersection of the segment's and rectangle's y-projections

		if (maxY > a_rectangleMaxY) {
			maxY = a_rectangleMaxY;
		}

		if (minY < a_rectangleMinY) {
			minY = a_rectangleMinY;
		}

		if (minY > maxY) // If Y-projections do not intersect return false
		{
			return false;
		}

		return true;
	}

	public static Vector2 getLinePointIntersection(Vector2 line1,
			Vector2 line2, Vector2 p) {
		Vector2 result = new Vector2();
		getLinePointIntersection(line1, line2, p, result);
		return result;
	}

	public static void getLinePointIntersection(Vector2 line1, Vector2 line2,
			Vector2 p, Vector2 result) {
		float r_numerator = (p.x - line1.x) * (line2.x - line1.x)
				+ (p.y - line1.y) * (line2.y - line1.y);
		float r_denomenator = (line2.x - line1.x) * (line2.x - line1.x)
				+ (line2.y - line1.y) * (line2.y - line1.y);
		float r = r_numerator / r_denomenator;

		result.x = line1.x + r * (line2.x - line1.x);
		result.y = line1.y + r * (line2.y - line1.y);
	}

	/**
	 * 
	 * find the distance from the point (cx,cy) to the line determined by the
	 * points (ax,ay) and (bx,by)
	 * 
	 * distanceSegment = distance from the point to the line segment
	 * distanceLine = distance from the point to the line (assuming infinite
	 * extent in both directions
	 * 
	 * 
	 * 
	 * 
	 * Subject 1.02: How do I find the distance from a point to a line?
	 * 
	 * 
	 * Let the point be C (Cx,Cy) and the line be AB (Ax,Ay) to (Bx,By). Let P
	 * be the point of perpendicular projection of C on AB. The parameter r,
	 * which indicates P's position along AB, is computed by the dot product of
	 * AC and AB divided by the square of the length of AB:
	 * 
	 * (1) AC dot AB r = --------- ||AB||^2
	 * 
	 * r has the following meaning:
	 * 
	 * r=0 P = A r=1 P = B r<0 P is on the backward extension of AB r>1 P is on
	 * the forward extension of AB 0<r<1 P is interior to AB
	 * 
	 * The length of a line segment in d dimensions, AB is computed by:
	 * 
	 * L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 + ... + (Bd-Ad)^2)
	 * 
	 * so in 2D:
	 * 
	 * L = sqrt( (Bx-Ax)^2 + (By-Ay)^2 )
	 * 
	 * and the dot product of two vectors in d dimensions, U dot V is computed:
	 * 
	 * D = (Ux * Vx) + (Uy * Vy) + ... + (Ud * Vd)
	 * 
	 * so in 2D:
	 * 
	 * D = (Ux * Vx) + (Uy * Vy)
	 * 
	 * So (1) expands to:
	 * 
	 * (Cx-Ax)(Bx-Ax) + (Cy-Ay)(By-Ay) r = ------------------------------- L^2
	 * 
	 * The point P can then be found:
	 * 
	 * Px = Ax + r(Bx-Ax) Py = Ay + r(By-Ay)
	 * 
	 * And the distance from A to P = r*L.
	 * 
	 * Use another parameter s to indicate the location along PC, with the
	 * following meaning: s<0 C is left of AB s>0 C is right of AB s=0 C is on
	 * AB
	 * 
	 * Compute s as follows:
	 * 
	 * (Ay-Cy)(Bx-Ax)-(Ax-Cx)(By-Ay) s = ----------------------------- L^2
	 * 
	 * 
	 * Then the distance from C to P = |s|*L.
	 * 
	 */
	public static float getLineSegmentDistance(Vector2 line1, Vector2 line2,
			Vector2 p) {
		float cx = p.x;
		float cy = p.y;

		float ax = line1.x;
		float ay = line1.y;
		float bx = line2.x;
		float by = line2.y;
		float distanceSegment = 0, distanceLine = 0;

		float r_numerator = (cx - ax) * (bx - ax) + (cy - ay) * (by - ay);
		float r_denomenator = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
		float r = r_numerator / r_denomenator;
		//
		float s = ((ay - cy) * (bx - ax) - (ax - cx) * (by - ay))
				/ r_denomenator;

		distanceLine = (float) (Math.abs(s) * Math.sqrt(r_denomenator));

		//
		// (xx,yy) is the point on the lineSegment closest to (cx,cy)
		//
		if ((r >= 0) && (r <= 1)) {
			distanceSegment = distanceLine;
		} else {

			float dist1 = (cx - ax) * (cx - ax) + (cy - ay) * (cy - ay);
			float dist2 = (cx - bx) * (cx - bx) + (cy - by) * (cy - by);
			if (dist1 < dist2) {
				distanceSegment = (float) Math.sqrt(dist1);
			} else {
				distanceSegment = (float) Math.sqrt(dist2);
			}
		}
		return distanceSegment;
	}

	/**
	 * This will get the distance between the line represented by l1 and l2 and
	 * the point p, it assumes that it is an infinite line
	 * 
	 * @param l1
	 * @param l2
	 * @param p
	 */
	public static float getLineDistance(Vector2 l1, Vector2 l2, Vector2 p) {
		float A = p.x - l1.x;
		float B = p.y - l1.y;
		float C = l2.x - l1.x;
		float D = l2.y - l1.y;
		return (float) (Math.abs(A * D - C * B) / Math.sqrt(C * C + D * D));
	}

	// /**
	// * @param lineA
	// * @param lineB
	// * @param p
	// * @return
	// */
	// public static boolean testInterset(Line2D lineA, Line2D lineB, Point2D p)
	// {
	// if (lineA.intersectsLine(lineB)) {
	// double mA = getSlope(lineA);
	// double mB = getSlope(lineB);
	//
	// double cA = getYIntercept(lineA);
	// double cB = getYIntercept(lineB);
	//
	// double x = (cB - cA) / (mA - mB);
	// double y = mA * x + cA;
	//
	// double b1 = -1;
	// double b2 = -1;
	//
	// // Compute the inverse of the determinate ..
	// // first makes sure that it does not go to inf
	//
	// double det_inv = (mA * b2 - mB * b1);
	// if (Math.abs(det_inv) < 0.0001) {
	// return false;
	// }
	// det_inv = 1 / det_inv;
	//
	// // use Kramers rule to compute xi and yi
	//
	// x = ((b1 * cB - b2 * cA) * det_inv);
	// y = ((mB * cA - mA * cB) * det_inv);
	//
	// p.setLocation(x, y);
	// return true;
	// } else {
	// return false;
	// }
	//
	// }
	//
	// /**
	// * This will get the postion on a line using a line segment i.e p = p1 +
	// * r(p2-p1)
	// *
	// * @param p1
	// * @param p2
	// * @param r
	// * @return
	// */
	// public static Point2D.Double getLinePoint(Point2D p1, Point2D p2, double
	// r) {
	// Point2D.Double result = new Point2D.Double();
	//
	// result.x = p1.getX() + r * (p2.getX() - p1.getX());
	// result.y = p1.getY() + r * (p2.getY() - p1.getY());
	// return result;
	// }

}
