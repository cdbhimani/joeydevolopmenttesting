package com.emptyPockets.test.controls;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.test.controls.entities.BaseEntity;
import com.emptyPockets.test.controls.inputs.EntityInputManager;
import com.emptyPockets.test.controls.inputs.handlers.EntityInputAdapter;
import com.emptyPockets.test.controls.inputs.handlers.MoveEntityInputHandler;
import com.emptyPockets.test.controls.menus.EntityMenu;
import com.emptyPockets.test.controls.menus.EntityMenuItem;
import com.emptyPockets.test.controls.render.EntityRender;

public class EntityStore {
	
	ArrayList<BaseEntity> entities;
	
	public EntityStore(){
		entities = new ArrayList<BaseEntity>();
	}
	
	public void randomData(int count, Rectangle worldBounds, float maxWidth, float maxHeight, float minScale){
		Vector2 pos = new Vector2();
		Rectangle localBounds = new Rectangle();
		MoveEntityInputHandler move = new MoveEntityInputHandler();
		EntityInputAdapter menuItemAdapter = new EntityInputAdapter(){
			@Override
			public boolean clicked(BaseEntity entity, float x, float y) {
				if(entity instanceof EntityMenuItem){
					EntityMenuItem item = (EntityMenuItem)entity;
					
					System.out.println(item.getName());
				}
				
				return super.clicked(entity, x, y);
			}
		};
		
		for(int i = 0; i < count; i++){
			//Setup position
			pos.x = MathUtils.random(worldBounds.x,worldBounds.width);
			pos.y = MathUtils.random(worldBounds.y,worldBounds.height);
			
			//Setup bounds
			float wide = MathUtils.random(minScale*maxWidth, maxWidth);
			float high = MathUtils.random(minScale*maxHeight, maxHeight);
			localBounds.x = -wide/2;
			localBounds.y = -high/2;
			localBounds.width = wide;
			localBounds.height = high;
			
			float buttonSize = 20;
			//Setup Menu
			EntityMenuItem item1 = new EntityMenuItem();
			item1.setLocalbounds(new Rectangle(-buttonSize/2, -buttonSize/2, buttonSize, buttonSize));
			item1.setOffset(new Vector2(wide,high));
			item1.setInputHandler(menuItemAdapter);
			item1.setName("Item 1");
			
			EntityMenuItem item2 = new EntityMenuItem();
			item2.setLocalbounds(new Rectangle(-buttonSize/2, -buttonSize/2, buttonSize, buttonSize));
			item2.setOffset(new Vector2(-wide,high));
			item2.setInputHandler(menuItemAdapter);
			item2.setName("Item 2");
			
			EntityMenu menu = new EntityMenu();
			menu.addMenuItem(item1);
			menu.addMenuItem(item2);
			
			BaseEntity e = new BaseEntity();
			e.setPos(pos.x, pos.y);
			e.setLocalbounds(localBounds);
			e.setInputHandler(move);
			e.setTouchable(true);//MathUtils.randomBoolean());
			e.setMenu(menu);
			
			menu.setOwner(e);
			
			
			addEntity(e);
		}		
	}
	
	public void addEntity(BaseEntity e){
		synchronized (entities) {
			entities.add(e);
			EntityInputManager.getInst().addEntity(e);
		}
	}
	
	public void removeEntity(BaseEntity e){
		synchronized (entities) {
			entities.remove(e);
			EntityInputManager.getInst().removeEntity(e);
		}
	}
	public void render(EntityRender render, Camera c){
		render.startRender(c);
		synchronized (entities) {
			for(BaseEntity e : entities){
				render.render(e);
			}
		}
		render.endRender();
	}
}
