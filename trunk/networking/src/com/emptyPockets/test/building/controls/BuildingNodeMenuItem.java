package com.emptyPockets.test.building.controls;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.emptyPockets.test.building.model.BuildingNode;

public class BuildingNodeMenuItem {
	String name;
	boolean enabled;
	Rectangle menuBounds;
	Rectangle currentBounds;
	BuildingNodeMenu parent;
	ArrayList<BuildingNodeMenuAction> actions;
	
	public BuildingNodeMenuItem(BuildingNodeMenu owner,String name, Rectangle bounds){
		this.name = name;
		currentBounds = new Rectangle();
		actions = new ArrayList<BuildingNodeMenuAction>();
		menuBounds = bounds;
		enabled = false;
		parent = owner;
	}
	
	public void updatePosition(BuildingNode node){
		currentBounds.set(menuBounds);
		currentBounds.x += node.getPosX();
		currentBounds.y += node.getPosY();
	}

	public void draw(ShapeRenderer rend) {
		rend.begin(ShapeType.Rectangle);
		rend.setColor(Color.BLUE);
		rend.rect(currentBounds.x, currentBounds.y, currentBounds.width, currentBounds.height);
		rend.end();
	}

	public boolean contains(float x, float y){ 
		return currentBounds.contains(x,y);
	}

	public void addAction(BuildingNodeMenuAction action){
		actions.add(action);
	}
	
	public void removeAction(BuildingNodeMenuAction action){
		actions.remove(action);
	}
	
	public void touchDown(){
		for(BuildingNodeMenuAction action : actions){
			action.touchDown(this);
		}
	}
	
	public void touchUp(){
		for(BuildingNodeMenuAction action : actions){
			action.touchUp(this);
		}
	}
	public void click() {
		for(BuildingNodeMenuAction action : actions){
			action.click(this);
		}
	}
	
	public void touchDragged(float x, float y) {
		for(BuildingNodeMenuAction action : actions){
			action.touchDragged(this, x, y);
		}
	}
}
