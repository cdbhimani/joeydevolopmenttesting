package com.emptypockets;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emptypockets.networking.log.ServerLogger;
import com.emptypockets.networking.server.ServerManager;
import com.esotericsoftware.minlog.Log;

public class SpaceManiaDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "spacemania";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 320;
		
		new LwjglApplication(new SpaceMania(), cfg);
	}
}
