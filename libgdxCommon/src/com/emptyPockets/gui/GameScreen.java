package com.emptyPockets.gui;

import com.badlogic.gdx.Game;
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

public abstract class GameScreen implements Screen, GestureListener, InputProcessor{

	protected OrthographicCamera cam = new OrthographicCamera();
	GestureDetector gesture;
	protected Color clearColor = new Color(1,1,1,1);
	protected Skin skin;
	protected long updateTime = 0;
	protected long drawTime = 0;
	InputMultiplexer parentInputMultiplexer;
	
	public GameScreen(InputMultiplexer inputProcessor){
		this.parentInputMultiplexer = inputProcessor;
		this.gesture = new GestureDetector(this);
	}
	
	public void initializeRender(){
		cam.update();
		if(Gdx.gl10 !=null)cam.apply(Gdx.gl10);
		
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14
	}
	
	protected void addInputMultiplexer(InputMultiplexer input){
		input.addProcessor(this);
		input.addProcessor(gesture);
	}
	

	public Skin getSkin(){
		if( skin == null ) {
            skin = new Skin( Gdx.files.internal( "ui/uiskin.json" ), Gdx.files.internal( "ui/uiskin.png" ) );
        }
        return skin;
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
	public void render(float delta) {
		long start;
		start = System.currentTimeMillis();
		updateLogic(delta);
		updateTime= System.currentTimeMillis()-start;
		
		start = System.currentTimeMillis();
		initializeRender();
		drawScreen(delta);
		drawTime = System.currentTimeMillis()-start;
		drawOverlay(delta);
	}
	
	public abstract void drawScreen(float delta);
	
	public abstract void drawOverlay(float delta);
		
	public abstract void updateLogic(float delta);

	@Override
	public void resize(int width, int height) {
		cam = new OrthographicCamera(width, height);
		cam.translate(width / 2, height / 2, 0);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean longPress(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(int x, int y, int deltaX, int deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean touchDown(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean tap(int x, int y, int count) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		 
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public Color getClearColor() {
		return clearColor;
	}

	public void setClearColor(Color clearColor) {
		this.clearColor = clearColor;
	}

}
