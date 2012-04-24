package com.joey.aitesting.game;

import com.badlogic.gdx.Gdx;
import com.joey.aitesting.game.cellSpace.CellSpacePartition;
import com.joey.aitesting.game.cellSpace.QuadTree;
import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.graphics.QuadTreeViewer;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.joey.aitesting.game.shapes.Vector2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameWorld {
	public static final int MAX_CELL_COUNT = 3;
	ArrayList<Vehicle> vehicles;
	ArrayList<BaseGameEntity> obstacles;
	ArrayList<Wall2D> walls;
	QuadTree<Vehicle> quadTree;
	Rectangle2D worldBounds;
	boolean paused = false;
	
	public GameWorld(Rectangle2D worldBounds) {
		vehicles = new ArrayList<Vehicle>();
		obstacles = new ArrayList<BaseGameEntity>();
		walls = new ArrayList<Wall2D>();
		quadTree = new QuadTree<Vehicle>(worldBounds,MAX_CELL_COUNT);
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
	
	public void updateQuadTree(){
		quadTree.rebuild();
	}
	
	public void setWorldSize(Rectangle2D worldBounds){

		this.worldBounds = worldBounds;
		quadTree.setWorldSize(worldBounds, MAX_CELL_COUNT);
	}

	public ArrayList<Vehicle> getVehicles() {
		// TODO Auto-generated method stub
		return vehicles;
	}
	
	public void addVehicle(Vehicle v){
		vehicles.add(v);
		quadTree.addEntity(v);
	}
	
	public void removeVehicle(Vehicle v){
		vehicles.remove(v);
		quadTree.removeEntity(v);
	}

	//Add a new viechle in random position
	public void addVehicles(int count) {
		
	}

	//Add remove random set of viechle in random position
	public void removeVehicles(int count) {
		
	}

}
