package com.joey.aitesting.game.graphics;

import java.util.ArrayList;

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
import com.joey.aitesting.game.cellSpace.QuadTree;
import com.joey.aitesting.game.cellSpace.QuadTreeNode;
import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.joey.aitesting.game.shapes.Vector2D;
import com.joey.aitesting.game.steeringBehaviors.SteeringBehaviors;

public class QuadTreeViewer<T extends BaseGameEntity> {
	public QuadTree<T> tree;

	ShapeRenderer gridRender;
	SpriteBatch spriteBatch;
	
	private Texture spriteTexture;
	private TextureRegion spriteRegion;
	float spriteScale = 6;
	
	long lastUpdate = System.currentTimeMillis();
	float entitySize = 40;

	public boolean drawBorders = true;
	public boolean drawEntities = true;
	public boolean drawQuadTree = true;
	
	
	
	public QuadTreeViewer(QuadTree<T> tree) {
		this.tree = tree;
		gridRender = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		loadTextures();
	}
	
	public void loadTextures() {
		spriteTexture = new Texture(Gdx.files.internal("fish1.png"));
		spriteRegion = new TextureRegion(spriteTexture);
	}

	public void rebuildTree() {
		tree.rebuild();
	}

	private void renderGridInCell(GLCommon gl, Camera camera, Rectangle2D drawRegion,QuadTreeNode<T> node) {
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
			}else{
				if(node.NW != null)renderGridInCell(gl, camera, drawRegion, node.NW);
				if(node.NE != null)renderGridInCell(gl, camera, drawRegion, node.NE);
				if(node.SW != null)renderGridInCell(gl, camera, drawRegion, node.SW);
				if(node.SE != null)renderGridInCell(gl, camera, drawRegion, node.SE);
			}

		}

	}
	private void renderEntitiesInCell(GLCommon gl, Camera camera, Rectangle2D drawRegion,
			QuadTreeNode<T> node) {
		float sizeX = spriteTexture.getWidth() / spriteScale;
		float sizeY = spriteTexture.getHeight() / spriteScale;
		if (drawRegion.intersects(node.region)) {
			if (node.isLeaf) {
				if(drawEntities){
					
					for (BaseGameEntity e : node.points) {
						Vehicle entity = (Vehicle)e;
//						if(!entity.steering.useFlee){
//							Vector2D rst = new Vector2D(entity.pos);
//							
//							gridRender.setColor(Color.RED);
//							gridRender.begin(ShapeType.FilledCircle);
//							gridRender.filledCircle(rst.x, rst.y, entitySize);
//							gridRender.end();
//						}
						
						spriteBatch.begin();
						spriteBatch.draw(spriteRegion, entity.pos.x-sizeX/2, entity.pos.y-sizeY/2, sizeX / 2,
								sizeY / 2, sizeX, sizeY, 1, 1,
								(float) (Math.toDegrees(entity.angle)));
						spriteBatch.end();
					}
				}
			}else{
				if(node.NW != null)renderEntitiesInCell(gl, camera, drawRegion, node.NW);
				if(node.NE != null)renderEntitiesInCell(gl, camera, drawRegion, node.NE);
				if(node.SW != null)renderEntitiesInCell(gl, camera, drawRegion, node.SW);
				if(node.SE != null)renderEntitiesInCell(gl, camera, drawRegion, node.SE);
			}

		}
	}

	public void render(GLCommon gl, Camera camera, Rectangle2D drawRegion) {
		
		gridRender.setProjectionMatrix(camera.combined);
		spriteBatch.setProjectionMatrix(camera.combined);
		synchronized (tree) {			
			if(drawQuadTree){
				renderGridInCell(gl, camera, drawRegion, tree.getRootNode());
			}
			
			if(drawEntities||drawBorders){
				renderEntitiesInCell(gl, camera, drawRegion, tree.getRootNode());
			}
		}

	}

}
