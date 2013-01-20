package com.joey.png;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.joey.png.screen.MenuScreen;
import com.joey.png.screen.PongScreen;

public class PongGame extends Game {

	public PongScreen pongScreen;
	public MenuScreen menuScreen;
	
	InputMultiplexer inputMultiplexer;
	
	public PongGame(){
		super();
	}
	
	@Override
	public void create() {
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		pongScreen = new PongScreen(this);
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	public InputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	public void setInputMultiplexer(InputMultiplexer inputMultiplexer) {
		this.inputMultiplexer = inputMultiplexer;
	}

}
