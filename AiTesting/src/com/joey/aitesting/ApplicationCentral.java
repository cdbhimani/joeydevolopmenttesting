package com.joey.aitesting;

import com.badlogic.gdx.ApplicationListener;

public class ApplicationCentral {
	public static ApplicationListener getApplication() {
//		return new ShapeFlipThingGame();
//		 return new RenderSpeedTest();
//		 return new TestingCellSpacePartition();

		 return new MyGdxGame();
//		 return new ShapeViewer();
//		 return new SpeedTests();
	}
}
