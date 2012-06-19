package com.joey.aitesting.game;

import java.util.ArrayList;

import com.joey.aitesting.game.cellSpace.QuadTree;
import com.joey.aitesting.game.entities.Obstacle;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.entities.Wall2D;
import com.joey.aitesting.game.shapes.Rectangle2D;

public class GameWorld {
	public static final int MAX_CELL_COUNT = 2;
	ArrayList<Vehicle> vehicles;
	ArrayList<Obstacle> obstacles;
	ArrayList<Wall2D> walls;
	public QuadTree<Vehicle> quadTree;
	public Rectangle2D worldBounds;
	boolean paused = false;

	public GameWorld(Rectangle2D worldBounds) {
		vehicles = new ArrayList<Vehicle>();
		obstacles = new ArrayList<Obstacle>();
		walls = new ArrayList<Wall2D>();
		quadTree = new QuadTree<Vehicle>(worldBounds, MAX_CELL_COUNT);
		setWorldSize(worldBounds);
	}

	public void update(float updateTime) {
		if (paused) {
			return;
		}
		
		for (int i = 0; i < vehicles.size(); i++) {	
			Vehicle v = vehicles.get(i);
			v.update(updateTime);
		}
		
		updateQuadTree();
	}

	public void updateQuadTree() {
		quadTree.rebuild();
	}

	public void setWorldSize(Rectangle2D worldBounds) {

		this.worldBounds = worldBounds;
		worldBounds.ensureOrder();
		quadTree.setWorldSize(worldBounds, MAX_CELL_COUNT);
	}

	public ArrayList<Vehicle> getVehicles() {
		// TODO Auto-generated method stub
		return vehicles;
	}
	public ArrayList<Obstacle> getObstacles() {
		// TODO Auto-generated method stub
		return obstacles;
	}
	public ArrayList<Wall2D> getWalls() {
		// TODO Auto-generated method stub
		return walls;
	}

	public void addVehicle(Vehicle v) {
		vehicles.add(v);
		quadTree.addEntity(v);
	}

	public void removeVehicle(Vehicle v) {
		vehicles.remove(v);
		quadTree.removeEntity(v);
	}

	

	public void addWall(Wall2D w) {
		synchronized (walls) {
			walls.add(w);
		}
	}

}
