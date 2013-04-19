package com.emptyPockets.test.controls.inputs.handlers;

import com.emptyPockets.test.controls.entities.BaseEntity;

public class EntityMenuControlsInputHandler implements EntityInputHandler{

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
		if(entity.getMenu() != null){
			entity.getMenu().setVisible(true);
		}
	}

	@Override
	public void unSelected(BaseEntity entity) {			
		if(entity.getMenu() != null){
			entity.getMenu().setVisible(false);
		}
	}

}
