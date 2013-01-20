package com.joey.png;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Pong";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 320;
		
		//Game game =  ApplicationCentral.getGame();
		//new LwjglApplication(game, cfg);
		//game.create();
		
		new LwjglApplication(ApplicationCentral.getApplication(), cfg);
	}
}
