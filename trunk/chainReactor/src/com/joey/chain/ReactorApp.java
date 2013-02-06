package com.joey.chain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.joey.chain.gui.CellMatchGameScreen;
import com.joey.chain.gui.GameScreen;
import com.joey.chain.gui.HighScoreScreen;
import com.joey.chain.gui.MenuScreen;
import com.joey.chain.gui.CellRotateChainScreen;
import com.joey.chain.gui.SplashScreen;

public class ReactorApp extends Game{
	
	CellRotateChainScreen cellRotateScreen;
	CellMatchGameScreen cellMatchScreen;
	SplashScreen splashScreen;
	MenuScreen menuScreen;
	HighScoreScreen scoreScreen;
	
	InputMultiplexer inputProcessor;
	
	@Override
	public void create() {
		inputProcessor = new InputMultiplexer();
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(inputProcessor);

		
		// TODO Auto-generated method stub
		cellRotateScreen = new CellRotateChainScreen(this);
		cellMatchScreen = new CellMatchGameScreen(this);
		splashScreen = new SplashScreen(this);
		menuScreen = new MenuScreen(this);
		scoreScreen = new HighScoreScreen(this);
		
//		setScreen(new FrameBufferScreen(this));
//		setScreen(new CellMatchGameViewer(this));
		setScreen(splashScreen);
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

	public CellRotateChainScreen getCellRotateScreen() {
		return cellRotateScreen;
	}

	public CellMatchGameScreen getCellMatchScreen() {
		return cellMatchScreen;
	}


}


