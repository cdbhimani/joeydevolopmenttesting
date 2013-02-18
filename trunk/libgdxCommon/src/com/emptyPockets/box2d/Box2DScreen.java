package com.emptyPockets.box2d;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptyPockets.gui.StageScreen;

public class Box2DScreen extends StageScreen{
	World world;
	Vector2 gravity;
	int velocityIterations = 2;
	int positionIterations = 2;
	
	public Box2DScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		gravity = new Vector2();
		world = new World(gravity, true);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		updateWorld(delta);
	}
	
	public void updateWorld(float delta){
		world.step(delta, velocityIterations, positionIterations);
	}
	
	@Override
	public void createStage(Stage stage) {
		// TODO Auto-generated method stub
		
	}
	
	public void createWorld(World world){
		
	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawOverlay(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLogic(float delta) {
		// TODO Auto-generated method stub
		
	}

}
