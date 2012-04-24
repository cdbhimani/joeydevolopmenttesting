package com.joey.aitesting.game.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class ConsoleLogger implements InputProcessor, GestureListener {
	int maxConsoleSize = 1000;
	int maxConsoleLines = 10;
	StringBuffer console = new StringBuffer();

	public ConsoleLogger() {
		
	}

	public int getMaxConsoleLines() {
		return maxConsoleLines;
	}

	public void setMaxConsoleLines(int maxConsoleLines) {
		this.maxConsoleLines = maxConsoleLines;
	}



	public void println(String s) {
		console.append(s);
		console.append("\n");
		trim();
	}

	public void print(String s) {
		console.append(s);
		trim();
	}

	public void printf(String s, Object... args) {
		console.append(String.format(s, args));
		trim();
	}

	private void trim() {
		int extra = console.length() - maxConsoleSize;
		if (extra > 0)
			console.delete(0, extra);

		int lines = countOccourence('\n');
		if (lines > maxConsoleLines) {
			int idx = indexOf('\n', (lines - maxConsoleLines));
			if (idx != -1) {
				console.delete(0, idx);
			}
		}

	}

	private int indexOf(char c, int times) {
		int count = 0;
		for (int i = 0; i < console.length(); i++) {
			if (console.charAt(i) == c) {
				count++;
				if (count == times) {
					return i;
				}
			}
		}

		return -1;
	}

	private int countOccourence(char c) {
		int count = 0;
		for (int i = 0; i < console.length(); i++) {
			if (console.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}

	public int getMaxConsoleSize() {
		return maxConsoleSize;
	}

	public void setMaxConsoleSize(int maxConsoleSize) {
		this.maxConsoleSize = maxConsoleSize;
	}

	public String toString() {
		return console.toString();
	}

	@Override
	public boolean keyDown(int keycode) {
		println("KEY DOWN[" + keycode + "]");
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		println("KEY UP[" + keycode + "]");
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		println("KEY TYPED[" + (int)character + "]");
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		println("TOUCH DOWN [" + x + "," + y + "]"+" - "+pointer+" - "+button);
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		println("TOUCH UP [" + x + "," + y + "]"+" - "+pointer+" - "+button);
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		println("TOUCH DRAGGED [" + x + "," + y + "]"+" - "+pointer);
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		println("TOUCH MOVED [" + x + "," + y + "]");
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		println("Scrolled[" + amount + "]");
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer) {
		println("Gesture - TOUCH DOWN [" + x + "," + y + "]"+" - "+pointer);
		return false;
	}

	@Override
	public boolean tap(int x, int y, int count) {
		println("Gesture - TAP[" + x + "," + y + "]"+" - "+count);
		return false;
	}

	@Override
	public boolean longPress(int x, int y) {
		println("Gesture - LONG Press[" + x + "," + y + "]");
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY) {
		println("Gesture - fling[" + velocityX + "," + velocityY + "]");
		return false;
	}

	@Override
	public boolean pan(int x, int y, int deltaX, int deltaY) {
		println("Gesture - pan[" + x + "," + y + "]"+" - "+deltaX+" - "+deltaY);
		return false;
	}

	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		println("Gesture - zoom[" + originalDistance+ "," + currentDistance + "]");
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialFirstPointer,Vector2 initialSecondPointer, Vector2 firstPointer,Vector2 secondPointer) {
		println("Gesture - pinch[" + initialFirstPointer.x+ "," + initialFirstPointer.y+ "]"
				+" - [" + initialSecondPointer.x+ "," + initialSecondPointer.y+ "]"
				+" - [" + firstPointer.x+ "," + firstPointer.y+ "]"
				+" - [" + secondPointer.x+ "," + secondPointer.y+ "]"
				);
		return false;
	}
}
