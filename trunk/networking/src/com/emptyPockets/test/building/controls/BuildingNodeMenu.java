package com.emptyPockets.test.building.controls;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.emptyPockets.test.building.model.BuildingNode;

public class BuildingNodeMenu {
	String name;
	ArrayList<BuildingNodeMenuItem> menuItems;
	
	public BuildingNodeMenu(String name){
		this.name = name;
		menuItems = new ArrayList<BuildingNodeMenuItem>();
	}
	
	public void addMenuItem(BuildingNodeMenuItem item){
		menuItems.add(item);
	}
	
	public void draw(ShapeRenderer rend){
		for(BuildingNodeMenuItem item : menuItems){
			item.draw(rend);
		}
	}

	public void updatePosition(BuildingNode buildingNode) {
		for(BuildingNodeMenuItem item : menuItems){
			item.updatePosition(buildingNode);
		}
	}

	public boolean contains(float x, float y) {
		for(BuildingNodeMenuItem item : menuItems){
			if(item.contains(x,y)){
				return true;
			}
		}
		return false;
	}

	public BuildingNodeMenuItem getMenuItem(float x, float y) {
		for(BuildingNodeMenuItem item : menuItems){
			if(item.contains(x,y)){
				return item;
			}
		}
		return null;
	}
}
