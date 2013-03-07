package com.emptyPockets.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class StageScreen extends GameScreen {

	private Stage stage;
	OrthographicCamera stageCamera;
	public StageScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		stageCamera = new OrthographicCamera();
	}

	public abstract void createStage(Stage stage);
	
	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		input.addProcessor(getStage());
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(getStage());
		
	}
	public void show() {
		setStage(new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), false));
		super.show();
		createStage(getStage());
	}
	
	@Override
	public void hide() {
		super.hide();
		getStage().dispose();
		setStage(null);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		super.resize(width, height);
		getStage().setViewport(width, height, false);
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
		getStage().act(delta);
		eventLogger.end("Stage Logic");
	}
	
	public void drawStage(float delta) {
		getStage().setCamera(stageCamera);
		try{
			getStage().draw();
		}catch(Exception e){
			
		}
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
