package com.emptyPockets.bodyEditor.main.controls;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;

public class RectangleControler extends EntityControler{
	float minPixelDistance = 10;
	
	public RectangleControler(EntityEditorScreen owner) {
		super(owner);
	}

	Rectangle rect;
	
	Rectangle tL = new Rectangle();
	Rectangle tM = new Rectangle();
	Rectangle tR = new Rectangle();
	Rectangle cL = new Rectangle();
	Rectangle cR = new Rectangle();
	Rectangle bL = new Rectangle();
	Rectangle bM = new Rectangle();
	Rectangle bR = new Rectangle();
	
	boolean tLSelected = false;
	boolean tMSelected = false;
	boolean tRSelected = false;
	boolean cLSelected = false;
	boolean cRSelected = false;
	boolean bLSelected = false;
	boolean bMSelected = false;
	boolean bRSelected = false;
	
	Vector2 firstMouse = new Vector2();
	Vector2 lastMouse = new Vector2();
	
	public void updateMouseSelection(){
	}
	
	private float getMouseDistance(){
		return owner.panelToCam(minPixelDistance);
	}
	
	private void clearMouseSelection(){
		tLSelected = false;
		tMSelected = false;
		tRSelected = false;
		cLSelected = false;
		cRSelected = false;
		bLSelected = false;
		bMSelected = false;
		bRSelected = false;
	}
	
	private void updateSelectionRegions(){
		float mouseDistance = getMouseDistance();
		
		//Top Left
		tL.x = rect.x-mouseDistance;
		tL.y = rect.y;
		tL.width = mouseDistance;
		tL.height = rect.height;
		
		//Top Left
		tM.x = rect.x-mouseDistance;
		tM.y = rect.y-mouseDistance;
		tM.width = mouseDistance;
		tM.height = mouseDistance;
		
		//Top Left
		tR.x = rect.x;
		tR.y = rect.y-mouseDistance;
		tR.width = rect.width;
		tR.height = mouseDistance;
				
	}
	@Override
	public boolean tap(float x, float y, int count, int button) {
		return super.tap(x, y, count, button);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		owner.camToPanel(x, y, firstMouse);
		return super.touchDown(x, y, pointer, button);
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		owner.camToPanel(x, y, lastMouse);
		return super.touchDragged(x, x, pointer);
	}
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return super.touchUp(x, y, pointer, button);
	}

	public void draw(ShapeRenderer shape){
		shape.begin(ShapeType.Rectangle);
		stAa
		shape.end();
	}
}
