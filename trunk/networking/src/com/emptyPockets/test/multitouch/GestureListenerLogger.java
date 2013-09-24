package com.emptyPockets.test.multitouch;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.logging.Console;

public class GestureListenerLogger implements GestureListener {

	GestureDetector detector;

	public GestureListenerLogger() {
		detector = new GestureDetector(this);
	}

	public void attach(InputMultiplexer input) {
		input.addProcessor(detector);
	}

	public void detatch(InputMultiplexer input) {
		input.removeProcessor(detector);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		Console.printf("Gesture : touchdown [x,y-(%f,%f) pointer-(%d) button-(%d)]\n", x, y, pointer, button);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		Console.printf("Gesture : tap [x,y-(%f,%f) count-(%d) button-(%d)]\n", x, y, count, button);
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		Console.printf("Gesture : longpress [x,y-(%f,%f)]\n", x, y);
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		Console.printf("Gesture : fling [Vx,Vy-(%f,%f) button-(%d)]\n", velocityX, velocityY, button);
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		Console.printf("Gesture : pan [x,y-(%f,%f) delta-(%f,%f)]\n", x, y, deltaX, deltaY);
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		Console.printf("Gesture : zoom [initialDistance (%f), distance(%f)]\n", initialDistance, distance);
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		Console.printf("Gesture : pinch [Pai-(%s) Pbi-(%s) ~ Pa-(%s) Pb-(%s)]\n", initialPointer1.toString(), initialPointer2.toString(), pointer1.toString(), pointer2.toString());
		return false;
	}

}
