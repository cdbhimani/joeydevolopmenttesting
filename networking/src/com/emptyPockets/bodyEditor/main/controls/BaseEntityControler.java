package com.emptyPockets.bodyEditor.main.controls;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;
import com.emptyPockets.gui.ScreenSizeHelper;

public abstract class BaseEntityControler  implements InputProcessor, GestureListener {
	
	protected EntityEditorScreen owner;
	GestureDetector gestureDetector;
	protected float minContactDistance = ScreenSizeHelper.getcmtoPxlX(.4f);
	protected Color shapeColor = Color.BLUE;
	protected Color controlColor = Color.LIGHT_GRAY;
	protected Color highLightColor = Color.GREEN;
	
	public BaseEntityControler(EntityEditorScreen owner){
		this.owner = owner;
		gestureDetector = new GestureDetector(this);
		
		shapeColor.a = 0.8f;
		controlColor.a = 0.4f;
		highLightColor.a = 0.8f;
	}
	
	public void attach(InputMultiplexer owner){
		owner.addProcessor(this);
		owner.addProcessor(gestureDetector);
	}
	
	public void detach(InputMultiplexer owner){
		owner.removeProcessor(this);
		owner.removeProcessor(gestureDetector);
	}
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
