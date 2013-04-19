package com.emptyPockets.test.controls.inputs.handlers;

import com.emptyPockets.test.controls.entities.BaseEntity;

public class MoveEntityInputHandler implements EntityInputHandler {

	@Override
	public boolean touchDown(BaseEntity entity, float x, float y) {
		if(entity.contains(x, y)){
			return true;
		}else{
			if(entity.isSelected()){
				entity.unSelectEntity();
			}
			return false;
		}
	}

	@Override
	public boolean touchUp(BaseEntity entity, float x, float y) {
		return entity.contains(x, y);
	}

	@Override
	public boolean clicked(BaseEntity entity, float x, float y) {
		if(entity.contains(x, y)){
			entity.selectEntity();
			return true;
		}
		return false;
	}

	@Override
	public boolean dragged(BaseEntity entity, float x, float y,float offX,float offY) {
		entity.setPos(x+offX, y+offY);
		return true;
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
