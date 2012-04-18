package com.joey.testing.game;

import com.badlogic.gdx.Gdx;
import com.joey.testing.game.cellSpace.CellSpacePartition;
import com.joey.testing.game.entities.BaseGameEntity;
import com.joey.testing.game.entities.Vehicle;
import com.joey.testing.game.shapes.Rectangle2D;
import com.joey.testing.game.shapes.Vector2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameWorld {
	ArrayList<Vehicle> vehicles;
	ArrayList<BaseGameEntity> obstacles;
	ArrayList<Wall2D> walls;
	CellSpacePartition<Vehicle> cellSpace;
	float wideX;
	float wideY;
	boolean paused = false;
	int maxCellDepth = 5;
	public GameWorld() {
		vehicles = new ArrayList<Vehicle>();
		obstacles = new ArrayList<BaseGameEntity>();
		walls = new ArrayList<Wall2D>();
		cellSpace = new CellSpacePartition<Vehicle>();
	}

	public void update(float updateTime) {
		if (paused) {
			return;
		}
		for (int i = 0; i < vehicles.size(); i++) {
			Vehicle v = vehicles.get(i);
			v.update(updateTime);
		}
	}
	
	public void updateCellSpace(){
		cellSpace.updateAllEntities();
	}
	
	public CellSpacePartition<Vehicle> getCellSpacePartition(){
		return cellSpace;
	}
	
	public void setSize(float wideX, float wideY){
		this.wideX=wideX;
		this.wideY=wideY;
		Rectangle2D r = new Rectangle2D(0, 0,wideX,
				wideY);
		cellSpace.setSize(r, maxCellDepth);
	}

	public float getWideX() {
		return wideX;
	}

	public float getWideY() {
		return wideY;
	}

	public ArrayList<Vehicle> getVehicles() {
		// TODO Auto-generated method stub
		return vehicles;
	}
	
	public void addVehicle(Vehicle v){
		vehicles.add(v);
		cellSpace.addEntity(v);
	}
	
	public void removeVehicle(Vehicle v){
		vehicles.remove(v);
		cellSpace.removeEntity(v);
	}

	public void setMaxCellDepth(int i) {
		maxCellDepth = i;
	}

}
