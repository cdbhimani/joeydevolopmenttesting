package com.emptyPockets.engine2D.spatialPartitions.stores.basic;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.math.Rectangle;
import com.emptyPockets.engine2D.entities.types.BaseGameEntity;
import com.emptyPockets.engine2D.shapes.Rectangle2D;
import com.emptyPockets.engine2D.spatialPartitions.bounds.BoundedBody;
import com.emptyPockets.engine2D.spatialPartitions.stores.EntityStore;

public class BasicEntityStore<T extends BoundedBody> extends EntityStore<T>{

	ArrayList<T> entities = new ArrayList<T>();
	@Override
	public void addEntity(T ent) {
		entities.add(ent);
	}

	@Override
	public void removeEntity(T ent) {
		entities.remove(ent);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void clear() {
		entities.clear();
	}

	@Override
	public void getEntitiesIntersectsRegion(Rectangle r, Collection<T> found) {
		for(T ent : entities){
			if(ent.getBoundingShape().intersects(r)){
				found.add(ent);
			}
		}
	}

	@Override
	public void getEntitiesIntersectsRegionUsingAABoundingBox(Rectangle r, Collection<T> found) {
		for(T ent : entities){
			if(ent.getBoundingShape().intersects(r)){
				found.add(ent);
			}
		}
	}

	@Override
	public void getEntitiesContainedInRegionUsingAABoundingBox(Rectangle r, Collection<T> found) {
		for(T ent : entities){
			if(ent.getBoundingShape().containsAABoundingBox(r)){
				found.add(ent);
			}
		}
	}

	@Override
	public void getEntitiesContainedInRegion(Rectangle r, Collection<T> found) {
		for(T ent : entities){
			if(ent.getBoundingShape().contains(r)){
				found.add(ent);
			}
		}
	}

	@Override
	public Collection<T> getAllEntities() {
		// TODO Auto-generated method stub
		return entities;
	}

	@Override
	public int getEntityCount() {
		// TODO Auto-generated method stub
		return entities.size();
	}
	
	
}

