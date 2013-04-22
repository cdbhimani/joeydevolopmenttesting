package com.emptyPockets.test.soccer.entity.bounds;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.emptyPockets.test.controls.entities.BaseEntity;

public class CircleBounds extends Bounds {
	Circle localCircle;
	
	Circle worldCircle;
	
	public CircleBounds(Circle localCircle){
		this.localCircle=localCircle;
		worldCircle = new Circle();
	}


	@Override
	public boolean contains(Bounds other) {
		return false;
	}

	@Override
	public boolean intersects(Bounds other) {
		return false;
	}


	@Override
	protected void updateShape(BaseEntity owner) {
		worldCircle.x = localCircle.x+owner.getPosX();
	}


	@Override
	protected void updateAABB(BaseEntity owner, Rectangle aaBounds) {
		aaBounds.x = worldCircle.x-worldCircle.radius;
		aaBounds.y = worldCircle.y-worldCircle.radius;
		aaBounds.width = 2*worldCircle.radius;
		aaBounds.height = 2*worldCircle.radius;
	}

}
