package com.emptyPockets.test.controls.inputs;

import java.util.ArrayList;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.gui.OrthographicsCameraConvertor;
import com.emptyPockets.test.controls.entities.BaseEntity;

public class EntityInputManager implements InputProcessor {
	static EntityInputManager manager = null;
	
	ArrayList<BaseEntity> entities;
	ArrayList<BaseEntity> touchedEntities;
	BaseEntity currentTouch;
	
	OrthographicsCameraConvertor converter;
	Vector2 lastCamera;
	boolean mouseDragged = false;
	
	public EntityInputManager(OrthographicsCameraConvertor converter) {
		this.converter = converter;
		lastCamera = new Vector2();
		entities = new ArrayList<BaseEntity>();
		touchedEntities = new ArrayList<BaseEntity>();
	}

	public static void setup(OrthographicsCameraConvertor convertor){
		if(manager == null){
			manager = new EntityInputManager(convertor);
		}
	}
	public static EntityInputManager getInst(){
		return manager;
	}
	public void addEntity(BaseEntity t) {
		entities.add(t);
	}

	public void removeEntity(BaseEntity t) {
		entities.remove(t);
	}

	public void attach(InputMultiplexer input) {
		input.addProcessor(this);
	}

	public void detach(InputMultiplexer input) {
		input.removeProcessor(this);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	public void grabEntity(BaseEntity entity){
		synchronized (touchedEntities) {
			touchedEntities.add(entity);
		}
	}
	
	public void releaseEntity(BaseEntity entity){
		synchronized (touchedEntities) {
			touchedEntities.add(entity);
		}
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean returnValue = false;
		mouseDragged = false;
		converter.camToPanel(screenX, screenY, lastCamera);
		for(BaseEntity e : entities){
			if(e.isTouchable()){
				if(e.touchDown(lastCamera.x, lastCamera.y) && e.contains(lastCamera.x, lastCamera.y)){
					grabEntity(e);
					returnValue = true;
				}
			}
		}
		return returnValue;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean returnValue = false;
		converter.camToPanel(screenX, screenY, lastCamera);
		
		synchronized (touchedEntities) {
			for(BaseEntity e : touchedEntities){
				if(e.isTouchable()){
					if(e.touchUp(lastCamera.x, lastCamera.y)){
						returnValue = true;
					}
					if(mouseDragged == false && e.clicked(lastCamera.x, lastCamera.y)){
						returnValue = true;
					}
				}
			}
			touchedEntities.clear();
		}
		return returnValue;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mouseDragged = true;
		
		boolean returnValue = false;
		converter.camToPanel(screenX, screenY, lastCamera);
		synchronized (touchedEntities) {
			for(BaseEntity e : touchedEntities){
				if(e.isTouchable()){
					if(e.dragged(lastCamera.x, lastCamera.y)){
						returnValue = true;
					}
				}
			}	
		}
		return returnValue;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
