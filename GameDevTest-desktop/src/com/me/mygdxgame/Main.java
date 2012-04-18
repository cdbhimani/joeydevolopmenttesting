package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "GameDevTest";
		cfg.useGL20 = false;
		cfg.width = 50;
		cfg.height = 50;
		
		
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
