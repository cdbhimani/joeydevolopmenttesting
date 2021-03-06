package com.emptypockets;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emptypockets.networking.log.ServerLogger;
import com.esotericsoftware.minlog.Log;

public class SpaceManiaDesktop {
	public static void main(String[] args) {
		ServerLogger.INFO();
		Log.INFO();
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "spacemania";
		cfg.useGL20 = true;
		cfg.width = 480;
		cfg.height = 320;
		
		new LwjglApplication(new SpaceMania(), cfg);
	}
}
