package com.joey.chain.gui;

import java.text.Normalizer.Form;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
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
		StringBuilder message = new StringBuilder();
		message.append("Welcome To The Game");
		font.drawMultiLine(batch, message.toString(), Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		batch.end();
		
		if(displayStart+displayTime<System.currentTimeMillis()){
			game.screenTransistion(this, game.getMenuScreen());
		}
	}

	public void setDisplayTime(long displayTime) {
		this.displayTime = displayTime;
	}

}
