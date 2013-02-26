package com.emptyPockets.bodyEditor.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

public class RectangleEntity extends Entity {

	Rectangle rectangle;
	
	public RectangleEntity() {
		setRectangle(new Rectangle());
	}
	
	@Override
	public Body createBody() {
		return null;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

}
