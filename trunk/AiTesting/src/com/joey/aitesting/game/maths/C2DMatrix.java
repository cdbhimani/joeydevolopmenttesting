package com.joey.aitesting.game.maths;

import java.util.List;

import com.joey.aitesting.game.shapes.Vector2D;

public class C2DMatrix {
	public Matrix meMatrix = new Matrix();

	public C2DMatrix() {
		// initialize the matrix to an identity matrix
		identity();
	}

	// multiply two matrices together
	public void matrixMultiply(Matrix mIn) {
		Matrix matetemp = new Matrix();

		// first row
		matetemp.e11 = (meMatrix.e11 * mIn.e11) + (meMatrix.e12 * mIn.e21)
				+ (meMatrix.e13 * mIn.e31);
		matetemp.e12 = (meMatrix.e11 * mIn.e12) + (meMatrix.e12 * mIn.e22)
				+ (meMatrix.e13 * mIn.e32);
		matetemp.e13 = (meMatrix.e11 * mIn.e13) + (meMatrix.e12 * mIn.e23)
				+ (meMatrix.e13 * mIn.e33);

		// second
		matetemp.e21 = (meMatrix.e21 * mIn.e11) + (meMatrix.e22 * mIn.e21)
				+ (meMatrix.e23 * mIn.e31);
		matetemp.e22 = (meMatrix.e21 * mIn.e12) + (meMatrix.e22 * mIn.e22)
				+ (meMatrix.e23 * mIn.e32);
		matetemp.e23 = (meMatrix.e21 * mIn.e13) + (meMatrix.e22 * mIn.e23)
				+ (meMatrix.e23 * mIn.e33);

		// third
		matetemp.e31 = (meMatrix.e31 * mIn.e11) + (meMatrix.e32 * mIn.e21)
				+ (meMatrix.e33 * mIn.e31);
		matetemp.e32 = (meMatrix.e31 * mIn.e12) + (meMatrix.e32 * mIn.e22)
				+ (meMatrix.e33 * mIn.e32);
		matetemp.e33 = (meMatrix.e31 * mIn.e13) + (meMatrix.e32 * mIn.e23)
				+ (meMatrix.e33 * mIn.e33);

		meMatrix = matetemp;
	}

	// applies a 2D transformation matrix to a stdevector of Vector2Ds
	public void transformVector2Ds(List<Vector2D> points) {
		float tempX, tempY;
		for (int i = 0; i < points.size(); ++i) {
			Vector2D vPoint = points.get(i);
			tempX = (meMatrix.e11 * vPoint.x) + (meMatrix.e21 * vPoint.y)
					+ (meMatrix.e31);
			tempY = (meMatrix.e12 * vPoint.x) + (meMatrix.e22 * vPoint.y)
					+ (meMatrix.e32);

			vPoint.x = tempX;
			vPoint.y = tempY;
		}
	}
	// applies a 2D transformation matrix to a stdevector of Vector2Ds
	public void transformVector2Ds(List<Vector2D> points,List<Vector2D> transFormedPoints) {
		Vector2D vPoint;
		Vector2D rst;
		for (int i = 0; i < points.size(); ++i) {
			vPoint = points.get(i);
			rst = transFormedPoints.get(i);
			rst.x = (meMatrix.e11 * vPoint.x) + (meMatrix.e21 * vPoint.y)
					+ (meMatrix.e31);
			rst.y = (meMatrix.e12 * vPoint.x) + (meMatrix.e22 * vPoint.y)
					+ (meMatrix.e32);
		}

	}
	// applies a 2D transformation matrix to a single Vector2D
	public void transformVector2Ds(Vector2D vPoint) {
		float tempX = (meMatrix.e11 * vPoint.x) + (meMatrix.e21 * vPoint.y)
				+ (meMatrix.e31);
		float tempY = (meMatrix.e12 * vPoint.x) + (meMatrix.e22 * vPoint.y)
				+ (meMatrix.e32);
		vPoint.x = tempX;
		vPoint.y = tempY;
	}

	// create an identity matrix
	public void identity() {
		meMatrix.e11 = 1;
		meMatrix.e12 = 0;
		meMatrix.e13 = 0;

		meMatrix.e21 = 0;
		meMatrix.e22 = 1;
		meMatrix.e23 = 0;

		meMatrix.e31 = 0;
		meMatrix.e32 = 0;
		meMatrix.e33 = 1;

	}

	// create a transformation matrix
	public void translate(float x, float y) {
		Matrix mat = new Matrix();

		mat.e11 = 1;
		mat.e12 = 0;
		mat.e13 = 0;

		mat.e21 = 0;
		mat.e22 = 1;
		mat.e23 = 0;

		mat.e31 = x;
		mat.e32 = y;
		mat.e33 = 1;

		// and multiply
		matrixMultiply(mat);
	}

	// create a scale matrix
	public void scale(float xScale, float yScale) {
		Matrix mat = new Matrix();

		mat.e11 = xScale;
		mat.e12 = 0;
		mat.e13 = 0;

		mat.e21 = 0;
		mat.e22 = yScale;
		mat.e23 = 0;

		mat.e31 = 0;
		mat.e32 = 0;
		mat.e33 = 1;

		// and multiply
		matrixMultiply(mat);
	}

	// create a rotation matrix
	public void rotate(float rot) {
		Matrix mat = new Matrix();

		float Sin = (float) Math.sin(rot);
		float Cos = (float) Math.cos(rot);

		mat.e11 = Cos;
		mat.e12 = Sin;
		mat.e13 = 0;

		mat.e21 = -Sin;
		mat.e22 = Cos;
		mat.e23 = 0;

		mat.e31 = 0;
		mat.e32 = 0;
		mat.e33 = 1;

		// and multiply
		matrixMultiply(mat);
	}

	// create a rotation matrix from a 2D vector
	public void rotate(Vector2D fwd, Vector2D side) {
		Matrix mat = new Matrix();

		mat.e11 = fwd.x;
		mat.e12 = fwd.y;
		mat.e13 = 0;

		mat.e21 = side.x;
		mat.e22 = side.y;
		mat.e23 = 0;

		mat.e31 = 0;
		mat.e32 = 0;
		mat.e33 = 1;

		// and multiply
		matrixMultiply(mat);
	}

}
