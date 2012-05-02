package com.joey.aitesting.game;

import com.badlogic.gdx.Gdx;
import com.joey.aitesting.game.cellSpace.QuadTree;
import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.entities.Obstacle;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.graphics.GameWorldViewer;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.joey.aitesting.game.shapes.Vector2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

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

	// Add a new viechle in random position
	public void addVehicles(int count, float maxVel, float maxForce) {
		synchronized (quadTree) {
			for (int i = 0; i < count; i++) {

				Vehicle entity = new Vehicle(this);
				entity.pos.setLocation(worldBounds.getRandomPos());
				entity.vel.setLocation(
						(float) (maxVel * (1 - 2 * Math.random())),
						(float) (maxVel * (1 - 2 * Math.random())));

				entity.maxSpeed = maxVel;
				entity.maxForce = maxForce;
				entity.mass = 1;
				entity.scale = new Vector2D(1, 1);

//				if (vehicles.size() > 0) {
//					Vehicle v = vehicles.get(0);
//					if (v != entity) {
//						entity.steering.evadeVehicle= v;
//						entity.steering.useEvade = false;
						entity.vel.scale(10);
						entity.maxForce = 1000;
						entity.steering.seperationWeight = 8.19f;
						entity.steering.alignmentWeight = 0.92f;
						entity.steering.cohesionWeight= 2.37f;
						entity.steering.useSeperation = true;
						entity.steering.useAlignment = true;
						entity.steering.useCohesion = true;
						entity.steering.neighborRadius = 100;
//					}
//				} else {
//					// entity.steering.arrivePos = new Vector2D(0,0);
//					// entity.steering.useArrive = true
//
//					entity.maxSpeed *= 1;
//					entity.steering.useWander = true;
//					entity.steering.wanderDistance = 50;
//					entity.steering.wanderRadius = 90;
//					entity.steering.wanderJitter = 65;
//				}
//
				addVehicle(entity);
			}

		}
	}

	// Add remove random set of viechle in random position
	public void removeVehicles(int count) {
		synchronized (quadTree) {
			for (int i = 0; i < count; i++) {
				if (vehicles.size() > 1) {
					removeVehicle(vehicles
							.get(1 + (int) ((vehicles.size() - 1) * Math
									.random())));
				}
			}

		}
	}

}
