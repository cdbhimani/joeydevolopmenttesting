package com.joey.aitesting.game.maths;

import java.util.ArrayList;
import java.util.List;

import com.joey.aitesting.game.entities.MovingEntity;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

public class Transformations {
	
	public static void main(String input[]){
		float scale = 10;
		ArrayList<Vector2D> localVehicleShape = new ArrayList<Vector2D>();
		ArrayList<Vector2D> transformedVehicleShape = new ArrayList<Vector2D>();
		
		localVehicleShape.add(new Vector2D(-1.0f*scale, 0.6f*scale));
		localVehicleShape.add(new Vector2D(1.0f*scale, 0.0f*scale));
		localVehicleShape.add(new Vector2D(-1.0f*scale, -0.6f*scale));
		
		transformedVehicleShape.add(new Vector2D(-1.0f*scale, 0.6f*scale));
		transformedVehicleShape.add(new Vector2D(1.0f*scale, 0.0f*scale));
		transformedVehicleShape.add(new Vector2D(-1.0f*scale, -0.6f*scale));
		
		Vector2D vel = new Vector2D(1,1);
		Vector2D velHead = new Vector2D();
		Vector2D velSide = new Vector2D();
		
		if (vel.lengthSq() > 0.00000001) {
			velHead.setLocation(vel);
			velHead.normalise();

			velSide.setPerp(velHead);

		}
		
		Transformations.WorldTransform(localVehicleShape, new Vector2D(0,0), velHead, velSide, transformedVehicleShape);
		for(int i = 0; i < transformedVehicleShape.size(); i++){
			System.out.println(transformedVehicleShape.get(i));
		}
	}
	public static void PointToLocalSpace(MovingEntity vehicle, Vector2D point,
			Vector2D rst) {

		rst.setLocation(point);

		// create a transformation matrix
		C2DMatrix matTransform = new C2DMatrix();

		// create the transformation matrix
		matTransform.meMatrix.e11 = vehicle.velHead.x;
		matTransform.meMatrix.e12 = vehicle.velSide.x;
		matTransform.meMatrix.e21 = vehicle.velHead.y;
		matTransform.meMatrix.e22 = vehicle.velSide.y;
		matTransform.meMatrix.e31 = -vehicle.pos.dot(vehicle.velHead);
		matTransform.meMatrix.e32 = -vehicle.pos.dot(vehicle.velSide);

		// now transform the vertices
		matTransform.transformVector2Ds(rst);
	}
	// --------------------------- WorldTransform -----------------------------
	//
	// given a std::vector of 2D vectors, a position, orientation and scale,
	// this function transforms the 2D vectors into the object's world space
	// ------------------------------------------------------------------------
	public static void WorldTransform(List<Vector2D> TranVector2Ds,
			Vector2D pos, Vector2D forward, Vector2D side, Vector2D scale, List<Vector2D> rst) {
		// create a transformation matrix
		C2DMatrix matTransform = new C2DMatrix();

		// scale
		if ((scale.x != 1.0) || (scale.y != 1.0)) {
			matTransform.scale(scale.x, scale.y);
		}

		// rotate
		matTransform.rotate(forward, side);

		// and translate
		matTransform.translate(pos.x, pos.y);

		// now transform the object's vertices
		matTransform.transformVector2Ds(TranVector2Ds, rst);
	}

	// --------------------------- WorldTransform -----------------------------
	//
	// given a std::vector of 2D vectors, a position and orientation
	// this function transforms the 2D vectors into the object's world space
	// ------------------------------------------------------------------------
	public static void WorldTransform(List<Vector2D> TranVector2Ds,
			Vector2D pos, Vector2D forward, Vector2D side, List<Vector2D> rst) {
		// create a transformation matrix
		C2DMatrix matTransform = new C2DMatrix();

		// rotate
		matTransform.rotate(forward, side);

		// and translate
		matTransform.translate(pos.x, pos.y);

		// now transform the object's vertices
		matTransform.transformVector2Ds(TranVector2Ds,rst);
	}
	// --------------------------- WorldTransform -----------------------------
	//
	// given a std::vector of 2D vectors, a position, orientation and scale,
	// this function transforms the 2D vectors into the object's world space
	// ------------------------------------------------------------------------
	public static void WorldTransform(List<Vector2D> TranVector2Ds,
			Vector2D pos, Vector2D forward, Vector2D side, Vector2D scale) {
		// create a transformation matrix
		C2DMatrix matTransform = new C2DMatrix();

		// scale
		if ((scale.x != 1.0) || (scale.y != 1.0)) {
			matTransform.scale(scale.x, scale.y);
		}

		// rotate
		matTransform.rotate(forward, side);

		// and translate
		matTransform.translate(pos.x, pos.y);

		// now transform the object's vertices
		matTransform.transformVector2Ds(TranVector2Ds);
	}

	// --------------------------- WorldTransform -----------------------------
	//
	// given a std::vector of 2D vectors, a position and orientation
	// this function transforms the 2D vectors into the object's world space
	// ------------------------------------------------------------------------
	public static void WorldTransform(List<Vector2D> TranVector2Ds,
			Vector2D pos, Vector2D forward, Vector2D side) {
		// create a transformation matrix
		C2DMatrix matTransform = new C2DMatrix();

		// rotate
		matTransform.rotate(forward, side);

		// and translate
		matTransform.translate(pos.x, pos.y);

		// now transform the object's vertices
		matTransform.transformVector2Ds(TranVector2Ds);
	}

	// --------------------- PointToWorldSpace --------------------------------
	//
	// Transforms a point from the agent's local space into world space
	// ------------------------------------------------------------------------
	public static void PointToWorldSpace(Vector2D point, Vector2D AgentHeading,
			Vector2D AgentSide, Vector2D AgentPosition, Vector2D rst) {
		rst.setLocation(point);
		// create a transformation matrix
		C2DMatrix matTransform = new C2DMatrix();

		// rotate
		matTransform.rotate(AgentHeading, AgentSide);

		// and translate
		matTransform.translate(AgentPosition.x, AgentPosition.y);

		// now transform the vertices
		matTransform.transformVector2Ds(rst);
	}

	// --------------------- VectorToWorldSpace --------------------------------
	//
	// Transforms a vector from the agent's local space into world space
	// ------------------------------------------------------------------------
	public static void VectorToWorldSpace(Vector2D vec, Vector2D AgentHeading,
			Vector2D AgentSide, Vector2D rst) {
		// make a copy of the point
		rst.setLocation(vec);

		// create a transformation matrix
		C2DMatrix matTransform = new C2DMatrix();

		// rotate
		matTransform.rotate(AgentHeading, AgentSide);

		// now transform the vertices
		matTransform.transformVector2Ds(rst);
	}

	// --------------------- PointToLocalSpace --------------------------------
	//
	// ------------------------------------------------------------------------
	public static void PointToLocalSpace(Vector2D point, Vector2D AgentHeading,
			Vector2D AgentSide, Vector2D AgentPosition, Vector2D rst) {

		// make a copy of the point
		rst.setLocation(point);

		// create a transformation matrix
		C2DMatrix matTransform = new C2DMatrix();

		// create the transformation matrix
		matTransform.meMatrix.e11 = (AgentHeading.x);
		matTransform.meMatrix.e12 = (AgentSide.x);
		matTransform.meMatrix.e21 = (AgentHeading.y);
		matTransform.meMatrix.e22 = (AgentSide.y);
		matTransform.meMatrix.e31 = (-AgentPosition.dot(AgentHeading));
		matTransform.meMatrix.e32 = (-AgentPosition.dot(AgentSide));

		// now transform the vertices
		matTransform.transformVector2Ds(rst);

	}

	// --------------------- VectorToLocalSpace --------------------------------
	//
	// ------------------------------------------------------------------------
	public static void VectorToLocalSpace(Vector2D vec, Vector2D AgentHeading,
			Vector2D AgentSide, Vector2D rst) {

		// make a copy of the point
		rst.setLocation(vec);

		// create a transformation matrix
		C2DMatrix matTransform = new C2DMatrix();

		// create the transformation matrix
		matTransform.meMatrix.e11 = (AgentHeading.x);
		matTransform.meMatrix.e12 = (AgentSide.x);
		matTransform.meMatrix.e21 = (AgentHeading.y);
		matTransform.meMatrix.e22 = (AgentSide.y);

		// now transform the vertices
		matTransform.transformVector2Ds(rst);

	}

	// -------------------------- Vec2DRotateAroundOrigin
	// --------------------------
	//
	// rotates a vector ang rads around the origin
	// -----------------------------------------------------------------------------
	public static void Vec2DRotateAroundOrigin(Vector2D v, float ang,
			Vector2D rst) {
		rst.setLocation(v);
		// create a transformation matrix
		C2DMatrix mat = new C2DMatrix();

		// rotate
		mat.rotate(ang);

		// now transform the object's vertices
		mat.transformVector2Ds(rst);
	}

	// ------------------------ CreateWhiskers
	// ------------------------------------
	//
	// given an origin, a facing direction, a 'field of view' describing the
	// limit of the outer whiskers, a whisker length and the number of whiskers
	// this method returns a vector containing the end positions of a series
	// of whiskers radiating away from the origin and with equal distance
	// between
	// them. (like the spokes of a wheel clipped to a specific segment size)
	// ----------------------------------------------------------------------------
	public static void CreateWhiskers(int NumWhiskers, float WhiskerLength,
			float fov, Vector2D facing, Vector2D origin, List<Vector2D> rst) {
		// this is the magnitude of the angle separating each whisker
		float SectorSize = fov / (float) (NumWhiskers - 1);

		float angle = -fov * 0.5f;

		for (int w = 0; w < NumWhiskers; ++w) {
			Vector2D temp = new Vector2D();
			// create the whisker extending outwards at this angle

			Vec2DRotateAroundOrigin(facing, angle, temp);
			temp.scale(WhiskerLength);
			temp.add(origin);
			rst.add(origin);
			angle += SectorSize;
		}

	}

}
