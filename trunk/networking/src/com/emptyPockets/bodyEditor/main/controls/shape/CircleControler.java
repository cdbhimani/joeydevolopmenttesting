package com.emptyPockets.bodyEditor.main.controls.shape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;
import com.emptyPockets.utils.maths.MathsToolkit;

public class CircleControler extends  BaseShapeControler{
	Circle circle;

	Vector2 lastMouse = new Vector2();
	Vector2 firstMouse = new Vector2();
	
	boolean mouseInsideCircle = false;
	boolean mouseOnBorder = false;
	float mouseEdgeDist = 0;
	
	int segmentCount = 100;
	
	public CircleControler(EntityEditorScreen owner) {
		super(owner);
	}

	public void clearMouseState(){
		mouseInsideCircle = false;
		mouseOnBorder = false;
	}
	public void updateMouseState(){
		clearMouseState();
		float mouseTouchSize= circle.radius+owner.panelToCam(minContactDistance);
		
		mouseEdgeDist = lastMouse.dst(circle.x, circle.y)-circle.radius;
		if(lastMouse.dst2(circle.x, circle.y) < circle.radius*circle.radius){
			mouseInsideCircle=true;
		}else if(lastMouse.dst2(circle.x, circle.y) < mouseTouchSize*mouseTouchSize){
			mouseOnBorder = true;
		}
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		if(circle == null || state == ControlState.DISABLED){
			return false;
		}
		return super.tap(x, y, count, button);
	}
	
	@Override
	public boolean mouseMoved(int x, int y) {
		if(circle == null || state == ControlState.DISABLED){
			return false;
		}
		
		synchronized (circle) {
			owner.camToPanel(x, y, lastMouse);	
			updateMouseState();
		}
		return super.mouseMoved(x, y);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if(circle == null || state == ControlState.DISABLED){
			return false;
		}
		
		synchronized (circle) {
			owner.camToPanel(x, y, firstMouse);	
			lastMouse.set(firstMouse);
			updateMouseState();
		}
		
		if(mouseInsideCircle || (mouseOnBorder&&state==ControlState.EDIT)){
			return true;
		}else{		
			return super.touchDown(x, y, pointer, button);
		}
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if(circle == null || state == ControlState.DISABLED){
			return false;
		}
		
		boolean returnValue = false;
		synchronized (circle) {
			//Building new Circle
			if(newShape){
				owner.camToPanel(x, y, lastMouse);	
				circle.x = firstMouse.x;
				circle.y = firstMouse.y;
				circle.radius = lastMouse.dst(firstMouse);
				returnValue = true;
			}else{
				//Change occouring
				Vector2 currentMouse= new Vector2();
				owner.camToPanel(x, y, currentMouse);	
				
				
				if(mouseInsideCircle){
					float dx = currentMouse.x-lastMouse.x;
					float dy = currentMouse.y-lastMouse.y;
					circle.x += dx;
					circle.y += dy;
					returnValue = true;
				}else if(mouseOnBorder){
					float rN = currentMouse.dst(circle.x, circle.y)-circle.radius;
					circle.radius -= mouseEdgeDist-rN;
					returnValue = true;
				}
				
				lastMouse.set(currentMouse);
			}
		}
		return returnValue || super.touchDragged(x,y, pointer);
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(circle == null || state == ControlState.DISABLED){
			return false;
		}
		boolean returnValue = false;
		
		synchronized (circle) {
			owner.camToPanel(x, y, lastMouse);	
			clearMouseState();
			
			if(newShape){
				newShape = false;
				circle.x = firstMouse.x;
				circle.y = firstMouse.y;
				circle.radius = lastMouse.dst(firstMouse);
				returnValue = true;
			}
		}
		return returnValue||super.touchUp(x, y, pointer, button);
	}
	
	public void draw(ShapeRenderer shape){
		if(circle == null){
			return;
		}
		float mouseTouchSize= owner.panelToCam(minContactDistance);
		synchronized (circle) {
			Gdx.gl.glLineWidth(2f);
			shape.begin(ShapeType.Circle);
			shape.setColor(mouseInsideCircle?shapeHighlightColor:shapeColor);
			shape.circle(circle.x, circle.y, circle.radius,segmentCount);
			shape.end();
			
			Gdx.gl.glLineWidth(1f);
			if(state == ControlState.EDIT)
			{
				shape.begin(ShapeType.Circle);
				shape.setColor(mouseOnBorder?controlHighlightColor:controlColor);
				shape.circle(circle.x, circle.y, circle.radius+mouseTouchSize,segmentCount);
				shape.end();
			}
		}
		
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}
	
}
