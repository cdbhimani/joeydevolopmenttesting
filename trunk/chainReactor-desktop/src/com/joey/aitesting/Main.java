package com.joey.aitesting;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.joey.chain.ChainReactionApp;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "AiTesting";
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 800;

		LwjglApplication app = new LwjglApplication(new ChainReactionApp(), cfg);
	}
}
