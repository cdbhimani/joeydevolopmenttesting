package com.emptyPockets.test.soccer.entity.bounds;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.math.Rectangle;
import com.emptyPockets.test.controls.entities.BaseEntity;

public abstract class Bounds {
	Rectangle aaBounds;
	
	public Bounds(){
		aaBounds = new Rectangle();
	}
	
	public void containsAABB(Bounds other){
		aaBounds.contains(other.aaBounds);
	}
	
	public void intersectsAAbb(Bounds other){
		aaBounds.overlaps(other.aaBounds);
	}
	
	public void update(BaseEntity owner){
		updateShape(owner);
		updateAABB(owner, aaBounds);
	}
	protected abstract void updateShape(BaseEntity owner);
	protected abstract void updateAABB(BaseEntity owner,Rectangle aaBounds);
	
	public abstract boolean contains(Bounds other);
	public abstract boolean intersects(Bounds other);
}
