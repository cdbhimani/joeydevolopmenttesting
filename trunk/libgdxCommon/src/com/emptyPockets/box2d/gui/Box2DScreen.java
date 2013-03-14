package com.emptyPockets.box2d.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.emptyPockets.box2d.body.BodyData;
import com.emptyPockets.graphics.GraphicsToolkit;
import com.emptyPockets.gui.StageScreen;

public abstract class Box2DScreen extends StageScreen{
	private World world;
	Vector2 gravity;
	int velocityIterations = 2;
	int positionIterations = 2;
	boolean showDebug = false;
	SpriteBatch textBatch;
	BitmapFont font;
	Box2DDebugRenderer debugRender;
	OrthographicCamera box2DWorldCamera;
	float worldScale = 0.1f;
	ShapeRenderer background;
	
	ArrayList<Body> bodiesToRemove; 
	public Box2DScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		gravity = new Vector2();
		box2DWorldCamera = new OrthographicCamera();
		bodiesToRemove = new ArrayList<Body>();
	}

	public void show() {
		super.show();
		createWorld();
		background = new ShapeRenderer();
		textBatch = new SpriteBatch();
		font = new BitmapFont();
	}
	
	private void createWorld(){
		setWorld(new World(gravity, true));
		createWorld(world);
	}
	
	public void removeBody(Body body){
		synchronized (bodiesToRemove) {
			if(!bodiesToRemove.contains(body)){
				bodiesToRemove.add(body);
			}
		}
	}
	@Override
	public void hide() {
		super.hide();
		world.dispose();
		world = null;
		background.dispose();
	}
	
	@Override
	public void updateLogic(float delta){
		super.updateLogic(delta);
		eventLogger.begin("World Logic");
		updateWorld(delta);
		eventLogger.end("World Logic");
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
		box2DWorldCamera.viewportWidth = width;
		box2DWorldCamera.viewportHeight= height;
	}
	
	public void updateWorld(float delta){
		getWorld().step(delta, velocityIterations, positionIterations);
		synchronized (bodiesToRemove) {
			for(Body b : bodiesToRemove) {
				if(b.getUserData() instanceof BodyData){
					((BodyData)b.getUserData()).dispose();
				}else{
					getWorld().destroyBody(b);
				}
			}
			bodiesToRemove.clear();
		}
		updateWorldCamera(box2DWorldCamera);
		box2DWorldCamera.update();
	}
	
	public void drawDebugWorld(float delta){
		if(showDebug){
			if(debugRender == null){
				debugRender = new Box2DDebugRenderer();
			}
			debugRender.render(getWorld(), box2DWorldCamera.combined);
		}
	}
	
	public void updateWorldCamera(OrthographicCamera worldCamera){
		worldCamera.viewportWidth=Gdx.graphics.getWidth()*worldScale;
		worldCamera.viewportHeight=Gdx.graphics.getHeight()*worldScale;
	}
	
	public abstract void createWorld(World world);

	@Override
	public void drawBackground(float delta) {
		if(isShowDebug()){
			box2DWorldCamera.update();
			background.setProjectionMatrix(box2DWorldCamera.combined);
			GraphicsToolkit.draw2DAxis(background, box2DWorldCamera, 1, Color.WHITE);	
		}
	}
	
	@Override
	public void drawOverlay(float delta) {
		textBatch.setProjectionMatrix(screenCamera.combined);
		drawDebugWorld(delta);
//		eventLogger.draw(textBatch, font, 10,10,15);
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public boolean isShowDebug() {
		return showDebug;
	}

	public void setShowDebug(boolean showDebug) {
		this.showDebug = showDebug;
	}

	public float getWorldScale() {
		return worldScale;
	}

	public void setWorldScale(float worldScale) {
		this.worldScale = worldScale;
	}
	
	public OrthographicCamera getBox2DWorldCamera() {
		return box2DWorldCamera;
	}


}
