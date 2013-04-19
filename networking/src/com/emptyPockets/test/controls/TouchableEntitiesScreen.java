package com.emptyPockets.test.controls;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.emptyPockets.graphics.GraphicsToolkit;
import com.emptyPockets.gui.GameScreen;
import com.emptyPockets.gui.OrthographicsCameraConvertor;
import com.emptyPockets.test.controls.inputs.EntityInputManager;
import com.emptyPockets.test.controls.render.EntityDebugRender;
import com.emptyPockets.test.controls.render.EntityRender;
import com.emptyPockets.utils.OrthoCamController;

public class TouchableEntitiesScreen extends GameScreen{
	EntityStore store;
	EntityRender render;
	EntityInputManager inputManager;
	
	ShapeRenderer backgroundRender;
	OrthoCamController backgroundControl;
	OrthographicsCameraConvertor camConvert;
	
	public TouchableEntitiesScreen(InputMultiplexer inputProcessor) {
		super(inputProcessor);
		setClearColor(Color.WHITE);
		
		backgroundRender = new ShapeRenderer();
		backgroundControl = new OrthoCamController(getScreenCamera());
		camConvert = new OrthographicsCameraConvertor(getScreenCamera());
		inputManager = new EntityInputManager(camConvert);
		EntityInputManager.setup(camConvert);
		store = new EntityStore();
		render = new EntityDebugRender();
		
		float sizeX = 0;
		float sizeY = 0;
		
		float wide = 200;
		float high = 200;
		store.randomData(1, new Rectangle(-sizeX/2,-sizeY/2, sizeX,sizeY), wide, high, 0.2f);
	}
	
	@Override
	protected void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		EntityInputManager.getInst().attach(input);
		input.addProcessor(backgroundControl);
	}

	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		EntityInputManager.getInst().detach(input);
		input.removeProcessor(backgroundControl);
		
	}
	@Override
	public void drawBackground(float delta) {
		backgroundRender.setProjectionMatrix(getScreenCamera().combined);
		GraphicsToolkit.draw2DAxis(backgroundRender, getScreenCamera(), 100, Color.BLACK);
	}
	
	@Override
	public void drawScreen(float delta) {
		store.render(render, getScreenCamera());
	}
	@Override
	public void drawOverlay(float delta) {
		// TODO Auto-generated method stub
		
	}
	
	
}
