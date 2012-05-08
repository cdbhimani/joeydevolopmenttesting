package com.joey.aitesting.game.entities;

import java.util.ArrayList;
import java.util.List;

import com.joey.aitesting.game.shapes.Vector2D;

public class WaypointPath {
	public ArrayList<Vector2D> points = new ArrayList<Vector2D>();
	public boolean loop = false;
	
	int currentNode = 0;
	
	
	public WaypointPath(){
	}
	
	public WaypointPath(List<Vector2D> p){
		points.addAll(p);
	}
	public Vector2D getCurrentNode(){
		return points.get(currentNode);
	}
	
	public boolean hasNext(){
		return loop || currentNode < points.size()-1 ;
	}
	
	public boolean finished(){
		return !loop && currentNode == points.size()-1 ;
	}
	
	public void next(){
		currentNode++;
		if(loop && currentNode == points.size()){
			currentNode = 0;
		}
		if(!loop && currentNode == points.size()){
			currentNode = points.size()-1;
		}
	}
	
	
	public void remove(){
		points.remove(currentNode);
	}
	public void addPoint(Vector2D p){
		points.add(p);
	}
}
