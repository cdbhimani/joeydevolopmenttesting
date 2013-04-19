package com.emptyPockets.test.controls.inputs.handlers;

import com.emptyPockets.test.controls.entities.BaseEntity;

public class EntityInputAdapter implements EntityInputHandler {

	public EntityInputAdapter(){
	}
	
	
	@Override
	public boolean touchDown(BaseEntity entity, float x, float y) {
		return entity.contains(x, y);
	}

	@Override
	public boolean touchUp(BaseEntity entity, float x, float y) {
		return entity.contains(x, y);
	}

	@Override
	public boolean clicked(BaseEntity entity, float x, float y) {
		return entity.contains(x, y);
	}

	@Override
	public boolean dragged(BaseEntity entity, float x, float y, float offX, float offY) {
		return entity.contains(x, y);
	}

	@Override
	public void selected(BaseEntity entity) {
	}

	@Override
	public void unSelected(BaseEntity entity) {
	}

}
