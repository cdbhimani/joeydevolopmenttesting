package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.joey.chain.ReactorApp;

public class ClientScreen extends GameScreen {

	Stage stage;
	
	public ClientScreen(ReactorApp game) {
		super(game);
	}

	@Override
	public void show() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		super.show();
	}
	
	@Override
	public void hide() {
		super.hide();
	}
	
	@Override
	public void drawScreen(float delta) {
	}

	@Override
	public void drawOverlay(float delta) {

	}

	@Override
	public void updateLogic(float delta) {

	}

}
