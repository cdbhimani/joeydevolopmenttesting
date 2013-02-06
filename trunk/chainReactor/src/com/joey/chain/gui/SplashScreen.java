package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.joey.chain.ReactorApp;

public class SplashScreen extends GameScreen {

	SpriteBatch batch;
	BitmapFont font;
	long displayStart = 0;
	long displayTime = 1000;
	
	public SplashScreen(ReactorApp game) {
		super(game);
		setClearColor(new Color(1,1,1,1));
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		batch = new SpriteBatch();
		font = new BitmapFont();
		displayStart = System.currentTimeMillis();
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
		
		String textA = "Welcome to the game";
		TextBounds boundA = font.getMultiLineBounds(textA);
		font.drawMultiLine(batch, textA, Gdx.graphics.getWidth()/2-boundA.width/2, Gdx.graphics.getHeight()/2+boundA.height/2);
		batch.end();
		
		if(displayStart+displayTime<System.currentTimeMillis()){
			game.screenTransistion(this, game.getMenuScreen());
		}
	}

	public void setDisplayTime(long displayTime) {
		this.displayTime = displayTime;
	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawOverlay(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLogic(float delta) {
		// TODO Auto-generated method stub
		
	}

}
