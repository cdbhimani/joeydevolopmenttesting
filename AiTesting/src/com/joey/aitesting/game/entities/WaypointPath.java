package com.joey.aitesting.game.entities;

import java.util.ArrayList;
import java.util.List;

import com.joey.aitesting.game.shapes.Vector2D;

public class WaypointPath {
	ArrayList<Vector2D> points = new ArrayList<Vector2D>();
	
	int currentNode = 0;
	boolean loop = false;
	
	public WaypointPath(){
	}
	
	public WaypointPath(List<Vector2D> p){
		points.addAll(p);
	}
	public Vector2D getCurrentNode(){
		return points.get(currentNode);
	}
	
	public boolean hasNext(){
		return loop || currentNode > points.size() ;
	}
	
	public void next(){
		currentNode++;
		if(currentNode > points.size() && loop){
			currentNode = 0;
		}
	}
	
	
	public void remove(){
		points.remove(currentNode);
	}
	public void addPoint(Vector2D p){
		points.add(p);
	}
}
