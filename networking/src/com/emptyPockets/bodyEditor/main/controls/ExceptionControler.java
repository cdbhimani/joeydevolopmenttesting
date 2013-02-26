package com.emptyPockets.bodyEditor.main.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;
import com.emptyPockets.utils.maths.MathsToolkit;

public class ExceptionControler extends EntityControler{
		
	boolean newRectangle = false;
	Rectangle rectangle;
	
	float borderGap = 3; 
	
	Vector2 firstMouse = new Vector2();
	Vector2 lastMouse = new Vector2();
	
	int rectMouseStateX = 0;
	int rectMouseStateY = 0;
	int mouseDragX = 0;
	int mouseDragY = 0;
	
	public ExceptionControler(EntityEditorScreen owner) {
		super(owner);
	}
	
	private float getMouseDistance(){
		return owner.panelToCam(minContactDistance);
	}
	
	private void updateMouseState(){
		clearMouseState();
		float mouseDistance = getMouseDistance();
		rectMouseStateX = LocationTest.testPosition(lastMouse.x, mouseDistance, rectangle.x, rectangle.x+rectangle.width);
		rectMouseStateY = LocationTest.testPosition(lastMouse.y, mouseDistance, rectangle.y, rectangle.y+rectangle.height);
	}
	
	private void clearMouseState(){
		rectMouseStateX = LocationTest.OUTSIDE;
		rectMouseStateY = LocationTest.OUTSIDE;
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		synchronized (rectangle) {
			if(count > 1){
				newRectangle = true;
			}
		}
		return super.tap(x, y, count, button);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		synchronized (rectangle) {
			owner.camToPanel(x, y, firstMouse);	
			lastMouse.set(firstMouse);
			updateMouseState();
			
			if(newRectangle){
				rectangle.x = firstMouse.x;
				rectangle.y = firstMouse.y;
				rectangle.width  = 1;
				rectangle.height = 1;
				MathsToolkit.validateRectangle(rectangle);
			}
			
			updateMouseState();
			mouseDragX = rectMouseStateX;
			mouseDragY = rectMouseStateY;
		}
		return super.touchDown(x, y, pointer, button);
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {

		synchronized (rectangle) {
			//Building new Rectangle
			if(newRectangle){
				owner.camToPanel(x, y, lastMouse);	
				rectangle.x = firstMouse.x;
				rectangle.y = firstMouse.y;
				rectangle.width = lastMouse.x - firstMouse.x;
				rectangle.height = lastMouse.y - firstMouse.y;
				MathsToolkit.validateRectangle(rectangle);
			}else{
				//Change occouring
				Vector2 currentMouse= new Vector2();
				owner.camToPanel(x, y, currentMouse);	
				float dx = currentMouse.x-lastMouse.x;
				float dy = currentMouse.y-lastMouse.y;
				lastMouse.set(currentMouse);
				if(mouseDragX == LocationTest.INSIDE && mouseDragY == LocationTest.INSIDE){
					//Drag inside Rectangle
					rectangle.x +=dx;
					rectangle.y +=dy;
				}else if(mouseDragX == LocationTest.INSIDE && mouseDragY == LocationTest.INSIDE){
					//Top Left
				}else if(mouseDragX == LocationTest.INSIDE && mouseDragY == LocationTest.INSIDE){
					//Top Middle
				}else if(mouseDragX == LocationTest.INSIDE && mouseDragY == LocationTest.INSIDE){
					//Top Right
				}else if(mouseDragX == LocationTest.INSIDE && mouseDragY == LocationTest.INSIDE){
					//Center Left
				}else if(mouseDragX == LocationTest.INSIDE && mouseDragY == LocationTest.INSIDE){
					//Center Right
				}else if(mouseDragX == LocationTest.INSIDE && mouseDragY == LocationTest.INSIDE){
					//Bottom Left
				}else if(mouseDragX == LocationTest.INSIDE && mouseDragY == LocationTest.INSIDE){
					//Bottom Middle
				}else if(mouseDragX == LocationTest.INSIDE && mouseDragY == LocationTest.INSIDE){
					//Bottom Right
				}
		
			}
		}
		return super.touchDragged(x, x, pointer);
	}
	
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		synchronized (rectangle) {
			owner.camToPanel(x, y, lastMouse);	
			clearMouseState();
			
			if(newRectangle){
				newRectangle = false;
				rectangle.x = firstMouse.x;
				rectangle.y = firstMouse.y;
				rectangle.width = lastMouse.x - firstMouse.x;
				rectangle.height = lastMouse.y - firstMouse.y;
				MathsToolkit.validateRectangle(rectangle);
			}
		}
		return super.touchUp(x, y, pointer, button);
	}

	public void functionToHoldeSelector(){
		if(rectMouseStateX == LocationTest.OUTSIDE || rectMouseStateY == LocationTest.OUTSIDE){
			//Missed Rectangle
		}else if(rectMouseStateX == LocationTest.INSIDE && rectMouseStateY == LocationTest.INSIDE){
			//Drag inside mouse
		}else if(rectMouseStateX == LocationTest.CLOSE_START && rectMouseStateY == LocationTest.CLOSE_START){
			//Top Left
		}else if(rectMouseStateX == LocationTest.INSIDE&& rectMouseStateY == LocationTest.CLOSE_START){
			//Top Middle
		}else if(rectMouseStateX == LocationTest.INSIDE && rectMouseStateY == LocationTest.CLOSE_END){
			//Top Right
		}else if(rectMouseStateX == LocationTest.CLOSE_START&& rectMouseStateY == LocationTest.INSIDE){
			//Center Left
		}else if(rectMouseStateX == LocationTest.CLOSE_END&& rectMouseStateY == LocationTest.INSIDE){
			//Center Right
		}else if(rectMouseStateX == LocationTest.CLOSE_START&& rectMouseStateY == LocationTest.CLOSE_END){
			//Bottom Left
		}else if(rectMouseStateX == LocationTest.INSIDE && rectMouseStateY == LocationTest.CLOSE_END){
			//Bottom Middle
		}else if(rectMouseStateX == LocationTest.CLOSE_END && rectMouseStateY == LocationTest.CLOSE_END){
			//Bottom Right
		}
	}
	public void draw(ShapeRenderer shape, boolean edit){
		synchronized (rectangle) {
			shape.begin(ShapeType.Rectangle);
			
			float mouseWidth = getMouseDistance();
			
			Color rectColor = Color.BLUE;
			Color handColor = Color.LIGHT_GRAY;
			
			shape.setColor(handColor);
			Rectangle hold = new Rectangle();
			
			//Bottom
			getBL(hold, mouseWidth);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			getBM(hold, mouseWidth);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			getBR(hold, mouseWidth);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			//Center
			getCL(hold, mouseWidth);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			getCR(hold, mouseWidth);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			//TOP
			getTL(hold, mouseWidth);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			getTM(hold, mouseWidth);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			getTR(hold, mouseWidth);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			//Draw Shape
			shape.setColor(rectColor);
			shape.rect(rectangle.x, rectangle.y, rectangle.width,rectangle.height);
			
			shape.end();
		}
	}
	public void getTR(Rectangle hold, float mouseWidth){
		hold.set(rectangle.x+rectangle.width, rectangle.y+rectangle.height, mouseWidth+borderGap,mouseWidth+borderGap);
	}
	public void getTM(Rectangle hold, float mouseWidth){
		hold.set(rectangle.x+borderGap, rectangle.y+rectangle.height, rectangle.width-2*borderGap,mouseWidth);
	}
	public void getTL(Rectangle hold, float mouseWidth){
		hold.set(rectangle.x-mouseWidth, rectangle.y+rectangle.height, mouseWidth,mouseWidth);
	}
	public void getCR(Rectangle hold, float mouseWidth){
		hold.set(rectangle.x+rectangle.width, rectangle.y+borderGap, mouseWidth,rectangle.height-2*borderGap);
	}
	public void getCL(Rectangle hold, float mouseWidth){
		hold.set(rectangle.x-mouseWidth, rectangle.y+borderGap, mouseWidth,rectangle.height-2*borderGap);
	}
	public void getBR(Rectangle hold, float mouseWidth){
		hold.set(rectangle.x+rectangle.width, rectangle.y-mouseWidth, mouseWidth,mouseWidth);
	}
	public void getBM(Rectangle hold, float mouseWidth){
		hold.set(rectangle.x+borderGap, rectangle.y-mouseWidth, rectangle.width-2*borderGap,mouseWidth);
	}
	public void getBL(Rectangle hold, float mouseWidth){
		hold.set(rectangle.x-mouseWidth-2*borderGap, rectangle.y-mouseWidth-2*borderGap, mouseWidth+2*borderGap,mouseWidth+2*borderGap);
	}
	
	
	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
}

class LocationTest{
	public static int OUTSIDE      = 0x00000000;
	public static int INSIDE       = 0x00000001;
	public static int CLOSE_START   = 0x00000100;
	public static int CLOSE_END   = 0x00000100;
		
	public static int testPosition(float pos, float dist, float start, float end){	
		int result = OUTSIDE;
		if(pos>=start&&pos<=end){
			result = result|INSIDE;
		}else if(pos>=start-dist&&pos<start){
			result = result|CLOSE_START;
		}else if(pos>end&&pos<=end+dist){
			result = result|CLOSE_END;
		}
		return result;
	}
	
}
