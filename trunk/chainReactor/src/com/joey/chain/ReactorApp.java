package com.joey.chain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.joey.chain.games.cellMatchSetGame.CellMatchEngine;
import com.joey.chain.gui.GameScreen;
import com.joey.chain.gui.game.CellMatchGameScreen;
import com.joey.chain.gui.game.CellRotateChainScreen;
import com.joey.chain.gui.game.CellSwapGameScreen;
import com.joey.chain.gui.menus.HighScoreScreen;
import com.joey.chain.gui.menus.MenuScreen;
import com.joey.chain.gui.menus.SplashScreen;
import com.joey.chain.gui.network.NetworkScreen;

public class ReactorApp extends Game{
	
	CellRotateChainScreen cellRotateScreen;
	CellMatchGameScreen cellMatchScreen;
	CellSwapGameScreen cellSwapScreen;
	
	SplashScreen splashScreen;
	MenuScreen menuScreen;
	HighScoreScreen scoreScreen;
	NetworkScreen networkScreen;
	
	InputMultiplexer inputProcessor;
	
	@Override
	public void create() {
		inputProcessor = new InputMultiplexer();
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(inputProcessor);

		cellRotateScreen = new CellRotateChainScreen(this);
		cellMatchScreen = new CellMatchGameScreen(this);
		cellSwapScreen = new CellSwapGameScreen(this);
		splashScreen = new SplashScreen(this);
		menuScreen = new MenuScreen(this);
		scoreScreen = new HighScoreScreen(this);
		networkScreen = new NetworkScreen(this);
//		setScreen(new FrameBufferScreen(this));
		setScreen(cellSwapScreen);
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

	public NetworkScreen getNetworkScreen() {
		return networkScreen;
	}

}


