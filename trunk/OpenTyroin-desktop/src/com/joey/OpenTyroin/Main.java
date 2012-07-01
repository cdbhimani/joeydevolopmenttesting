package com.joey.OpenTyroin;

import java.awt.GraphicsEnvironment;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "OpenTyroin";
		cfg.useGL20 = false;
		cfg.width = 512;
		cfg.height = 400;
		
		cfg.fullscreen = false;
		if(cfg.fullscreen){
			cfg.resizable=false;
			cfg.width = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
			cfg.height = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
		}
		
		Game game = (Game) ApplicationCentral.getApplication();
		
		LwjglApplication app = new LwjglApplication(game, cfg);
		
	}
}
