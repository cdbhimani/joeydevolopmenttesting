package com.joey.aitesting;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "AiTesting";
		cfg.useGL20 = false;
		cfg.width = 100;
		cfg.height = 100;
		
		new LwjglApplication(ApplicationCentral.getApplication(), cfg);
	}
}
