package com.emptyPockets.bodyEditor.entity;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class PolygonEntity {
	private ArrayList<Vector2> polygon;
	public PolygonEntity(){
		setPolygon(new ArrayList<Vector2>());
	}
	public ArrayList<Vector2> getPolygon() {
		return polygon;
	}
	public void setPolygon(ArrayList<Vector2> polygon) {
		this.polygon = polygon;
	}
	
}
