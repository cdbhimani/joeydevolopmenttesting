package com.emptyPockets.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
		input.addProcessor(getStage());
		super.addInputMultiplexer(input);
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		input.removeProcessor(getStage());
		super.removeInputMultiplexer(input);
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
		eventLogger.begin("RENDER-stage");
		drawStage(delta);
		eventLogger.end("RENDER-stage");
	}
	
	@Override
	public void updateLogic(float delta) {
		super.updateLogic(delta);
		eventLogger.begin("LOGIC-Stage");
		getStage().act(delta);
		eventLogger.end("LOGIC-Stage");
	}
	
	public void drawStage(float delta) {
		getStage().setCamera(stageCamera);
		try{
			getStage().draw();
			Table.drawDebug(getStage());
		}catch(Exception e){
			
		}
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public OrthographicCamera getStageCamera() {
		return stageCamera;
	}

	public void setStageCamera(OrthographicCamera stageCamera) {
		this.stageCamera = stageCamera;
	}

}
