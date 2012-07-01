package com.joey.OpenTyroin;

import com.badlogic.gdx.ApplicationListener;

public class ApplicationCentral {
	public static ApplicationListener getApplication(){
		return new Tyrian();
	}
}
