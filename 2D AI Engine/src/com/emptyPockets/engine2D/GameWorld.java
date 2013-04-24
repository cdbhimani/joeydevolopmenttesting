package com.emptyPockets.engine2D;

import java.util.Collection;
import java.util.List;

import com.emptyPockets.engine2D.entities.types.Obstacle;
import com.emptyPockets.engine2D.entities.types.Vehicle;
import com.emptyPockets.engine2D.entities.types.Wall2D;
import com.emptyPockets.engine2D.shapes.Rectangle2D;
import com.emptyPockets.engine2D.spatialPartitions.stores.EntityStore;
import com.emptyPockets.engine2D.spatialPartitions.stores.basic.BasicEntityStore;

public class GameWorld {
	EntityStore<Vehicle> vehicles;
	EntityStore<Obstacle> obstacles;
	EntityStore<Wall2D> walls;
	
	public Rectangle2D worldBounds;
	boolean paused = false;

	public GameWorld(Rectangle2D worldBounds) {
		vehicles = new BasicEntityStore<Vehicle>();
		obstacles = new BasicEntityStore<Obstacle>();
		walls = new BasicEntityStore<Wall2D>();
		setWorldSize(worldBounds);
	}

	public void update(float updateTime) {
		if (paused) {
			return;
		}
		
		for (Vehicle v : vehicles.getAllEntities()) {	
			v.update(updateTime);
		}
	}

	public void setWorldSize(Rectangle2D worldBounds) {
		this.worldBounds = worldBounds;
		worldBounds.ensureOrder();
	}

	public void addVehicle(Vehicle v) {
		vehicles.addEntity(v);
	}

	public void removeVehicle(Vehicle v) {
		vehicles.removeEntity(v);
	}

	public void addWall(Wall2D w) {
		synchronized (walls) {
			walls.addEntity(w);
		}
	}

	public void getEntitiesInRegion(Rectangle2D hold, Collection<Vehicle> result){
		vehicles.getEntitiesContainedInRegion(hold.getRect(), result);
	}
	public List<Wall2D> getWalls() {
		// TODO Auto-generated method stub
		return (List<Wall2D>) walls.getAllEntities();
	}
	
	public List<Obstacle> getObstacles(){
		return (List<Obstacle>) obstacles.getAllEntities();
	}
}
