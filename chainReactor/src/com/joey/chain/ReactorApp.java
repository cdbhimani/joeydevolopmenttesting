package com.joey.chain;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.joey.chain.gui.GameScreen;
import com.joey.chain.gui.game.CellMatchGameScreen;
import com.joey.chain.gui.game.CellRotateChainScreen;
import com.joey.chain.gui.menus.HighScoreScreen;
import com.joey.chain.gui.menus.MenuScreen;
import com.joey.chain.gui.menus.SplashScreen;
import com.joey.chain.gui.network.ClientScreen;
import com.joey.chain.gui.network.ServerScreen;

public class ReactorApp extends Game{
	
	CellRotateChainScreen cellRotateScreen;
	CellMatchGameScreen cellMatchScreen;
	SplashScreen splashScreen;
	MenuScreen menuScreen;
	HighScoreScreen scoreScreen;
	ServerScreen serverScreen;
	ClientScreen clientScreen;
	
	InputMultiplexer inputProcessor;
	
	@Override
	public void create() {
		inputProcessor = new InputMultiplexer();
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(inputProcessor);

		cellRotateScreen = new CellRotateChainScreen(this);
		cellMatchScreen = new CellMatchGameScreen(this);
		splashScreen = new SplashScreen(this);
		menuScreen = new MenuScreen(this);
		scoreScreen = new HighScoreScreen(this);
		serverScreen = new ServerScreen(this);
		clientScreen = new ClientScreen(this);
//		setScreen(new FrameBufferScreen(this));
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

	public ServerScreen getServerScreen() {
		return serverScreen;
	}


	public ClientScreen getClientScreen() {
		return clientScreen;
	}



}


