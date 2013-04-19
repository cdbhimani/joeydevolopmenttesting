package com.emptyPockets.test.controls.inputs.handlers;

import com.emptyPockets.test.controls.entities.BaseEntity;

public interface EntityInputHandler {
	public boolean touchDown(BaseEntity entity, float x, float y);
	public boolean touchUp(BaseEntity entity, float x, float y);
	public boolean clicked(BaseEntity entity, float x, float y);
	public boolean dragged(BaseEntity entity, float x, float y, float offX,float offY);
	
	public void selected(BaseEntity entity);
	public void unSelected(BaseEntity entity);
}
