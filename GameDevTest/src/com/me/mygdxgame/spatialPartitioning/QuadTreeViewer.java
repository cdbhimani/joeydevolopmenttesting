package com.me.mygdxgame.spatialPartitioning;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class QuadTreeViewer {
	public QuadTree tree;

	ShapeRenderer gridRender;
	ShapeRenderer entityRender;

	long lastUpdate = System.currentTimeMillis();
	float border = 500;
	float entitySize = 10;

	public boolean drawBorders = false;
	public boolean drawEntities = true;
	public boolean drawQuadTree = false;
	
	public QuadTreeViewer(Rectangle2D region) {
		tree = new QuadTree(region, 2);
		gridRender = new ShapeRenderer();
		entityRender = new ShapeRenderer();
	}

	public void addPoints(int number) {
		synchronized (tree) {
			for (float i = 0; i < number; i++) {
				float maxVel = 100;
				Entity2D p = new Entity2D();
				p.x = (float) (tree.root.region.x1 + Math.random()
						* (tree.root.region.x2 - tree.root.region.x1));
				p.y = (float) (tree.root.region.y1 + Math.random()
						* (tree.root.region.y2 - tree.root.region.y1));
				p.vx = (float) (maxVel - Math.random() * 2 * maxVel);
				p.vy = (float) (maxVel - Math.random() * 2 * maxVel);
				p.rad = entitySize;
				tree.addPoint(p);
			}
		}
	}

	public void removePoints(int number) {
		synchronized (tree) {
			for(int i = 0; i < number; i++){
				if(tree.points.size() > 0){
					tree.points.remove((int)(Math.random()*tree.points.size()));
				}
			}
		}
	}
	public void updatePoints() {
		float diff = (System.currentTimeMillis() - lastUpdate) / 1000f;
		lastUpdate = System.currentTimeMillis();
		for (int i = 0; i < tree.points.size(); i++) {
			// Update Positions
			Entity2D p = tree.points.get(i);
			p.x += p.vx * diff;
			p.y += p.vy * diff;

			// Do Calculation
//			ArrayList<Entity2D> rst = tree.getPointsInRegion(new Rectangle2D(p.x
//					- border, p.y - border, p.x + border, p.y + border));
//			float vx = 0;
//			float vy = 0;
//			int count = 0;
//			for (Entity2D data : rst) {
//				EntityCollider.collide(p, data);
//			}

			// Validate position
			if (p.x < tree.root.region.x1)
				p.x = tree.root.region.x2;
			if (p.x > tree.root.region.x2)
				p.x = tree.root.region.x1;
			if (p.y < tree.root.region.y1)
				p.y = tree.root.region.y2;
			if (p.y > tree.root.region.y2)
				p.y = tree.root.region.y1;
			
			
		}
		
		for(Entity2D e : tree.points){
			e.flip();
		}
	}

	public void rebuildTree() {
		tree.rebuild();
	}

	private void renderCell(GLCommon gl, Camera camera, Rectangle2D drawRegion,
			QuadTreeNode node) {
		if (drawRegion.intersects(node.region)) {

			if (node.isLeaf) {

				if(drawQuadTree){
					gridRender.setColor(Color.RED);
					gridRender.begin(ShapeType.Rectangle);
					gridRender.rect(node.region.x1, node.region.y1,
							node.region.x2 - node.region.x1,
							node.region.y2 - node.region.y1);
					gridRender.end();
				}
				
				if(drawEntities||drawBorders){
					entityRender.setColor(Color.BLUE);
					for (Entity2D p : node.points) {
						if(drawEntities){
							entityRender.begin(ShapeType.FilledCircle);
							entityRender.filledCircle(p.x, p.y, entitySize);
							entityRender.end();
						}
						
						if(drawBorders){
							entityRender.begin(ShapeType.Circle);
							entityRender.circle(p.x, p.y, border);
							entityRender.end();
						}
					}
				}
			}else{
				if(node.NW != null)renderCell(gl, camera, drawRegion, node.NW);
				if(node.NE != null)renderCell(gl, camera, drawRegion, node.NE);
				if(node.SW != null)renderCell(gl, camera, drawRegion, node.SW);
				if(node.SE != null)renderCell(gl, camera, drawRegion, node.SE);
			}

		}
	}

	public void render(GLCommon gl, Camera camera, Rectangle2D drawRegion) {
		
		gridRender.setProjectionMatrix(camera.combined);
		entityRender.setProjectionMatrix(camera.combined);

		synchronized (tree) {
			renderCell(gl, camera, drawRegion, tree.root);
		}

	}

}
