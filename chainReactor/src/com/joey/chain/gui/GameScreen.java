package com.joey.chain.gui;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.joey.chain.ReactorApp;

public abstract class GameScreen implements Screen, GestureListener, InputProcessor{

	OrthographicCamera stageCamera = new OrthographicCamera();
	ReactorApp game;
	GestureDetector gesture;
	Color clearColor = new Color(1,1,1,1);
	Stage stage;
	Skin skin;
	
	public GameScreen(ReactorApp game){
		this.game= game;
		this.gesture = new GestureDetector(this);
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), false);
	}
	
	public void initializeRender(){
		stageCamera.update();
		if(Gdx.gl10 !=null)stageCamera.apply(Gdx.gl10);
		
		Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14
	}
	
	public void addInputMultiplexer(InputMultiplexer input){
		input.addProcessor(this);
		input.addProcessor(gesture);
		input.addProcessor(stage);
	}
	

	public Skin getSkin(){
		if( skin == null ) {
            skin = new Skin( Gdx.files.internal( "ui/uiskin.json" ), Gdx.files.internal( "ui/uiskin.png" ) );
        }
        return skin;
	}
	
	public void removeInputMultiplexer(InputMultiplexer input){
//		input.removeProcessor(this);
//		input.removeProcessor(gesture);
//		input.removeProcessor(stage);
	}
	
	public void createStage(Stage stage){
		
	}
	public GestureDetector getGesture() {
		return gesture;
	}

	public void setGesture(GestureDetector gesture) {
		this.gesture = gesture;
	}

	@Override
	public void render(float delta) {
		initializeRender();
	}
	
	public void drawStage(float delta){
		stage.act(delta);
		stage.setCamera(stageCamera);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stageCamera = new OrthographicCamera(width, height);
		stageCamera.translate(width / 2, height / 2, 0);
		stage.setViewport(width, height, false);
	}


	@Override
	public void show() {
		addInputMultiplexer(game.getInputProcessor());
		createStage(stage);
	}

	@Override
	public void hide() {
		removeInputMultiplexer(game.getInputProcessor());
		stage.clear();
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
