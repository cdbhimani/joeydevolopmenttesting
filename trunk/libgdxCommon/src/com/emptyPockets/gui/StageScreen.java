package com.emptyPockets.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class StageScreen extends GameScreen {

	Stage stage;
	OrthographicCamera stageCamera;
	
	public StageScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		stageCamera = new OrthographicCamera();
	}

	public abstract void createStage(Stage stage);
	
	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		input.addProcessor(stage);
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(stage);
		
	}
	public void show() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), false);
		super.show();
		createStage(stage);
	}
	
	@Override
	public void hide() {
		super.hide();
		stage.dispose();
		stage = null;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
		stage.setViewport(width, height, false);
		stageCamera.viewportWidth = width;
		stageCamera.viewportHeight= height;
		stageCamera.position.x =width/2;
		stageCamera.position.y =height/2;
	}
	
	@Override
	public void renderScreen(float delta) {
		super.renderScreen(delta);
		eventLogger.begin("Stage Draw");
		drawStage(delta);
		eventLogger.end("Stage Draw");
	}
	
	@Override
	public void updateLogic(float delta) {
		super.updateLogic(delta);
		eventLogger.begin("Stage Logic");
		stage.act(delta);
		eventLogger.end("Stage Logic");
	}
	
	public void drawStage(float delta) {
		stage.setCamera(stageCamera);
		try{
			stage.draw();
		}catch(Exception e){
			
		}
	}


}
