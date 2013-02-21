package com.emptyPockets.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;
import com.emptyPockets.gui.GameScreen;
import com.emptyPockets.gui.BasicCarTestScreen;
import com.emptyPockets.gui.Network;
import com.emptyPockets.gui.NetworkScreen;

public class ProgramMain extends Game{

	InputMultiplexer input;
	@Override
	public void create() {
		input = new InputMultiplexer();
		Gdx.input.setInputProcessor(input);
//		BasicCarTestScreen screen = new BasicCarTestScreen(input);
//		NetworkScreen screen = new NetworkScreen(input);
//		Network screen = new Network(input);
		EntityEditorScreen screen = new EntityEditorScreen(input);
		setScreen(screen);
	}
	
}