package com.emptyPockets.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.emptyPockets.utils.event.EventRecorder;

public abstract class GameScreen implements Screen, GestureListener, InputProcessor{

	protected OrthographicCamera screenCamera = new OrthographicCamera();
	GestureDetector gesture;
	protected Color clearColor = new Color(1,1,1,1);
	protected Skin skin;
	protected EventRecorder eventLogger;
	InputMultiplexer parentInputMultiplexer;
	
	public GameScreen(InputMultiplexer inputProcessor){
		this.parentInputMultiplexer = inputProcessor;
		this.gesture = new GestureDetector(this);
		eventLogger = new EventRecorder(50);
	}
	
	public void initializeRender(){
		screenCamera.update();
		if(Gdx.gl10 !=null)screenCamera.apply(Gdx.gl10);
		
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14
	}
	
	protected void addInputMultiplexer(InputMultiplexer input){
		input.addProcessor(this);
		input.addProcessor(gesture);
	}
	

	public Skin getSkin(){
		return Scene2DToolkit.getToolkit().getSkin();
	}
	
	public void removeInputMultiplexer(InputMultiplexer input){
		input.removeProcessor(this);
		input.removeProcessor(gesture);
	}

	public GestureDetector getGesture() {
		return gesture;
	}

	public void setGesture(GestureDetector gesture) {
		this.gesture = gesture;
	}


	@Override
	public final void render(float delta) {
		eventLogger.begin("Logic");
		updateLogic(delta);
		eventLogger.end("Logic");
		
		eventLogger.begin("Render");
		initializeRender();
		renderScreen(delta);
		eventLogger.end("Render");
	}
	
	public void renderScreen(float delta){
		eventLogger.begin("Background");
		drawBackground(delta);
		eventLogger.end("Background");
		
		eventLogger.begin("Screen");
		drawScreen(delta);
		eventLogger.end("Screen");
		
		eventLogger.begin("Overlay");
		drawOverlay(delta);
		eventLogger.end("Overlay");
	}
	
	public void updateLogic(float delta){
	}
	
	public abstract void drawBackground(float delta);
	public abstract void drawScreen(float delta);
	public abstract void drawOverlay(float delta);

	@Override
	public void resize(int width, int height) {
		screenCamera.viewportWidth = width;
		screenCamera.viewportHeight= height;
	}


	@Override
	public void show() {
		addInputMultiplexer(parentInputMultiplexer);
	}

	@Override
	public void hide() {
		removeInputMultiplexer(parentInputMultiplexer);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
	}

	public Color getClearColor() {
		return clearColor;
	}

	public void setClearColor(Color clearColor) {
		this.clearColor = clearColor;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	public OrthographicCamera getScreenCamera() {
		return screenCamera;
	}

}
