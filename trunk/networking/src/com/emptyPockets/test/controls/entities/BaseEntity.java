package com.emptyPockets.test.controls.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.test.controls.inputs.handlers.EntityInputHandler;
import com.emptyPockets.test.controls.menus.EntityMenu;

public class BaseEntity {
	String name;
	Rectangle localBounds;
	Vector2 pos;
	EntityMenu menu;

	boolean selectable = false;
	boolean touchable = false;

	boolean selected = false;

	Rectangle worldBounds;
	Vector2 touchDownOffset;
	EntityInputHandler inputHandler;

	public BaseEntity() {
		localBounds = new Rectangle();
		worldBounds = new Rectangle();
		pos = new Vector2();
		touchDownOffset = new Vector2();
	}

	/**
	 * NOTE : World bounds is only a tempary variable and is modified inside the
	 * base entity (see setPos)
	 * 
	 * @return
	 */
	public Rectangle getWorldBounds() {
		return worldBounds;
	}

	public float getPosX() {
		return pos.x;
	}

	public float getPosY() {
		return pos.y;
	}

	public void setPos(float x, float y) {
		pos.x = x;
		pos.y = y;
		update();
	}

	public void setLocalbounds(Rectangle localBounds) {
		this.localBounds.set(localBounds);
		update();
	}

	public void update() {
		worldBounds.x = pos.x + localBounds.x;
		worldBounds.y = pos.y + localBounds.y;
		worldBounds.width = localBounds.width;
		worldBounds.height = localBounds.height;
		if (menu != null && menu.isVisible()) {
			menu.update();
		}
	}

	public void selectEntity() {
		selected = true;
		if (inputHandler != null) {
			inputHandler.selected(this);
		}
	}

	public void unSelectEntity() {
		selected = false;
		if (inputHandler != null) {
			inputHandler.unSelected(this);
		}
	}

	public boolean contains(float x, float y) {
		boolean returnValue = false;
		if (worldBounds.contains(x, y)) {
			returnValue = true;
		}

		if (menu != null && menu.isVisible()) {
			if (menu.contains(x, y)) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	public boolean clicked(float x, float y) {
		boolean returnValue = false;
		if (inputHandler != null) {
			if (inputHandler.clicked(this, x, y)) {
				returnValue = true;
			}
		}
		if (menu != null && menu.isVisible()) {
			if (menu.contains(x, y)) {
				if (menu.clicked(this, x, y)) {
					returnValue = true;
				}
			}
		}
		return returnValue;
	}

	public boolean touchDown(float x, float y) {
		touchDownOffset.x = getPosX() - x;
		touchDownOffset.y = getPosY() - y;
		boolean returnValue = false;
		if (inputHandler != null) {
			if (inputHandler.touchDown(this, x, y)) {
				returnValue = true;
			}
		}
		if (menu != null && menu.isVisible()) {
			if (menu.contains(x, y)) {
				if (menu.touchDown(this, x, y)) {
					returnValue = true;
				}
			}
		}
		return returnValue;
	}

	public boolean touchUp(float x, float y) {
		boolean returnValue = false;
		if (inputHandler != null) {
			if (inputHandler.touchUp(this, x, y)) {
				returnValue = true;
			}
		}
		if (menu != null && menu.isVisible()) {
			if (menu.contains(x, y)) {
				if (menu.touchUp(this, x, y)) {
					returnValue = true;
				}
			}
		}
		return returnValue;
	}

	public boolean dragged(float x, float y) {
		boolean returnValue = false;
		if (inputHandler != null) {
			if (inputHandler.dragged(this, x, y, touchDownOffset.x, touchDownOffset.y)) {
				returnValue = true;
			}
		}
		if (menu != null && menu.isVisible()) {
			if (menu.contains(x, y)) {
				if (menu.dragged(this, x, y, touchDownOffset.x, touchDownOffset.y)) {
					returnValue = true;
				}
			}
		}
		return returnValue;
	}

	public EntityMenu getMenu() {
		return menu;
	}

	public void setMenu(EntityMenu menu) {
		this.menu = menu;
	}

	public EntityInputHandler getInputHandler() {
		return inputHandler;
	}

	public void setInputHandler(EntityInputHandler inputHandler) {
		this.inputHandler = inputHandler;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public boolean isTouchable() {
		return touchable;
	}

	public void setTouchable(boolean touchable) {
		this.touchable = touchable;
	}

	public boolean isSelected() {
		return selected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
