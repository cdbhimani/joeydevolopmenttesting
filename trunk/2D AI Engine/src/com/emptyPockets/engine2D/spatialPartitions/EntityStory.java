package com.emptyPockets.engine2D.spatialPartitions;

import java.util.Collection;

import com.emptyPockets.engine2D.entities.types.BaseGameEntity;
import com.emptyPockets.engine2D.shapes.Rectangle2D;

public abstract class EntityStory<T extends BaseGameEntity> {

	public abstract void addEntity(T ent);
	public abstract void removeEntity(T ent);
	public abstract void update();
	public abstract void clear();
	public abstract void getEntitiesInRegion(Rectangle2D r, Collection<T> found);
}
