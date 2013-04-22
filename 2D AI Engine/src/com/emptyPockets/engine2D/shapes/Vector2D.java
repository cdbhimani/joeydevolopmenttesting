package com.emptyPockets.engine2D.shapes;

import com.badlogic.gdx.math.Vector2;

public class Vector2D extends Vector2 {

	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2D(Vector2D p) {
		this.x = p.x;
		this.y = p.y;
	}

	public String toString() {
		return "[" + x + "," + y + "]("+len()+")";
	}

	/**
	 * //------------------------ Sign
	 * ------------------------------------------
	 * 
	 * returns positive if v2 is clockwise of this vector, minus if
	 * anticlockwise (Y axis pointing down, X axis to right)
	 * 
	 * @param v2
	 * @return
	 */
	public int sign(Vector2D v2) {
		if (y * v2.x > x * v2.y) {
			return -1;
		} else {
			return +1;
		}
	}

	@Deprecated
	public Vector2D perp() {
		return new Vector2D(-y, x);
	}

	public void truncate(float max) {
		if (this.len() > max) {
			this.nor();
			this.mul(max);
		}
	}

	public Vector2D getReverse() {
		return new Vector2D(-x, -y);
	}

	public void setPerp(Vector2D dir) {
		// TODO Auto-generated method stub
		this.x = -dir.y;
		this.y = dir.x;
	}

	public void setPerp() {
		float tmp;
		tmp = -y;
		y = x;
		x = tmp;
	}

	public static void wrapAround(Vector2D pos, float MaxX, float MaxY) {
		if (pos.x > MaxX) {
			pos.x = 0.0f;
		}

		if (pos.x < 0) {
			pos.x = MaxX;
		}

		if (pos.y < 0) {
			pos.y = MaxY;
		}

		if (pos.y > MaxY) {
			pos.y = 0.0f;
		}
	}

	public float getAngle() {
		// TODO Auto-generated method stub
		Vector2D v = clone();
		v.nor();
		return (float) Math.atan2(v.y, v.x);

	}

	public static Vector2D fromPoints(Vector2D a1, Vector2D a2) {
		Vector2D rst = new Vector2D();
		subtract(a1, a2, rst);
		return rst;
	}

	public static void subtract(Vector2 a, Vector2 b, Vector2 rst){
		rst.set(a);
		rst.sub(b);
	}
	public Vector2D clone() {
		return new Vector2D(x, y);
	}

	public static void wrapAround(Vector2D p, Rectangle2D r) {
		if(p.x  < r.x1){
			p.x = r.x2;
		}
		else if(p.x  > r.x2){
			p.x = r.x1;
		}
		
		if(p.y  <r.y1){
			p.y = r.y2;
		}else if(p.y  >r.y2){
			p.y = r.y1;
		}
	}

	public void flip() {
		x= -x;
		y =-y;
	}


}
