package com.emptyPockets.engine2D.spatialPartitions.bounds;

import com.badlogic.gdx.math.Rectangle;
import com.emptyPockets.engine2D.entities.types.BaseGameEntity;

public abstract class BoundingShape {
	Rectangle aaBoundingBox;

	public BoundingShape(){
		aaBoundingBox = new Rectangle();
	}
	
	public abstract void updateAABB(BaseGameEntity owner);
	public abstract void updateShape(BaseGameEntity owner);
	public abstract boolean intersects(Rectangle rect);
	public abstract boolean contains(Rectangle rect);
	
	
	public void update(BaseGameEntity owner){
		updateAABB(owner);
		updateShape(owner);
	}
	
	public boolean intersectsAABoundingBox(Rectangle rect){
		return rect.overlaps(aaBoundingBox);
	}
	
	public boolean containsAABoundingBox(Rectangle rect){
		return rect.contains(aaBoundingBox);
	}
}
