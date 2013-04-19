package com.emptyPockets.test.controls.render;

import com.badlogic.gdx.graphics.Camera;
import com.emptyPockets.test.controls.entities.BaseEntity;

public abstract class EntityRender {
	public abstract void startRender(Camera c);
	public abstract void endRender();	
	public abstract void render(BaseEntity e);
}
