package com.emptyPockets.box2d.gui.bodyEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.box2d.gui.Box2DScreen;
import com.emptyPockets.box2d.shape.ShapeManager;
import com.emptyPockets.gui.ScreenSizeHelper;

public class BodyEditorScreen extends Box2DScreen{

	ShapeManager shapeManager;
	ShapeRenderer shapeRender;
	Button shapeManagerButton;
	float buttonSize = .6f; 
	
	public BodyEditorScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		shapeRender  = new ShapeRenderer();
		setClearColor(Color.DARK_GRAY);
		setShowDebug(true);
		shapeManager = new ShapeManager(getBox2DWorldCamera());
	}

	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		shapeManager.attach(input);
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		shapeManager.detatch(input);
	}
	
	public void createPanel(Stage stage){
		shapeManager.setBackground("default-rect");		
		shapeManager.setVisible(false);
		shapeManagerButton=shapeManager.getShowPanelButton();
		
		stage.addActor(shapeManager);
		stage.addActor(shapeManagerButton);
		
		setButtonSize(buttonSize);
		
		shapeManagerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				shapeManager.showControlPanel();	
				shapeManagerButton.setVisible(false);
			}
		});
	}
	
	
	@Override
	public void createStage(Stage stage) {
		createPanel(stage);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		setButtonSize(buttonSize);
		shapeManager.setBounds(0, 0, shapeManager.getWidth(), Gdx.graphics.getHeight());
		shapeManager.invalidateHierarchy();
	}
	
	public void setButtonSize(float size){
		float buttonWide = ScreenSizeHelper.getcmtoPxlX(size);
		float buttonHigh = ScreenSizeHelper.getcmtoPxlY(size);
		
		shapeManagerButton.setSize(buttonWide, buttonHigh);
		shapeManagerButton.setPosition(0, 0);
		shapeManager.setButtonSize(size);
	}
	
	@Override
	public void drawStage(float delta) {
		super.drawStage(delta);
		Table.drawDebug(getStage());
	}

	@Override
	public void drawScreen(float delta) {
		shapeRender.setProjectionMatrix(getBox2DWorldCamera().combined);
		shapeManager.drawShapes(shapeRender);
	}

	@Override
	public void createWorld(World world) {
	}

}
