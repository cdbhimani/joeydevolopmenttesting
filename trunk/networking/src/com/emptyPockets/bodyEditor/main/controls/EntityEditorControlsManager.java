package com.emptyPockets.bodyEditor.main.controls;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.emptyPockets.bodyEditor.entity.Entity;
import com.emptyPockets.bodyEditor.entity.PolygonEntity;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;
import com.emptyPockets.utils.OrthoCamController;

public class EntityEditorControlsManager {
	EntityEditorScreen owner;
	
	OrthoCamController cameraControl;
	PolygonControler polygonControl;
	
	InputMultiplexer inputMultiplexer;

	
	public EntityEditorControlsManager(EntityEditorScreen editor){
		this.owner = editor;
		cameraControl = new OrthoCamController(owner.getEditorCamera());
		inputMultiplexer = new InputMultiplexer();
		polygonControl = new PolygonControler(owner);
				
		attachListeners();
	}
	
	private void attachListeners(){
		
		if(owner.getEntity() instanceof PolygonEntity){
			polygonControl.attach(inputMultiplexer);
		}
		
		inputMultiplexer.addProcessor(cameraControl);
	}
	
	private void detachListeners(){
		inputMultiplexer.clear();
	}
	
	public void update(){
		detachListeners();
		attachListeners();
	}

	public InputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	public void setInputMultiplexer(InputMultiplexer inputMultiplexer) {
		this.inputMultiplexer = inputMultiplexer;
	}

	public void drawScreen(ShapeRenderer shape, Entity entity) {
		if(entity != null){
			if(entity instanceof PolygonEntity){
				polygonControl.drawPolygonEntity(shape, (PolygonEntity) entity);
			}
		}
		
	}
}
