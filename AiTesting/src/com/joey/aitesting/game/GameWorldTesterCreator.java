package com.joey.aitesting.game;

import java.util.ArrayList;

import com.joey.aitesting.MyGdxGame;
import com.joey.aitesting.game.entities.Obstacle;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.entities.Wall2D;
import com.joey.aitesting.game.entities.WaypointPath;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steering.behaviors.FollowPath;

public class GameWorldTesterCreator {

	public static float MAX_VEL = 300;
	public static float MAX_FORCE = 1000f;

	public static void CreateGameWorld(GameWorld world) {
//		 CreateTestWorld(world);
//		 CreatePathFollowWorld(world);
//		 CreateOffsetPursuitWorld(world);
//		 CreateInterposeWorld(world);
		CreateHideWorld(world);
//		CreateWallAvoidanceWorld(world);
	}

	private static void CreateEntity(GameWorld world, float maxVel,
			float maxForce) {
//		 CreateTestEntity(world, maxVel, maxForce);
//		 CratePathFollowEntity(world, maxVel, maxForce);
//		 CrateOffsetPursuitEntity(world, maxVel, maxForce);
//		 CreateInterposeEntity(world, maxVel, maxForce);
		CreateHideEntity(world, maxVel, maxForce);
//		CreateWallAvoidanceEntity(world, maxVel, maxForce);
	}

	public static void CreateHideWorld(GameWorld world) {
		
		Wall2D.addRectangle(world, world.worldBounds, 20f, true);
		addEntity(world, 2, GameWorldTesterCreator.MAX_VEL,
				GameWorldTesterCreator.MAX_FORCE);
		
		addObstacles(world, 3, 100);
	}

	public static void CreateHideEntity(GameWorld world, float maxVel,
			float maxForce) {
		Vehicle entity = new Vehicle(world);
		entity.pos.setLocation(world.worldBounds.getInset(100).getRandomPos());
		entity.maxSpeed = maxVel;
		entity.maxForce = maxForce;

		entity.mass = 1;
		entity.scale = new Vector2D(1, 1);

		entity.steering.feelerCount = 3;
		entity.steering.feelerFOV = (float)(Math.PI/2);
		entity.steering.feelerLength = 100;
		
		entity.steering.wallAvoidance.enabled = true;
		entity.steering.wallAvoidance.forceWeight = 100;
		
		int entityCount = world.vehicles.size();
		if(entityCount == 0){
			entity.steering.drawBehaviors = true;
			
			entity.steering.obstacleAvoidance.enabled= true;
			entity.steering.obstacleAvoidance.obstacleSearchBoxDistance = 10;
			entity.steering.obstacleAvoidance.forceWeight = 2000;
			
			entity.steering.wander.enabled = true;
			entity.steering.wander.wanderDistance = 100;
			entity.steering.wander.wanderRadius = 60;
			entity.steering.wander.wanderJitter = 30;
			
		} else{
			entity.steering.obstacleAvoidance.enabled = false;
			entity.steering.obstacleAvoidance.obstacleSearchBoxDistance = 50;
			entity.steering.obstacleAvoidance.forceWeight = 20;
			
			entity.steering.hide.enabled = true;
			entity.steering.hide.forceWeight = 1000;
			entity.steering.hide.hideVehicle = world.getVehicles().get(0);
		}

		entity.vel.setLocation(
				(float) (entity.maxSpeed * (1 - 2 * Math.random())),
				(float) (entity.maxSpeed * (1 - 2 * Math.random())));
		world.addVehicle(entity);
	}

	public static void CreateTestWorld(GameWorld world) {

	}

	public static void CreateTestEntity(GameWorld world, float maxVel,
			float maxForce) {
		Vehicle entity = new Vehicle(world);
		entity.pos.setLocation(world.worldBounds.getInset(100).getRandomPos());
		entity.maxSpeed = maxVel;
		entity.maxForce = maxForce;

		entity.mass = 1;
		entity.scale = new Vector2D(1, 1);

		entity.steering.seperation.forceWeight = 1f;
		entity.steering.alignment.forceWeight = 1f;
		entity.steering.cohesion.forceWeight = 1f;

		entity.steering.seperation.enabled = true;
		entity.steering.alignment.enabled = true;
		entity.steering.cohesion.enabled = true;
		entity.steering.neighborDetectionRadius = 100;
		
		entity.pos.x = entity.steering.neighborDetectionRadius*(float)Math.random();
		entity.pos.y = entity.steering.neighborDetectionRadius*(float)Math.random();
		
		if(entity.world.getVehicles().size() > 0){
			entity.steering.drawBehaviors= false;
		}else{
			entity.steering.drawBehaviors= true;
		}
		entity.vel.setLocation(
				(float) (entity.maxSpeed * (1 - 2 * Math.random())),
				(float) (entity.maxSpeed * (1 - 2 * Math.random())));
		world.addVehicle(entity);
	}

	public static void CreateInterposeWorld(GameWorld world) {
		addEntity(world, 3, GameWorldTesterCreator.MAX_VEL,
				GameWorldTesterCreator.MAX_FORCE);
	}

	public static void CreateInterposeEntity(GameWorld world, float maxVel,
			float maxForce) {
		Vehicle entity = new Vehicle(world);
		entity.pos.setLocation(world.worldBounds.getInset(100).getRandomPos());
		entity.maxSpeed = maxVel;
		entity.maxForce = maxForce;

		entity.mass = 1;
		entity.scale = new Vector2D(1, 1);

		int entityCount = world.vehicles.size();
		switch (entityCount % 3) {
		case 0:
			entity.steering.wander.enabled= true;
			entity.steering.wander.wanderDistance = 100;
			entity.steering.wander.wanderRadius = 60;
			entity.steering.wander.wanderJitter = 30;
			entity.steering.drawBehaviors= true;
			break;
		case 1:
			// entity.steering.useOffsetPursuit = true;
			// entity.steering.offsetPursuitWeight = 10f;
			// entity.steering.offsetPursuitVehicle =
			// world.vehicles.get(entityCount - 1);
			// entity.steering.offsetPursuitOffset = new Vector2D(0,100);
			//
			//
			entity.steering.wander.enabled = true;
			entity.steering.wander.wanderDistance = 100;
			entity.steering.wander.wanderRadius = 60;
			entity.steering.wander.wanderJitter = 30;
			entity.steering.drawBehaviors= true;
			break;
		case 2:
			entity.steering.interpose.enabled = true;
			entity.steering.interpose.interposeVehicleA = world.vehicles.get(entityCount - 1);
			entity.steering.interpose.interposeVehicleB = world.vehicles.get(entityCount - 2);
			entity.steering.interpose.forceWeight = 10;
			break;

		}

		entity.vel.setLocation(
				(float) (entity.maxSpeed * (1 - 2 * Math.random())),
				(float) (entity.maxSpeed * (1 - 2 * Math.random())));
		world.addVehicle(entity);
	}

	public static void CreateOffsetPursuitWorld(GameWorld world) {
		addEntity(world, 2, GameWorldTesterCreator.MAX_VEL,
				GameWorldTesterCreator.MAX_FORCE);
	}

	public static void CrateOffsetPursuitEntity(GameWorld world, float maxVel,
			float maxForce) {
		Vehicle entity = new Vehicle(world);
		entity.pos.setLocation(world.worldBounds.getInset(100).getRandomPos());
		entity.maxSpeed = maxVel;
		entity.maxForce = maxForce;

		entity.mass = 1;
		entity.scale = new Vector2D(1, 1);

		int entityCount = world.vehicles.size();
		if (entityCount == 0) {// Create a path following Leader
			WaypointPath path = new WaypointPath();
			for (int i = 0; i < 3; i++) {
				path.addPoint(world.worldBounds.getRandomPos());
			}
			path.loop = true;

			entity.steering.wander.enabled= true;
			entity.steering.wander.wanderDistance = 100;
			entity.steering.wander.wanderRadius = 60;
			entity.steering.wander.wanderJitter = 30;
			entity.steering.drawBehaviors= true;

			entity.steering.followPath.forceWeight = 10f;
			entity.steering.followPath.enabled= false;
			entity.steering.followPath.path = path;
			entity.steering.followPath.waypointDistance = 10;
			entity.steering.drawBehaviors = true;
		} else {
			entity.maxSpeed *= 4;
			entity.steering.offsetPursuit.enabled= true;
			entity.steering.offsetPursuit.forceWeight = 10f;
			entity.steering.offsetPursuit.leader= world.vehicles
					.get(entityCount - 1);
			float box = 50;
			entity.steering.offsetPursuit.offset = new Vector2D(-box, 0);
		}

		entity.vel.setLocation(
				(float) (entity.maxSpeed * (1 - 2 * Math.random())),
				(float) (entity.maxSpeed * (1 - 2 * Math.random())));
		world.addVehicle(entity);

	}

	public static void CreatePathFollowWorld(GameWorld world) {
		addEntity(world, 1, GameWorldTesterCreator.MAX_VEL,
				GameWorldTesterCreator.MAX_FORCE);
	}

	public static void CratePathFollowEntity(GameWorld world, float maxVel,
			float maxForce) {
		WaypointPath path = new WaypointPath();
		for (int i = 0; i < 10; i++) {
			path.addPoint(world.worldBounds.getInset(50).getRandomPos());
		}
		path.loop = true;

		Vehicle entity = new Vehicle(world);
		entity.pos.setLocation(world.worldBounds.getRandomPos());
		entity.maxSpeed = maxVel;
		entity.maxForce = maxForce;
		entity.vel.setLocation(
				(float) (entity.maxSpeed * (1 - 2 * Math.random())),
				(float) (entity.maxSpeed * (1 - 2 * Math.random())));
		entity.mass = 1;
		entity.scale = new Vector2D(1, 1);

		entity.steering.followPath.enabled = true;
		entity.steering.followPath.path = path;
		entity.steering.followPath.waypointDistance = 10;
		entity.steering.followPath.forceWeight = 10;

		entity.steering.drawBehaviors = true;
		world.addVehicle(entity);

	}

	public static void CreateWallAvoidanceWorld(GameWorld world) {
		// Add outer Boundary
		//Wall2D.addRectangle(world, world.worldBounds, 20, true);

		// Add random walls and ensure they dont overlap
		ArrayList<Rectangle2D> hold = new ArrayList<Rectangle2D>();
		while (hold.size() < 3) {
			Vector2D p = world.worldBounds.getRandomPos();
			float size = world.worldBounds.getWidth()/20;
			Rectangle2D r = new Rectangle2D(p.x, p.y, p.x + size, p.y + size);

			if (world.worldBounds.contains(r)) {
				boolean ok = true;
				for (Rectangle2D r2 : hold) {
					if (r2.intersects(r)) {
						ok = false;
					}
				}
				if (ok) {
					hold.add(r);
				}
			}

		}

		for (Rectangle2D r : hold) {
			Wall2D.addRectangle(world, r, 0, false);
		}
	}

	public static void addObstacles(GameWorld world, int count, float radius) {
		synchronized (world.obstacles) {
			for (int i = 0; i < count; i++) {
				Obstacle ob = new Obstacle(world.worldBounds.getRandomPos(),
						radius);
				world.obstacles.add(ob);
			}
		}
	}

	// Add a new viechle in random position
	public static void addEntity(GameWorld world, int count, float maxVel,
			float maxForce) {
		synchronized (world.quadTree) {
			for (int i = 0; i < count; i++) {
				CreateEntity(world, maxVel, maxForce);
			}

		}
	}

	public static void CreateWallAvoidanceEntity(GameWorld world, float maxVel,
			float maxForce) {
		Vehicle entity = new Vehicle(world);
		entity.pos.setLocation(world.worldBounds.getRandomPos());
		entity.maxSpeed = maxVel;
		entity.maxForce = maxForce;
		entity.mass = 1;
		entity.scale = new Vector2D(1, 1);

		entity.steering.wallAvoidance.enabled= true;
		entity.steering.wallAvoidance.forceWeight = 10;
		entity.steering.wallAvoidance.requiredUpdateTime = 0;
		entity.steering.feelerCount = 3;
		entity.steering.feelerFOV = (float) Math.PI;
		entity.steering.feelerLength = maxVel;

		if (world.vehicles.size() > 0) {
			Vehicle v = world.vehicles.get(0);
			if (v != entity) {
				entity.steering.evade.enabled = true;
				entity.steering.evade.forceWeight = 30;
				entity.steering.evade.evadeVehicle = v;
				entity.steering.evade.evadePanicDistance = 2*maxVel;
				entity.steering.evade.useEvadePanic = true;

		
				entity.steering.seperation.forceWeight = 1f;
				entity.steering.alignment.forceWeight = 2f;
				entity.steering.cohesion.forceWeight = 1f;

				entity.steering.seperation.enabled = true;
				entity.steering.alignment.enabled = true;
				entity.steering.cohesion.enabled = true;
				entity.steering.neighborDetectionRadius = maxVel;
				
				if(entity.world.getVehicles().size() > 0){
					entity.steering.drawBehaviors= false;
				}else{
					entity.steering.drawBehaviors= true;
				}
			}
		} else {
			entity.steering.wander.enabled = true;
			entity.steering.wander.wanderDistance = 100;
			entity.steering.wander.wanderRadius = 60;
			entity.steering.wander.wanderJitter = 30;
			entity.steering.drawBehaviors= true;
			entity.maxSpeed *= 2;
		}

		entity.vel.setLocation(
				(float) (entity.maxSpeed * (1 - 2 * Math.random())),
				(float) (entity.maxSpeed * (1 - 2 * Math.random())));
		world.addVehicle(entity);
	}

	// Add remove random set of viechle in random position
	public static void removeVehicles(GameWorld world, int count) {
		synchronized (world.quadTree) {
			for (int i = 0; i < count; i++) {
				if (world.vehicles.size() > 1) {
					world.removeVehicle(world.vehicles
							.get(1 + (int) ((world.vehicles.size() - 1) * Math
									.random())));
				}
			}

		}
	}
}
