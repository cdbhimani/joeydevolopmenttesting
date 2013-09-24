package com.emptyPockets.test.multitouch;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptyPockets.graphics.GraphicsToolkit;
import com.emptyPockets.gui.StageScreen;
import com.emptyPockets.logging.ConsoleScreen;
import com.emptyPockets.utils.OrthoCamController;

public class MultitouchScreen extends StageScreen {

	ConsoleScreen console;
	OrthoCamController control;
	ShapeRenderer shape ;
	public MultitouchScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		control = new OrthoCamController(getScreenCamera());
		setClearColor(Color.WHITE);
	}

	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(control);
	}
	
	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		input.addProcessor(control);
	}
	
	@Override
	public void show() {
		super.show();
		shape = new ShapeRenderer();
	}
	
	@Override
	public void hide() {
		super.hide();
		shape.dispose();
	}
	
	@Override
	public void createStage(Stage stage) {
//		console = new ConsoleScreen("Testing", getSkin());
//		console.setHeight(Gdx.graphics.getHeight()/2);
//		console.setWidth(Gdx.graphics.getWidth());
//		stage.addActor(console);
	}

	@Override
	public void initializeRender() {
		super.initializeRender();
		shape.setProjectionMatrix(getScreenCamera().combined);
	}
	
	@Override
	public void drawBackground(float delta) {
		GraphicsToolkit.draw2DAxis(shape, getScreenCamera(), 10, Color.BLACK);
	}

	@Override
	public void drawScreen(float delta) {
		
	}

	@Override
	public void drawOverlay(float delta) {
		
	}

}
