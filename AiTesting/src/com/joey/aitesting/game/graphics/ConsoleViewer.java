package com.joey.aitesting.game.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ConsoleViewer {
	
	ConsoleLogger console;
	BitmapFont consoleFont;
	SpriteBatch consoleBatch;
	
	public ConsoleViewer(ConsoleLogger log){
		this.console = log;
		
		consoleBatch = new SpriteBatch();
		consoleFont = new BitmapFont();
		
	}
	
	public void draw(Camera cam){
		consoleBatch.setProjectionMatrix(cam.combined);
		
		consoleFont.setScale(1f);
		consoleFont.setColor(Color.WHITE);
		consoleBatch.begin();
		consoleFont.drawMultiLine(consoleBatch, console.toString(), 0,Gdx.graphics.getHeight());
		consoleBatch.end();
	}
	
}
