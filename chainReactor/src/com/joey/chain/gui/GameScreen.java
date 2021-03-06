package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.joey.chain.ReactorApp;

public abstract class GameScreen implements Screen, GestureListener, InputProcessor{

	protected OrthographicCamera cam = new OrthographicCamera();
	private ReactorApp game;
	GestureDetector gesture;
	Color clearColor = new Color(1,1,1,1);
	Skin skin;
	long updateTime = 0;
	long drawTime = 0;
	
	public GameScreen(ReactorApp game){
		this.setGame(game);
		this.gesture = new GestureDetector(this);
	}
	
	public void initializeRender(){
		cam.update();
		if(Gdx.gl10 !=null)cam.apply(Gdx.gl10);
		
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14
	}
	
	public void addInputMultiplexer(InputMultiplexer input){
		input.addProcessor(this);
		input.addProcessor(gesture);
	}
	

	public Skin getSkin(){
		if( skin == null ) {
			FileHandle jsonFile = Gdx.files.internal( "ui/uiskin.json" );
			FileHandle atlasFile = Gdx.files.internal( "ui/uiskin.atlas" );
			TextureAtlas atlas = new TextureAtlas(atlasFile);
            skin = new Skin(jsonFile, atlas);
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
		addInputMultiplexer(getGame().getInputProcessor());
	}

	@Override
	public void hide() {
		removeInputMultiplexer(getGame().getInputProcessor());
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

	public ReactorApp getGame() {
		return game;
	}

	public void setGame(ReactorApp game) {
		this.game = game;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

}
