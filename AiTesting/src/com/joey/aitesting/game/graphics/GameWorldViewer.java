package com.joey.aitesting.game.graphics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.joey.aitesting.game.GameWorld;
import com.joey.aitesting.game.cellSpace.QuadTree;
import com.joey.aitesting.game.cellSpace.QuadTreeNode;
import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.entities.Obstacle;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.entities.Wall2D;
import com.joey.aitesting.game.entities.WaypointPath;
import com.joey.aitesting.game.maths.Transformations;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steering.SteeringBehaviors;

public class GameWorldViewer {
	public GameWorld world;

	ShapeRenderer gridRender;
	SpriteBatch spriteBatch;

	private Texture spriteTexture;
	private TextureRegion spriteRegion;
	float spriteScale = 6;

	long lastUpdate = System.currentTimeMillis();
	float entitySize = 40;

	float wallNormSize = 30f;
	public boolean drawBorders = true;
	public boolean drawEntities = true;
	public boolean drawQuadTree = false;
	public boolean drawBehaviour = false;
	public boolean drawObstacles = true;
	public boolean drawWalls = true;

	public GameWorldViewer(GameWorld world) {
		this.world = world;
		gridRender = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		loadTextures();
	}

	public void loadTextures() {
		spriteTexture = new Texture(Gdx.files.internal("fish1.png"));
		spriteRegion = new TextureRegion(spriteTexture);
	}

	public void rebuildTree() {
		world.quadTree.rebuild();
	}

	private void renderGridInCell(GLCommon gl, Camera camera,
			Rectangle2D drawRegion, QuadTreeNode node) {
		if (drawRegion.intersects(node.region)) {
			if (node.isLeaf) {
				if (drawQuadTree) {
					gridRender.setColor(Color.RED);
					gridRender.begin(ShapeType.Rectangle);
					gridRender.rect(node.region.x1, node.region.y1,
							node.region.x2 - node.region.x1, node.region.y2
									- node.region.y1);
					gridRender.end();
				}
			} else {
				if (node.NW != null)
					renderGridInCell(gl, camera, drawRegion, node.NW);
				if (node.NE != null)
					renderGridInCell(gl, camera, drawRegion, node.NE);
				if (node.SW != null)
					renderGridInCell(gl, camera, drawRegion, node.SW);
				if (node.SE != null)
					renderGridInCell(gl, camera, drawRegion, node.SE);
			}

		}

	}

	private void drawEntity(GLCommon gl, Camera camera, Rectangle2D drawRegion,
			BaseGameEntity e) {
		Vehicle vehicle = (Vehicle) e;

		
		
		if (vehicle.steering.drawBehaviors) {
			
			if(vehicle.steering.followPath.enabled){
				WaypointPath path = vehicle.steering.followPath.path;
				
				for(int i = 0; i < path.points.size(); i++){
					Vector2D p1 = path.points.get(i);
					Vector2D p2 = null;
					if(i+1 < path.points.size()){
						p2 = path.points.get(i+1);
					}else{
						if(path.loop){
							p2 = path.points.get(0);
						}
					}
					
					if(p2 != null){
						gridRender.setColor(Color.BLACK);
						gridRender.begin(ShapeType.Line);
						gridRender.line(p1.x, p1.y, p2.x, p2.y);
						gridRender.end();
					}
					
					gridRender.setColor(Color.RED);
					gridRender.begin(ShapeType.Circle);
					gridRender.circle(p1.x, p1.y, vehicle.steering.followPath.waypointDistance);
					gridRender.end();
				}
			}
			
			
			if(vehicle.steering.wallAvoidance.enabled && vehicle.steering.getFeelers() != null){
				for(Vector2D f : vehicle.steering.getFeelers()){
					gridRender.setColor(Color.RED);
					gridRender.begin(ShapeType.Line);
					gridRender.line(vehicle.pos.x, vehicle.pos.y,f.x, f.y);
					
					gridRender.end();
				}
			}
			
			if (vehicle.steering.obstacleAvoidance.enabled) {
				// the detection box length is proportional to the agent's
				// velocity
				float box = vehicle.steering.obstacleAvoidance.obstacleSearchBoxDistance
						+ (vehicle.getVel().length() / vehicle.maxSpeed)
						* vehicle.steering.obstacleAvoidance.obstacleSearchBoxDistance;

				Vector2D p1 = new Vector2D(0, -vehicle.radius);
				Vector2D p2 = new Vector2D(box, -vehicle.radius);
				Vector2D p3 = new Vector2D(box, vehicle.radius);
				Vector2D p4 = new Vector2D(0, vehicle.radius);
				ArrayList<Vector2D> points = new ArrayList<Vector2D>();
				points.add(p1);
				points.add(p2);
				points.add(p3);
				points.add(p4);
				Transformations.WorldTransform(points, vehicle.pos,
						vehicle.velHead, vehicle.velSide);

				gridRender.setColor(Color.RED);
				gridRender.begin(ShapeType.Line);
				gridRender.line(p1.x, p1.y, p2.x, p2.y);
				gridRender.line(p2.x, p2.y, p3.x, p3.y);
				gridRender.line(p3.x, p3.y, p4.x, p4.y);
				gridRender.line(p4.x, p4.y, p1.x, p1.y);
				gridRender.end();
			}

			if (vehicle.steering.flee.enabled) {
				Vector2D rst = new Vector2D(vehicle.pos);

				gridRender.setColor(Color.RED);
				gridRender.begin(ShapeType.FilledCircle);
				gridRender.filledCircle(rst.x, rst.y, entitySize);
				gridRender.end();

				if (vehicle.steering.flee.useFleePanic) {
					gridRender.setColor(Color.RED);
					gridRender.begin(ShapeType.FilledCircle);
					gridRender.filledCircle(rst.x, rst.y,
							vehicle.steering.flee.fleePanicDistance);
					gridRender.end();
				}
			}

			if (vehicle.steering.seperation.enabled|| vehicle.steering.cohesion.enabled|| vehicle.steering.alignment.enabled) {
				
				for (Vehicle v : vehicle.steering.getVisibleVehicles()) {
					gridRender.setColor(Color.RED);
					gridRender.begin(ShapeType.Circle);
					gridRender.circle(v.pos.x, v.pos.y, 10);
					gridRender.end();
				}

				gridRender.setColor(Color.RED);
				gridRender.begin(ShapeType.Rectangle);
				gridRender
						.rect(vehicle.steering.neighborDetectionRegion.x1, vehicle.steering.neighborDetectionRegion.y1, 
								vehicle.steering.neighborDetectionRegion.getWidth(), vehicle.steering.neighborDetectionRegion.getHeight());
				gridRender.end();
			}

			if (vehicle.steering.wander.enabled) {
				Vector2D rst = new Vector2D(vehicle.pos);

				// Draw Render Circle
				Vector2D circle = new Vector2D(vehicle.vel);
				circle.normalise();
				circle.scale(vehicle.steering.wander.wanderDistance);
				circle.add(vehicle.pos);

				gridRender.setColor(Color.BLUE);
				gridRender.begin(ShapeType.Circle);
				gridRender.circle(circle.x, circle.y,
						vehicle.steering.wander.wanderRadius);
				gridRender.end();

				gridRender.setColor(Color.GREEN);
				gridRender.begin(ShapeType.Line);
				gridRender.line(circle.x, circle.y, circle.x
						+ vehicle.steering.wander.wanderVector.x, circle.y
						+ vehicle.steering.wander.wanderVector.y);
				gridRender.end();

				gridRender.setColor(Color.RED);
				gridRender.begin(ShapeType.Circle);
				gridRender.circle(circle.x + vehicle.steering.wander.wanderVector.x,
						circle.y + vehicle.steering.wander.wanderVector.y,
						vehicle.steering.wander.wanderJitter);
				gridRender.end();
			}
		}
		gridRender.setColor(Color.BLUE);
		gridRender.begin(ShapeType.Triangle);
		gridRender.triangle(vehicle.transformedVehicleShape.get(0).x,
				vehicle.transformedVehicleShape.get(0).y,
				vehicle.transformedVehicleShape.get(1).x,
				vehicle.transformedVehicleShape.get(1).y,
				vehicle.transformedVehicleShape.get(2).x,
				vehicle.transformedVehicleShape.get(2).y);
		gridRender.end();
		// spriteBatch.begin();
		// spriteBatch.draw(spriteRegion, entity.pos.x-sizeX/2,
		// entity.pos.y-sizeY/2, sizeX / 2,
		// sizeY / 2, sizeX, sizeY, 1, 1,
		// (float) (Math.toDegrees(entity.angle)));
		// spriteBatch.end();

	}

	private void renderEntitiesInCell(GLCommon gl, Camera camera,
			Rectangle2D drawRegion, QuadTreeNode node) {
		float sizeX = spriteTexture.getWidth() / spriteScale;
		float sizeY = spriteTexture.getHeight() / spriteScale;
		if (drawRegion.intersects(node.region)) {
			if (node.isLeaf) {
				if (drawEntities) {

					for (Object e : node.points) {
						drawEntity(gl, camera, drawRegion, (BaseGameEntity) e);
					}
				}
			} else {
				if (node.NW != null)
					renderEntitiesInCell(gl, camera, drawRegion, node.NW);
				if (node.NE != null)
					renderEntitiesInCell(gl, camera, drawRegion, node.NE);
				if (node.SW != null)
					renderEntitiesInCell(gl, camera, drawRegion, node.SW);
				if (node.SE != null)
					renderEntitiesInCell(gl, camera, drawRegion, node.SE);
			}

		}
	}

	public void render(GLCommon gl, Camera camera, Rectangle2D drawRegion) {

		gridRender.setProjectionMatrix(camera.combined);
		spriteBatch.setProjectionMatrix(camera.combined);
		// Draw Entities
		synchronized (world.quadTree) {
			if (drawQuadTree) {
				renderGridInCell(gl, camera, drawRegion,
						world.quadTree.getRootNode());
			}

			if (drawEntities || drawBorders) {
				renderEntitiesInCell(gl, camera, drawRegion,
						world.quadTree.getRootNode());
			}

			if (drawObstacles) {
				renderObstacles(gl, camera, drawRegion, world.getObstacles());
			}
			
			if(drawWalls){
				renderWalls(gl, camera, drawRegion, world.getWalls(), wallNormSize);
			}
		}

	}

	private void renderWalls(GLCommon gl, Camera camera,
			Rectangle2D drawRegion, ArrayList<Wall2D> wall, float normalSize) {
		for (Wall2D w : wall) {
			gridRender.setColor(Color.BLACK);
			gridRender.begin(ShapeType.Line);
			gridRender.line(w.p1.x, w.p1.y, w.p2.x, w.p2.y);
			gridRender.end();
			
			//Draw Normal
			float x = (w.p1.x+w.p2.x)/2;
			float y = (w.p1.y+w.p2.y)/2;
			gridRender.setColor(Color.RED);
			gridRender.begin(ShapeType.Line);
			gridRender.line(x,y, x+normalSize*w.vN.x, y+normalSize*w.vN.y);
			gridRender.end();
			
			
		}
		
	}
	
	private void renderObstacles(GLCommon gl, Camera camera,
			Rectangle2D drawRegion, ArrayList<Obstacle> obstacles) {
		for (Obstacle o : obstacles) {
			gridRender.setColor(Color.BLACK);
			gridRender.begin(ShapeType.Circle);
			gridRender.circle(o.pos.x, o.pos.y, o.radius);
			gridRender.end();
			
			gridRender.setColor(Color.BLACK);
			gridRender.begin(ShapeType.FilledCircle);
			gridRender.filledCircle(o.pos.x, o.pos.y, 2);
			gridRender.end();
		}
	}

}
