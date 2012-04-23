package com.joey.testing;

import com.badlogic.gdx.ApplicationListener;

public class ApplicationCentral {
	public static ApplicationListener getApplication(){
//		return new TestingCellSpacePartition();
		return new TestingClass();
//		return  new ShapeViewer();
//		return new SpeedTests();
	}
}
