package com.joey.aitesting.game.maths;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.joey.aitesting.game.entities.MovingEntity;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Vector2D;

public class Transformations {
	
	public static void main(String input[]) throws InterruptedException{
		final int sizeX = 200;
		final int sizeY = 200;
		float scale = 50;
		ArrayList<Vector2D> localVehicleShape = new ArrayList<Vector2D>();
		ArrayList<Vector2D> transformedVehicleShape = new ArrayList<Vector2D>();
		
		localVehicleShape.add(new Vector2D(0f*scale, 1f*scale));
		localVehicleShape.add(new Vector2D(1.0f*scale, 0.0f*scale));
		localVehicleShape.add(new Vector2D(1f*scale, 1f*scale));
		
		transformedVehicleShape.add(localVehicleShape.get(0).clone());
		transformedVehicleShape.add(localVehicleShape.get(1).clone());
		transformedVehicleShape.add(localVehicleShape.get(2).clone());
		
	
		final BufferedImage rst = new BufferedImage(sizeX,sizeY, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = rst.createGraphics();
		
		final Color or = new Color(1f,0f,0f,0.5f);
		final Color tr = new Color(0f,1f,0f,0.5f);
		
		
		
		final Vector2D t1 = transformedVehicleShape.get(0);
		final Vector2D t2 = transformedVehicleShape.get(1);
		final Vector2D t3 = transformedVehicleShape.get(2);
		final Vector2D o1 = localVehicleShape.get(0);
		final Vector2D o2 = localVehicleShape.get(1);
		final Vector2D o3 = localVehicleShape.get(2);
		
		JPanel p = new JPanel(){
			
			public void paintLine(Graphics2D g, Vector2D v1, Vector2D v2, int sizeX, int sizeY, int nodeSize){
				drawNode(g,v1,sizeX, sizeY, nodeSize);
				drawNode(g,v2,sizeX, sizeY, nodeSize);
				g.drawLine(sizeX/2+(int)v1.x, sizeY/2+(int)v1.y, sizeX/2+(int)v2.x, sizeY/2+(int)v2.y);
			}
			
			public void drawNode(Graphics2D g, Vector2D v1, int sizeX, int sizeY, int nodeSize){
				g.drawRect(sizeX/2+(int)v1.x-nodeSize/2, sizeY/2+(int)v1.y-nodeSize/2, nodeSize, nodeSize);
			}
			@Override
			protected void paintComponent(Graphics g1) {
				// TODO Auto-generated method stub
				super.paintComponent(g1);

				g.setColor(Color.white);
				g.fillRect(0, 0, getWidth(), getHeight());

				g.setColor(Color.blue);
				g.fillOval(sizeX/2-1, sizeY/2-2, 4, 4);
				int orgSize = 4;
				int traSize = 8;
				
				g.setColor(or);
				paintLine(g, o1, o2, sizeX, sizeY, orgSize);
				paintLine(g, o3, o2, sizeX, sizeY, orgSize);
				paintLine(g, o1, o3, sizeX, sizeY, orgSize);
				
				g.setColor(tr);

				paintLine(g, t1, t2, sizeX, sizeY, traSize);
				paintLine(g, t3, t2, sizeX, sizeY, traSize);
				paintLine(g, t1, t3, sizeX, sizeY, traSize);
				
				
				g1.drawImage(rst, 0,0,null);
			}
		};
		p.setBackground(Color.WHITE);
		JFrame f= new JFrame();
		f.setLocation(400, 400);
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(p);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(sizeX, sizeY);
		f.setVisible(true);
		
		float angle = 1;
		final Vector2D vel = new Vector2D();
		final Vector2D velHead = new Vector2D();
		final Vector2D velSide = new Vector2D();
		while(true){
			vel.setLocation((float)Math.cos(Math.toRadians(angle)),(float)Math.sin(Math.toRadians(angle)));
			
			if (vel.lengthSq() > 0.00000001) {
				velHead.setLocation(vel);
				velHead.normalise();
				velSide.setPerp(velHead);
			}
			
			
			Transformations.WorldTransform(localVehicleShape, new Vector2D(0,0), velHead, velSide, transformedVehicleShape);
			angle+=1/5f;
			
			Thread.sleep((long)(1000/360f));
			p.repaint();
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
