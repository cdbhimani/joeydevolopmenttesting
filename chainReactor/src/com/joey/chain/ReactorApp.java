package com.joey.chain;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.joey.chain.gui.GameScreen;
import com.joey.chain.gui.ReactorViewerScreen;
import com.joey.chain.gui.SplashScreen;
import com.joey.chain.model.Chain;
import com.joey.chain.model.Reactor;
import com.joey.chain.model.Chain.ChainState;

public class ReactorApp extends Game{
	ReactorViewerScreen screen;
	InputMultiplexer inputProcessor;
	
	@Override
	public void create() {
		inputProcessor = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputProcessor);
		
		// TODO Auto-generated method stub
		screen = new ReactorViewerScreen(this);
//		setScreen(new SplashScreen(this));
		setScreen(screen);
	}
	
	@Override
	public void setScreen(Screen screen) {
		//Remove old input processor
		if(getScreen() != null){
			if(getScreen() instanceof GameScreen){
				inputProcessor.removeProcessor((InputProcessor)getScreen());
				inputProcessor.removeProcessor(((GameScreen)screen).getGesture());
			}
		}
		
		//Add new input processor
		if(screen != null && screen instanceof GameScreen){
			inputProcessor.addProcessor((InputProcessor)screen);
			inputProcessor.addProcessor(((GameScreen)screen).getGesture());
		}
		super.setScreen(screen);
	}


}


