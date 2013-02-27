package com.emptyPockets.network;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emptyPockets.main.ProgramMain;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "networking";
		cfg.useGL20 = true;
		cfg.width = 300;
		cfg.height = 300;
		
		new LwjglApplication(new ProgramMain(), cfg);
	}
}
