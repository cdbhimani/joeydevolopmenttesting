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
		stage.setViewport(width, height, false);
		stageCamera.viewportWidth = width;
		stageCamera.viewportHeight= height;
		stageCamera.position.x =width/2;
		stageCamera.position.y =height/2;
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
		try{
			stage.draw();
		}catch(Exception e){
			
		}
	}


}
