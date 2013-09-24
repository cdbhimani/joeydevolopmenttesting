package com.emptypockets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.emptypockets.client.ClientScreen;
import com.emptypockets.grid.GridScreen;

public class SpaceMania extends Game {
	InputMultiplexer input;

	@Override
	public void create() {
		input = new InputMultiplexer();
		Gdx.input.setInputProcessor(input);
		//Screen screen = new ClientScreen(input);
		GridScreen screen = new GridScreen(input);
		setScreen(screen);
	}
}
