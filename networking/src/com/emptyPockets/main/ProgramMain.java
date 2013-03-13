package com.emptyPockets.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.emptyPockets.test.ErrorTesting;
import com.emptyPockets.utils.file.FileSelector;

public class ProgramMain extends Game{
	FileSelector fileSelector;
	
	InputMultiplexer input;
	@Override
	public void create() {
		input = new InputMultiplexer();
		Gdx.input.setInputProcessor(input);
//		BasicCarTestScreen screen = new BasicCarTestScreen(input);
//		NetworkScreen screen = new NetworkScreen(input);
//		Network screen = new Network(input);
//		EntityEditorScreen screen = new EntityEditorScreen(input);
//		BodyEditorScreen screen = new BodyEditorScreen(input);
//		SpaceShipTesting screen = new SpaceShipTesting(input);
		ErrorTesting screen = new ErrorTesting(input);
		setScreen(screen);
	}
	
}