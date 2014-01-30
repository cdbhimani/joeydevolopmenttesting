package com.emptyPockets.box2d.shape.data;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.emptyPockets.box2d.utils.Box2DShapeConvertor;
import com.emptyPockets.utils.maths.MathsToolkit;

public class PolygonShapeData extends ShapeData {
	ArrayList<Vector2> points;
	
	public PolygonShapeData(){
		points = new ArrayList<Vector2>();
	}
	
	public PolygonShapeData(ArrayList<Vector2> points){
		this.points = points;
	}
	
	public Vector2 getPoint(int index){
		synchronized (points) {
			return points.get(index);
		}
	}
	
	public void addPoint(Vector2 p){
		synchronized (points) {
			points.add(p);
			updateBoundingBox();
		}
	}
	
	public void addPoint(int index, Vector2 p){
		synchronized (points) {
			points.add(index, p);
			updateBoundingBox();
		}
	}
	
	public void removePoint(int index){
		synchronized (points) {
			points.remove(index);
			updateBoundingBox();
		}
	}
	
	public void updatePoint(int index, Vector2 p){
		synchronized (points) {
			points.get(index).set(p);
			updateBoundingBox();
		}
	}
	@Override
	public void updateBoundingBox() {
		synchronized (points) {
			Vector2 p;
			for(int i = 0; i < points.size(); i++){
				p = points.get(i);
				if(i == 0){
					aaBoundingBox.x = p.x;
					aaBoundingBox.y = p.y;
					aaBoundingBox.width =p.x;
					aaBoundingBox.height =p.y;
				}
				
				if(aaBoundingBox.x < p.x){
					aaBoundingBox.x = p.x;
				}
				
				if(aaBoundingBox.y < p.y){
					aaBoundingBox.y = p.y;
				}
				
				if(aaBoundingBox.width > p.x){
					aaBoundingBox.width = p.x;
				}
				
				if(aaBoundingBox.height > p.y){
					aaBoundingBox.height = p.y;
				}
			}
			
			aaBoundingBox.width-=aaBoundingBox.x;
			aaBoundingBox.height-=aaBoundingBox.y;
			
			MathsToolkit.validateRectangle(aaBoundingBox);
			
		}
	}
	
	public boolean isConvex() {
		synchronized (points) {
			boolean isPositive = false;
			int nVertices = points.size();
			for (int i = 0; i < nVertices; ++i) {
				int lower = (i == 0) ? (nVertices - 1) : (i - 1);
				int middle = i;
				int upper = (i == nVertices - 1) ? (0) : (i + 1);
				
				Vector2 lowerP = points.get(lower);
				Vector2 middleP = points.get(middle);
				Vector2 upperP = points.get(upper);
				float dx0 = middleP.x - lowerP.x;
				float dy0 = middleP.y - lowerP.y;
				float dx1 = upperP.x - middleP.x;
				float dy1 = upperP.y - middleP.y;
				float cross = dx0 * dy1 - dx1 * dy0;
				//Cross product should have same sign
				//for each vertex if poly is convex.
				boolean newIsP = (cross > 0) ? true : false;
				if (i == 0) {
					isPositive = newIsP;
				} else if (isPositive != newIsP) {
					return false;
				}
			}
		}
		return true;
	}
	
	public int getPointCount(){
		return points.size();
	}

	public boolean contains (float x, float y) {
		int intersects = 0;
		synchronized (points) {
			final int numPoints = points.size();
			Vector2 p1;
			Vector2 p2;
			for (int i = 0; i < numPoints; i++) {
				p1 = points.get(i);
				p2 = points.get(i%numPoints);
				if (((p1.y <= y && y < p2.y) || (p2.y <= y && y < p1.y)) && x < ((p2.x - p1.x) / (p2.y - p1.y) * (y - p1.y) + p1.x)) intersects++;
			}
		}
		return (intersects & 1) == 1;
	}

	
	public void setShape(Shape shape) {
		if(true){
			throw new InvalidParameterException("Not implemented");
		}
		if(shape.getType() == Type.Polygon){
			PolygonShape poly = (PolygonShape) shape;
			synchronized (points) {
				
				//If not enough points add them to the array
				if(points.size() < poly.getVertexCount()){
					int required = poly.getVertexCount()-points.size();
					points.ensureCapacity(required);
					for(int i = 0; i< required; i++){
						points.add(new Vector2());
					}
					points.trimToSize();
				}
				
				//If too few points remove them all
				if(points.size() > poly.getVertexCount()){
					int required = points.size()-poly.getVertexCount();
					for(int i = 0; i< required; i++){
						points.remove(0);
					}
					points.trimToSize();
				}
				
				//Retrieve polygon data
				for(int i = 0; i < poly.getVertexCount(); i++){
					poly.getVertex(i, points.get(i));
				}
			}
		}else{
			throw new InvalidParameterException("Not A Polygon");
		}
	}

	@Override
	public void updateShapes() {
		if(shapes == null){
			shapes = new ArrayList<Shape>();
		}
		for(Shape s : shapes){
			s.dispose();
		}
		shapes.clear();
		
		shapes.addAll(Box2DShapeConvertor.getPolygonShapes(points));
	}

	public void removePoints(ArrayList<Vector2> pointToRemove) {
		synchronized (points) {
			points.removeAll(pointToRemove);
		}
	}
}
