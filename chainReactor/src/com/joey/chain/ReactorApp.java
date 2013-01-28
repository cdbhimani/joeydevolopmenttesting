package com.joey.chain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.joey.chain.gui.GameScreen;
import com.joey.chain.gui.HighScoreScreen;
import com.joey.chain.gui.MenuScreen;
import com.joey.chain.gui.ReactorScreen;
import com.joey.chain.gui.ReactorViewerScreen;
import com.joey.chain.gui.SplashScreen;

public class ReactorApp extends Game{
	
	ReactorViewerScreen gameScreen;
	SplashScreen splashScreen;
	MenuScreen menuScreen;
	HighScoreScreen scoreScreen;
	
	InputMultiplexer inputProcessor;
	
	@Override
	public void create() {
		inputProcessor = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputProcessor);
		
		// TODO Auto-generated method stub
		gameScreen = new ReactorViewerScreen(this);
		splashScreen = new SplashScreen(this);
		menuScreen = new MenuScreen(this);
		scoreScreen = new HighScoreScreen(this);
		
		setScreen(gameScreen);
	}
	
	public void screenTransistion(GameScreen source, GameScreen dest){
		
		if(source == splashScreen){
			setScreen(dest);
			splashScreen.dispose();
			splashScreen = null;
		}else if(source==menuScreen){
			setScreen(dest);
		}
	}

	public ReactorViewerScreen getGameScreen() {
		return gameScreen;
	}



	public SplashScreen getSplashScreen() {
		return splashScreen;
	}



	public MenuScreen getMenuScreen() {
		return menuScreen;
	}



	public HighScoreScreen getScoreScreen() {
		return scoreScreen;
	}

	public InputMultiplexer getInputProcessor() {
		return inputProcessor;
	}

	public void setInputProcessor(InputMultiplexer inputProcessor) {
		this.inputProcessor = inputProcessor;
	}


}


