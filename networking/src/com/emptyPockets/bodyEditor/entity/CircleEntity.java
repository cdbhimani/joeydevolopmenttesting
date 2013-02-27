package com.emptyPockets.bodyEditor.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;

public class CircleEntity extends BaseEntity{
	Circle circle;

	public CircleEntity(){
		setCircle(new Circle());
	}
	@Override
	public Body createBody() {
		// TODO Auto-generated method stub
		return null;
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}
}
