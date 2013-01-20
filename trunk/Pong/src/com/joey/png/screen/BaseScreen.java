package com.joey.png.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.joey.png.PongGame;

public abstract class BaseScreen implements Screen, InputProcessor {

	Stage stage;
	PongGame game;
	
	public BaseScreen(PongGame game){
		this.game = game;
		
		int width = 2;
		int height = 2;
		
		if(Gdx.graphics!= null){
			width = Gdx.graphics.getWidth();
			height = Gdx.graphics.getHeight();
		}
		stage = new Stage(width,height, true);
		

	}
	
	
	
	@Override
	public void render(float delta) {
		render(delta, Gdx.gl10);
		
		stage.draw();
	}
	
	public abstract void render(float delta, GL10 g);
	

	@Override
	public void resize(int width, int height) {
		//stage.setViewport(width, height, false);
		
	}

	@Override
	public void show() {
		game.getInputMultiplexer().addProcessor(this);
	}

	@Override
	public void hide() {
		game.getInputMultiplexer().removeProcessor(this);
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
	public boolean touchDown(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
