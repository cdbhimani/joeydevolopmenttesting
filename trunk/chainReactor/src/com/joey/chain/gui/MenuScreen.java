package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.joey.chain.ReactorApp;

public class MenuScreen extends GameScreen {

	SpriteBatch batch;
	BitmapFont font;
	
	public MenuScreen(ReactorApp game) {
		super(game);
		setClearColor(new Color(1,1,1,1));
	}
	
	@Override
	public void show() {
		super.show();
		batch = new SpriteBatch();
		font = new BitmapFont();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
	
		
		batch.setProjectionMatrix(stageCamera.combined);
		
		batch.begin();
	
		font.setColor(Color.RED);
		StringBuilder message = new StringBuilder();
		message.append("Main Menu");
		font.drawMultiLine(batch, message.toString(), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		batch.end();
	}
	
	@Override
	public boolean tap(int x, int y, int count) {
		boolean done = super.tap(x, y, count);
		
		game.screenTransistion(this, game.getGameScreen());
		
		return done;
	}
}
