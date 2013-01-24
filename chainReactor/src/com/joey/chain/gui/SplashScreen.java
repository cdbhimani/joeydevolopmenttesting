package com.joey.chain.gui;

import java.text.Normalizer.Form;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.joey.chain.ReactorApp;

public class SplashScreen extends GameScreen {

	SpriteBatch batch;
	BitmapFont font;
	
	Pixmap pixmap;
	Texture texture;
	
	public SplashScreen(ReactorApp game) {
		super(game);
		// TODO Auto-generated constructor stub
		createTexture();
	}
	
	
	public void createTexture(){
		pixmap = new Pixmap(512,512, Format.RGBA8888);
		texture = new Texture(pixmap, false);
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		batch = new SpriteBatch();
		font = new BitmapFont();
	}
	
	public void updateTexture(){
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		
		pixmap.setColor(Color.BLACK);
		pixmap.fillCircle(MathUtils.random(30, 100), MathUtils.random(30, 100), 10);
		
		pixmap.drawLine(0, 0,pixmap.getWidth(), pixmap.getHeight());
		
		texture.draw(pixmap, 0, 0);
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		updateTexture();
		
		batch.begin();
		batch.draw(texture, 0, 0);
		
		
		font.setColor(Color.RED);
		StringBuilder message = new StringBuilder();
		message.append("Time :"+(int)(delta*1000)+"\n");
		message.append("Wide :"+pixmap.getWidth()+"\n");
		message.append("High :"+pixmap.getHeight()+"\n");
		font.drawMultiLine(batch, message.toString(), 10, 50);
		batch.end();
	}

}
