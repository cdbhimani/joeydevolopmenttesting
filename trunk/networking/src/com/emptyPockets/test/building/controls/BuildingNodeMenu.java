package com.emptyPockets.test.building.controls;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.emptyPockets.test.building.model.BuildingNode;

public class BuildingNodeMenu {
	String name;
	boolean visible = false;
	ArrayList<BuildingNodeMenuItem> menuItems;
	Rectangle bounds;
	public BuildingNodeMenu(String name){
		this.name = name;
		bounds = new Rectangle();
		menuItems = new ArrayList<BuildingNodeMenuItem>();
	}
	
	public void addMenuItem(BuildingNodeMenuItem item){
		menuItems.add(item);
	}
	
	public void draw(ShapeRenderer rend){
		if(!visible){
			return;
		}
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
		return getMenuItem(x, y)!=null;
	}

	public BuildingNodeMenuItem getMenuItem(float x, float y) {
		if(!visible){
			return null;
		}
		for(BuildingNodeMenuItem item : menuItems){
			if(item.contains(x,y)){
				return item;
			}
		}
		return null;
	}
}
