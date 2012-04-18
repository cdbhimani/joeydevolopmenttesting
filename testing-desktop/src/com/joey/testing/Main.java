package com.joey.testing;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.joey.testing.game.shapes.ShapeViewer;
import com.joey.testing.speedTests.SpeedTests;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "testing";
		cfg.useGL20 = false;

		if (false) {
			cfg.width = 1600;
			cfg.height = 900;
			cfg.fullscreen = true;
			cfg.resizable = false;
		} else {
			cfg.width = 800;
			cfg.height = 600;
			cfg.fullscreen = false;
			cfg.resizable = true;
		}

		ApplicationListener test = ApplicationCentral.getApplication();

		

		LwjglApplication app = new LwjglApplication(test, cfg);
	}
}
