package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.joey.chain.ReactorApp;

public abstract class StageScreen extends GameScreen {

	Stage stage;
	OrthographicCamera stageCamera;
	
	public StageScreen(ReactorApp game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	public abstract void createStage(Stage stage);
	
	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		// TODO Auto-generated method stub
		super.addInputMultiplexer(input);
		input.addProcessor(stage);
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		// TODO Auto-generated method stub
		super.removeInputMultiplexer(input);
		input.removeProcessor(stage);
		
	}
	public void show() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), false);
		super.show();
		createStage(stage);
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
		stageCamera = new OrthographicCamera(width, height);
		stageCamera.translate(width / 2, height / 2, 0);
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		stage.dispose();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		drawStage(delta);
	}
	
	public void drawStage(float delta) {
		stage.setCamera(stageCamera);
		stage.act(delta);
		stage.draw();
	}


}
