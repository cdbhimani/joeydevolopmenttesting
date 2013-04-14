package com.emptyPockets.test.building.model;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class World {
	ArrayList<BuildingNode> buildings;
	
	public World(){
		buildings = new ArrayList<BuildingNode>();
	}
	
	public void addBuilding(BuildingNode node){
		buildings.add(node);
	}
	
	public void removeBuilding(BuildingNode node){
		buildings.remove(node);
	}
	
	public void draw(ShapeRenderer shape){
		for(BuildingNode building : buildings){
			building.draw(shape);
		}
	}
	
	public BuildingNode selectBuildingNode(float x, float y){
		for(BuildingNode building : buildings){
			if(building.contains(x,y)){
				return building;
			}
		}
		return null;
	}
}
