package com.emptyPockets.engine2D.spatialPartitions.stores;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.math.Rectangle;
import com.emptyPockets.engine2D.entities.types.BaseGameEntity;
import com.emptyPockets.engine2D.shapes.Rectangle2D;
import com.emptyPockets.engine2D.spatialPartitions.bounds.BoundedBody;

public abstract class EntityStore<T extends BoundedBody> {

	public abstract Collection<T> getAllEntities();
	public abstract int getEntityCount();
	public abstract void addEntity(T ent);
	public abstract void removeEntity(T ent);
	public abstract void update();
	public abstract void clear();
	public abstract void getEntitiesIntersectsRegionUsingAABoundingBox(Rectangle r, Collection<T> found);
	public abstract void getEntitiesIntersectsRegion(Rectangle r, Collection<T> found);
	public abstract void getEntitiesContainedInRegionUsingAABoundingBox(Rectangle r, Collection<T> found);
	public abstract void getEntitiesContainedInRegion(Rectangle r, Collection<T> found);
}
