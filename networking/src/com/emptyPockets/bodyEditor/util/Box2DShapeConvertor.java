package com.emptyPockets.bodyEditor.util;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.emptyPockets.bodyEditor.util.box2d.polygon.Clipper;
import com.emptyPockets.bodyEditor.util.box2d.polygon.Clipper.Polygonizer;

public class Box2DShapeConvertor {
	public PolygonShape[] getPolygonShapes(ArrayList<Vector2> polygon){
		Vector2[] points = new Vector2[polygon.size()];
		points = polygon.toArray(points);
		Vector2[][] polygons = Clipper.polygonize(Polygonizer.BAYAZIT, points);
		
		PolygonShape rst[] = new PolygonShape[polygons.length];
		
		for(int i = 0; i < rst.length; i++){
			rst[i] = getPolygonShape(polygons[i]);
		}		
		return rst;
	}
	
	public PolygonShape getPolygonShape(Vector2[] data){
		PolygonShape p = new PolygonShape();
		p.set(data);
		return p;
	}
}
