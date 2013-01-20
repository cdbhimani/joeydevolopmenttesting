package com.joey.png.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.joey.png.PongGame;

public class MenuScreen extends BaseScreen{

	public MenuScreen(PongGame game) {
		super(game);
	}
	
	@Override
	public void render(float delta, GL10 gl) {
		gl.glClearColor( 1, 0, 0, 1 );
		gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		game.setScreen(game.pongScreen);
		return true;
	}
}
