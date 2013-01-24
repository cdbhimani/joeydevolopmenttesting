package com.joey.aitesting;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.joey.chain.ReactorApp;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "AiTesting";
		cfg.useGL20 = true;
		cfg.width = 600;
		cfg.height = 480;
		
		LwjglApplication app = new LwjglApplication(new ReactorApp(), cfg);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}
}
