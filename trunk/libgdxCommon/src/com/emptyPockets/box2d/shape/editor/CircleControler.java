package com.emptyPockets.box2d.shape.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.box2d.shape.data.CircleShapeData;
import com.emptyPockets.gui.ViewportConvertor;

public class CircleControler extends  BaseShapeControler{
	CircleShapeData circleShape;

	Vector2 lastMouse = new Vector2();
	Vector2 firstMouse = new Vector2();
	
	boolean mouseInsideCircle = false;
	boolean mouseOnBorder = false;
	float mouseEdgeDist = 0;
	
	int segmentCount = 100;
	
	public CircleControler(ViewportConvertor owner) {
		super(owner);
	}

	public void clearMouseState(){
		mouseInsideCircle = false;
		mouseOnBorder = false;
	}
	public void updateMouseState(){
		clearMouseState();
		float mouseTouchSize= circleShape.getCircle().radius+owner.panelToCam(minContactDistance);
		
		mouseEdgeDist = lastMouse.dst(circleShape.getCircle().x, circleShape.getCircle().y)-circleShape.getCircle().radius;
		if(lastMouse.dst2(circleShape.getCircle().x, circleShape.getCircle().y) < circleShape.getCircle().radius*circleShape.getCircle().radius){
			mouseInsideCircle=true;
		}else if(lastMouse.dst2(circleShape.getCircle().x, circleShape.getCircle().y) < mouseTouchSize*mouseTouchSize){
			mouseOnBorder = true;
		}
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		if(circleShape == null || state == ControlState.DISABLED){
			return false;
		}
		return super.tap(x, y, count, button);
	}
	
	@Override
	public boolean mouseMoved(int x, int y) {
		if(circleShape== null || state == ControlState.DISABLED){
			return false;
		}
		
		synchronized (circleShape.getCircle()) {
			owner.camToPanel(x, y, lastMouse);	
			updateMouseState();
		}
		return super.mouseMoved(x, y);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if(circleShape == null || state == ControlState.DISABLED){
			return false;
		}
		
		synchronized (circleShape.getCircle()) {
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
		if(circleShape == null || state == ControlState.DISABLED){
			return false;
		}
		
		boolean returnValue = false;
		synchronized (circleShape.getCircle()) {
			//Building new Circle
			if(newShape){
				owner.camToPanel(x, y, lastMouse);	
				circleShape.getCircle().x = firstMouse.x;
				circleShape.getCircle().y = firstMouse.y;
				circleShape.getCircle().radius = lastMouse.dst(firstMouse);
				returnValue = true;
			}else{
				//Change occouring
				Vector2 currentMouse= new Vector2();
				owner.camToPanel(x, y, currentMouse);	
				
				
				if(mouseInsideCircle){
					float dx = currentMouse.x-lastMouse.x;
					float dy = currentMouse.y-lastMouse.y;
					circleShape.getCircle().x += dx;
					circleShape.getCircle().y += dy;
					returnValue = true;
				}else if(mouseOnBorder){
					float rN = currentMouse.dst(circleShape.getCircle().x, circleShape.getCircle().y)-circleShape.getCircle().radius;
					circleShape.getCircle().radius -= mouseEdgeDist-rN;
					returnValue = true;
				}
				
				lastMouse.set(currentMouse);
			}
		}
		return returnValue || super.touchDragged(x,y, pointer);
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(circleShape== null || state == ControlState.DISABLED){
			return false;
		}
		boolean returnValue = false;
		
		synchronized (circleShape.getCircle()) {
			owner.camToPanel(x, y, lastMouse);	
			clearMouseState();
			
			if(newShape){
				newShape = false;
				circleShape.getCircle().x = firstMouse.x;
				circleShape.getCircle().y = firstMouse.y;
				circleShape.getCircle().radius = lastMouse.dst(firstMouse);
				returnValue = true;
			}
		}
		return returnValue||super.touchUp(x, y, pointer, button);
	}
	
	public void draw(ShapeRenderer shape){
		if(circleShape == null){
			return;
		}
		float mouseTouchSize= owner.panelToCam(minContactDistance);
		synchronized (circleShape.getCircle()) {
			Gdx.gl.glLineWidth(2f);
			shape.begin(ShapeType.Circle);
			shape.setColor(mouseInsideCircle?shapeHighlightColor:shapeColor);
			shape.circle(circleShape.getCircle().x, circleShape.getCircle().y, circleShape.getCircle().radius,segmentCount);
			shape.end();
			
			Gdx.gl.glLineWidth(1f);
			if(state == ControlState.EDIT)
			{
				shape.begin(ShapeType.Circle);
				shape.setColor(mouseOnBorder?controlHighlightColor:controlColor);
				shape.circle(circleShape.getCircle().x, circleShape.getCircle().y, circleShape.getCircle().radius+mouseTouchSize,segmentCount);
				shape.end();
			}
		}
		
	}

	public Circle getCircle() {
		return circleShape.getCircle();
	}

	public void setCircle(CircleShapeData circle) {
		this.circleShape = circle;
	}
	
}
