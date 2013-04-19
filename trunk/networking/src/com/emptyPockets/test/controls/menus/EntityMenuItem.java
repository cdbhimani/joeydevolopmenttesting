package com.emptyPockets.test.controls.menus;

import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.test.controls.entities.BaseEntity;

public class EntityMenuItem extends BaseEntity {
	Vector2 offset;

	public EntityMenuItem(){
		this.offset = new Vector2();
	}
	
	public void setOffset(Vector2 offset){
		this.offset.set(offset);
	}
	
	public void updateMenuItem(EntityMenu menu) {
		if(menu != null && menu.getOwner() != null){
			setPos(menu.getOwner().getPosX()+offset.x, menu.getOwner().getPosY()+offset.y);	
		}
	}

}
