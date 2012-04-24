package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.input.GestureDetector;
import com.joey.testing.TestingCellSpacePartition;
import com.me.mygdxgame.Gestures.GestureTesting;

public class ApplicationCentral {
	public static ApplicationListener getApplication(){
		return new TestingCellSpacePartition();
//		return new GestureTesting();
//		return new MyGdxGame();
//		return new TestPanel();
//		return new App();
//		return new UITest();
	}
}
