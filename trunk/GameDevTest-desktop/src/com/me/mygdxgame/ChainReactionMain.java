package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.joey.testing.chainReaction.ChainReactionApp;

public class ChainReactionMain {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Test";
		cfg.useGL20 = false;
		cfg.width = 400;
		cfg.height = 400;
		
		ChainReactionApp app = new ChainReactionApp();
		new LwjglApplication(app, cfg);
	}
}
