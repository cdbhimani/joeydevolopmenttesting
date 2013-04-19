package com.emptyPockets.test.controls.menus;

import java.util.ArrayList;

import com.emptyPockets.test.controls.entities.BaseEntity;
import com.emptyPockets.test.controls.inputs.EntityInputManager;
import com.emptyPockets.test.controls.inputs.handlers.EntityInputHandler;

public class EntityMenu implements EntityInputHandler {
	BaseEntity owner;
	ArrayList<EntityMenuItem> items;
	boolean visible = false;

	public EntityMenu() {
		items = new ArrayList<EntityMenuItem>();
	}

	public void setOwner(BaseEntity owner) {
		this.owner = owner;
	}

	public BaseEntity getOwner() {
		return owner;
	}

	public ArrayList<EntityMenuItem> getMenuItems() {
		return items;
	}

	public void update() {
		synchronized (items) {
			for (EntityMenuItem item : items) {
				item.updateMenuItem(this);
			}
		}
	}

	public void addMenuItem(EntityMenuItem item) {
		synchronized (items) {
			items.add(item);
		}
	}

	public void removeMenuItem(EntityMenuItem item) {
		synchronized (items) {
			items.remove(item);
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		update();
	}

	@Override
	public boolean touchDown(BaseEntity entity, float x, float y) {
		boolean returnValue = false;
		synchronized (items) {
			for (EntityMenuItem item : items) {
				if (item.contains(x, y)) {
					if (item.touchDown(x, y)) {
						returnValue = true;
					}
				}
			}
		}
		return returnValue;
	}

	@Override
	public boolean touchUp(BaseEntity entity, float x, float y) {
		boolean returnValue = false;
		synchronized (items) {
			for (EntityMenuItem item : items) {
				if (item.contains(x, y)) {
					if (item.touchUp(x, y)) {
						returnValue = true;
					}
				}
			}
		}
		return returnValue;
	}

	@Override
	public boolean clicked(BaseEntity entity, float x, float y) {
		boolean returnValue = false;
		synchronized (items) {
			for (EntityMenuItem item : items) {
				if (item.contains(x, y)) {
					if (item.clicked(x, y)) {
						returnValue = true;
					}
				}
			}
		}
		return returnValue;
	}

	@Override
	public boolean dragged(BaseEntity entity, float x, float y, float offX, float offY) {
		boolean returnValue = false;
		synchronized (items) {
			for (EntityMenuItem item : items) {
				if (item.contains(x, y)) {
					if (item.dragged(x, y)) {
						returnValue = true;
					}
				}
			}
		}
		return returnValue;
	}

	@Override
	public void selected(BaseEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unSelected(BaseEntity entity) {
		// TODO Auto-generated method stub

	}

	public boolean contains(float x, float y) {
		boolean returnValue = false;
		synchronized (items) {
			for (EntityMenuItem item : items) {
				if (item.contains(x, y)) {
					returnValue = true;
				}
			}
		}
		return returnValue;
	}
}
