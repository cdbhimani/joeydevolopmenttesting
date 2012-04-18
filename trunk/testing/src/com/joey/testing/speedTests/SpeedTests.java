package com.joey.testing.speedTests;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.joey.testing.game.graphics.ConsoleLogger;

public class SpeedTests implements ApplicationListener, Runnable{

	OrthographicCamera cam;
	ConsoleLogger console = new ConsoleLogger();
	BitmapFont consoleFont;
	SpriteBatch consoleBatch;
	
	@Override
	public void create() {
		consoleBatch = new SpriteBatch();
		consoleFont = new BitmapFont();
	}

	@Override
	public void resize(int width, int height) {
		cam = new OrthographicCamera(width, height);
		cam.translate(width / 2, height / 2, 0);
	}

	@Override
	public void render() {
		consoleFont.setScale(1f);
		consoleFont.setColor(Color.WHITE);
		consoleBatch.begin();
		consoleFont.drawMultiLine(consoleBatch, console.toString(), 0,
				Gdx.graphics.getHeight());
		consoleBatch.end();	
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		while(true){
			
		}
	}

	

}
