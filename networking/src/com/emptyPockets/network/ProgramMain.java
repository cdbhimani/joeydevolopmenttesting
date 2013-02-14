package com.emptyPockets.network;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.emptyPockets.gui.GameScreen;
import com.emptyPockets.gui.TestScreen;

public class ProgramMain extends Game{

	InputMultiplexer input;
	@Override
	public void create() {
		input = new InputMultiplexer();
		Gdx.input.setInputProcessor(input);
		TestScreen screen = new TestScreen(input);
		setScreen(screen);
	}
	
}