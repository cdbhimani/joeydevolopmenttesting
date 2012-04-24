package com.joey.aitesting.game.shapes;

public class Vector2D {
	public float x;
	public float y;

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

	public float distanceSq(Vector2D p) {
		float dx = p.x - x;
		float dy = p.y - y;
		return dx * dx + dy * dy;
	}

	public String toString() {
		return super.toString() + "[" + x + "," + y + "]";
	}

	public float lengthSq() {
		// TODO Auto-generated method stub
		return x * x + y * y;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public void scale(float x, float y) {
		this.x *= x;
		this.y *= y;
	}

	public void scale(float scale) {
		this.x *= scale;
		this.y *= scale;
	}

	public void normalise() {
		scale(1f / length());
	}

	public void subtract(Vector2D b) {
		x = x - b.x;
		y = y - b.y;
	}

	public void add(Vector2D b) {
		x = x + b.x;
		y = y + b.y;
	}

	@Deprecated
	public static Vector2D subtract(Vector2D vecA, Vector2D vecB) {
		Vector2D rst = new Vector2D();
		subtract(vecA, vecB, rst);
		return rst;
	}

	public static void subtract(Vector2D vecA, Vector2D vecB, Vector2D rst) {
		rst.x = vecA.x - vecB.x;
		rst.y = vecA.y - vecB.y;
	}

	public float dot(Vector2D v) {
		return x * v.x + y * v.y;
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
		if (this.length() > max) {
			this.normalise();
			this.scale(max);
		}
	}

	public Vector2D getReverse() {
		return new Vector2D(-x, -y);
	}

	public void setPerp(Vector2D dir) {
		// TODO Auto-generated method stub
		x = -y;
		y = x;
	}

	public void setPerp() {
		float tmp;
		tmp = -y;
		y = x;
		x = tmp;
	}

	public void setLocation(Vector2D v) {
		this.x = v.x;
		this.y = v.y;
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
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
		v.normalise();
		return (float) Math.atan2(v.y, v.x);

	}

	public static Vector2D fromPoints(Vector2D a1, Vector2D a2) {
		Vector2D rst = new Vector2D();
		subtract(a1, a2, rst);
		return rst;
	}

	public Vector2D clone() {
		return new Vector2D(x, y);
	}
}
