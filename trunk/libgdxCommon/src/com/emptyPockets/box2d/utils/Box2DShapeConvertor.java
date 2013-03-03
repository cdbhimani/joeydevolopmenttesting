package com.emptyPockets.box2d.utils;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.emptyPockets.box2d.utils.box2d.polygon.Clipper;
import com.emptyPockets.box2d.utils.box2d.polygon.Clipper.Polygonizer;

public class Box2DShapeConvertor {
	public static ArrayList<PolygonShape> getPolygonShapes(ArrayList<Vector2> polygon){
		Vector2[] points = new Vector2[polygon.size()];
		points = polygon.toArray(points);
		Vector2[][] polygons = Clipper.polygonize(Polygonizer.BAYAZIT, points);
		
		ArrayList<PolygonShape> result = new ArrayList<PolygonShape>(polygons.length);
		
		for(int i = 0; i < polygons.length; i++){
			result.add(getPolygonShape(polygons[i]));
		}		
		return result;
	}
	
	public static PolygonShape getPolygonShape(Vector2[] data){
		PolygonShape p = new PolygonShape();
		p.set(data);
		return p;
	}
}
