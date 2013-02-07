package com.joey.chain.network;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Application.ApplicationType;

import junit.framework.TestCase;

public class LibgdxTest extends TestCase {

	@Override
	protected void setUp() throws Exception {	
		super.setUp();
		setupApp();
	}
	
	private void setupApp(){
		   Gdx.app = new Application() {
				int logLevel = LOG_DEBUG;
				@Override
				public void setLogLevel(int logLevel) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void postRunnable(Runnable runnable) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public int getVersion() {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public ApplicationType getType() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Preferences getPreferences(String name) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public long getNativeHeap() {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public long getJavaHeap() {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public Input getInput() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Graphics getGraphics() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Files getFiles() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Audio getAudio() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public void exit() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void debug (String tag, String message) {
					if (logLevel >= LOG_DEBUG) {
						System.out.println(tag + ": " + message);
					}
				}
				
				@Override
				public void debug (String tag, String message, Throwable exception) {
					if (logLevel >= LOG_DEBUG) {
						System.out.println(tag + ": " + message);
						exception.printStackTrace(System.out);
					}
				}

				public void log (String tag, String message) {
					if (logLevel >= LOG_INFO) {
						System.out.println(tag + ": " + message);
					}
				}

				@Override
				public void log (String tag, String message, Exception exception) {
					if (logLevel >= LOG_INFO) {
						System.out.println(tag + ": " + message);
						exception.printStackTrace(System.out);
					}
				}

				@Override
				public void error (String tag, String message) {
					if (logLevel >= LOG_ERROR) {
						System.err.println(tag + ": " + message);
					}
				}

				@Override
				public void error (String tag, String message, Throwable exception) {
					if (logLevel >= LOG_ERROR) {
						System.err.println(tag + ": " + message);
						exception.printStackTrace(System.err);
					}
				}

			};
	}
}
