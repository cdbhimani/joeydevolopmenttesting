package com.joey.png;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.joey.png.box2D.Box2DApplication;

public class ApplicationCentral {


	public static Game getGame() {
		PongGame game = new PongGame();
		return game;
	}
	
	public static ApplicationListener getApplication(){
		return new Box2DApplication();
		//return new LibGdxApp();
	}
}
