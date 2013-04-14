package com.emptyPockets.test.building.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.test.building.controls.BuildingNodeMenu;

public class BuildingNode {
	Vector2 pos;
	float radius = 10;
	boolean selected = false;
	private BuildingNodeMenu menu;
	
	public BuildingNode(){
		pos = new Vector2();
	}
	
	public void draw(ShapeRenderer shape){
		if(selected){
			shape.setColor(Color.RED);
		}else{
			shape.setColor(Color.GREEN);
		}
		shape.begin(ShapeType.Circle);
		shape.circle(pos.x, pos.y, radius,100);
		shape.end();
		
		if(selected&&getMenu()!=null){
			getMenu().draw(shape);
		}
	}
	
	public boolean contains(float x, float y){
		if(pos.dst(x, y) < radius){
			return true;
		}
		return false;
	}
	
	public void setPos(float x, float y){
		pos.x = x;
		pos.y = y;
		if(menu != null){
			menu.updatePosition(this);
		}
	}
	
	public float getPosX(){
		return pos.x;
	}
	
	public float getPosY(){
		return pos.y;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public BuildingNodeMenu getMenu() {
		return menu;
	}

	public void setMenu(BuildingNodeMenu menu) {
		this.menu = menu;
		menu.updatePosition(this);
	}
}
