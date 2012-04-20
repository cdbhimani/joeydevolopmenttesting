package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;

public class ApplicationCentral {
	public static ApplicationListener getApplication(){
		return new MyGdxGame();
//		return new TestPanel();
//		return new App();
//		return new UITest();
	}
}
