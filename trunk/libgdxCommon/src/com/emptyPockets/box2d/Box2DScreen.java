package com.emptyPockets.box2d;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptyPockets.gui.StageScreen;

public abstract class Box2DScreen extends StageScreen{
	private World world;
	Vector2 gravity;
	int velocityIterations = 2;
	int positionIterations = 2;
	boolean showDebug = false;
	Box2DDebugRenderer debugRender;
	OrthographicCamera worldCamera;
	float worldScale = 0.1f;
	ShapeRenderer backShape;
	
	ArrayList<Body> bodiesToRemove; 
	public Box2DScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		gravity = new Vector2();
		worldCamera = new OrthographicCamera();
		bodiesToRemove = new ArrayList<Body>();
	}

	public void show() {
		super.show();
		createWorld();
		backShape = new ShapeRenderer();
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
		backShape.dispose();
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
		worldCamera.viewportWidth = width;
		worldCamera.viewportHeight= height;
		worldCamera.position.x =width/2;
		worldCamera.position.y =height/2;
	}
	
	public void updateWorld(float delta){
		getWorld().step(delta, velocityIterations, positionIterations);
		synchronized (bodiesToRemove) {
			for(Body b : bodiesToRemove) {
				getWorld().destroyBody(b);
			}
			bodiesToRemove.clear();
		}
		updateWorldCamera(worldCamera);
		worldCamera.update();
	}
	
	public void drawDebugWorld(float delta){
		if(showDebug){
			if(debugRender == null){
				debugRender = new Box2DDebugRenderer();
			}
			debugRender.render(getWorld(), worldCamera.combined);
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
			
			float viewWide = worldCamera.viewportHeight;
			float viewHigh = worldCamera.viewportWidth;
			
			backShape.setProjectionMatrix(worldCamera.combined);
			
			backShape.begin(ShapeType.Rectangle);
			int numX = 10;
			int numY = 10;
			float xP,yP,wide,high;
			for(int x = 0; x < numX; x++){
				for(int y = 0; y < numY; y++){
					xP = 0;
					yP = 0;
					wide = 0;
					high = 0;
					backShape.rect(xP, yP, wide, high);
				}
			}
			
			backShape.end();
		}

	}
	
	@Override
	public void drawOverlay(float delta) {
		drawDebugWorld(delta);
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


}
