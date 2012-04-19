package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;

public class ApplicationCentral {
	public static ApplicationListener getApplication(){
		return new TestPanel();
	}
}
