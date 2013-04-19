package com.emptyPockets.test.controls.render;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.emptyPockets.test.controls.entities.BaseEntity;
import com.emptyPockets.test.controls.menus.EntityMenuItem;

public class EntityDebugRender extends EntityRender {
	ShapeRenderer shape;
	
	public EntityDebugRender(){
		shape = new ShapeRenderer();
	}
	
	public void startRender(Camera camera){
		shape.setProjectionMatrix(camera.combined);
		shape.begin(ShapeType.Rectangle);
	}
	
	public void endRender(){
		shape.end();
	}
	
	public void render(BaseEntity e){
		Rectangle bounds = e.getWorldBounds();
		if(e.isTouchable()){
			if(e.isSelected()){
				shape.setColor(Color.GREEN);	
			}else{
				shape.setColor(Color.BLUE);
			}
		}else{
			shape.setColor(Color.RED);
		}
		shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
		
		shape.setColor(Color.RED);
		shape.rect(e.getPosX()-1, e.getPosY()-1, 2, 2);
		
		if(e.getMenu() != null && e.getMenu().isVisible()){
			ArrayList<EntityMenuItem> items = e.getMenu().getMenuItems();
			synchronized (items) {
				for(EntityMenuItem item : items){
					render(item);
				}
			}
		}
	}
}
