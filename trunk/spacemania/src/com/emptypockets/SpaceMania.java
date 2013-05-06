package com.emptypockets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.emptypockets.client.ClientScreen;

public class SpaceMania extends Game {
	InputMultiplexer input;

	@Override
	public void create() {
		input = new InputMultiplexer();
		Gdx.input.setInputProcessor(input);
		Screen screen = new ClientScreen(input);
		setScreen(screen);
	}
}
