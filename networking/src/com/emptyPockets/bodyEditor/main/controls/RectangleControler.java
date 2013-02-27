package com.emptyPockets.bodyEditor.main.controls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;
import com.emptyPockets.utils.maths.MathsToolkit;

public class RectangleControler extends BaseEntityControler{
		
	boolean newRectangle = false;
	Rectangle rectangle;
	
	float borderGap = 3; 
	
	Vector2 firstMouse = new Vector2();
	Vector2 lastMouse = new Vector2();
	

	
	boolean mouseCenterDrag = false;
	boolean mouseTLDrag = false;
	boolean mouseTMDrag = false;
	boolean mouseTRDrag = false;
	boolean mouseCLDrag = false;
	boolean mouseCRDrag = false;
	boolean mouseBLDrag = false;
	boolean mouseBMDrag = false;
	boolean mouseBRDrag = false;
	
	
	public RectangleControler(EntityEditorScreen owner) {
		super(owner);
	}
	
	private void updateMouseState(){
		clearMouseState();
		float mouseDistance= owner.panelToCam(this.minContactDistance);
		float regionGap = owner.panelToCam(this.borderGap);
		
		Rectangle rect = new Rectangle();
		rect.set(rectangle);
		if(rect.contains(lastMouse.x, lastMouse.y)){
			mouseCenterDrag = true;
		}
		
		//Top
		getTL(rect, mouseDistance, regionGap);
		if(rect.contains(lastMouse.x, lastMouse.y)){
			mouseTLDrag = true;
		}
		getTM(rect, mouseDistance, regionGap);
		if(rect.contains(lastMouse.x, lastMouse.y)){
			mouseTMDrag = true;
		}
		getTR(rect, mouseDistance, regionGap);
		if(rect.contains(lastMouse.x, lastMouse.y)){
			mouseTRDrag = true;
		}
		
		//Center
		getCL(rect, mouseDistance, regionGap);
		if(rect.contains(lastMouse.x, lastMouse.y)){
			mouseCLDrag = true;
		}
		getCR(rect, mouseDistance, regionGap);
		if(rect.contains(lastMouse.x, lastMouse.y)){
			mouseCRDrag = true;
		}
		
		//Top
		getBL(rect, mouseDistance, regionGap);
		if(rect.contains(lastMouse.x, lastMouse.y)){
			mouseBLDrag = true;
		}
		getBM(rect, mouseDistance, regionGap);
		if(rect.contains(lastMouse.x, lastMouse.y)){
			mouseBMDrag = true;
		}
		getBR(rect, mouseDistance, regionGap);
		if(rect.contains(lastMouse.x, lastMouse.y)){
			mouseBRDrag = true;
		}
	}
	
	private void clearMouseState(){
		mouseCenterDrag = false;
		mouseTLDrag = false;    
		mouseTMDrag = false;    
		mouseTRDrag = false;    
		mouseCLDrag = false;    
		mouseCRDrag = false;    
		mouseBLDrag = false;    
		mouseBMDrag = false;    
		mouseBRDrag = false;    
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
				if(mouseCenterDrag){
					//Drag inside Rectangle
					rectangle.x +=dx;
					rectangle.y +=dy;
				}else if(mouseTRDrag){
					rectangle.width+=dx;
					rectangle.height+=dy;
				}else if(mouseTMDrag){
					rectangle.height+=dy;
				}else if(mouseTLDrag){
					rectangle.x+=dx;
					rectangle.width-=dx;
					rectangle.height+=dy;
				}else if(mouseCLDrag){
					rectangle.x+=dx;
					rectangle.width-=dx;
				}else if(mouseCRDrag){
					rectangle.width+=dx;
				}else if(mouseBLDrag){
					rectangle.x+=dx;
					rectangle.width-=dx;
					rectangle.y+=dy;
					rectangle.height-=dy;
				}else if(mouseBMDrag){
					rectangle.y+=dy;
					rectangle.height-=dy;
				}else if(mouseBRDrag){
					rectangle.width+=dx;
					rectangle.y+=dy;
					rectangle.height-=dy;
				}
				
				if(rectangle.width < 0){
					//Allows moving if size below 0
					if(mouseTRDrag || mouseBRDrag || mouseCRDrag){
						rectangle.x+=rectangle.width;
					}
					rectangle.width = 0;
				}
				if(rectangle.height < 0){
					if(mouseTMDrag || mouseTLDrag || mouseTRDrag){
						rectangle.y+=rectangle.height;
					}
					rectangle.height= 0;
				}
			}
		}
		return super.touchDragged(x, x, pointer);
	}
	
	@Override
	public boolean mouseMoved(int x, int y) {
		synchronized (rectangle) {
			owner.camToPanel(x, y, lastMouse);	
			updateMouseState();
		}
		return super.mouseMoved(x, y);
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

	public void createRectangle(){
		
	}
	
	public void draw(ShapeRenderer shape, boolean edit){
		synchronized (rectangle) {
			shape.begin(ShapeType.Rectangle);

			float mouseWidth = owner.panelToCam(this.minContactDistance);
			float regionGap = owner.panelToCam(this.borderGap);
			
			Rectangle hold = new Rectangle();
			
			//Bottom
			getBL(hold, mouseWidth, regionGap);
			shape.setColor(mouseBLDrag?highLightColor:controlColor);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			getBM(hold, mouseWidth, regionGap);
			shape.setColor(mouseBMDrag?highLightColor:controlColor);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			getBR(hold, mouseWidth, regionGap);
			shape.setColor(mouseBRDrag?highLightColor:controlColor);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			//Center
			getCL(hold, mouseWidth, regionGap);
			shape.setColor(mouseCLDrag?highLightColor:controlColor);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			getCR(hold, mouseWidth, regionGap);
			shape.setColor(mouseCRDrag?highLightColor:controlColor);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			//TOP
			getTL(hold, mouseWidth, regionGap);
			shape.setColor(mouseTLDrag?highLightColor:controlColor);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			getTM(hold, mouseWidth, regionGap);
			shape.setColor(mouseTMDrag?highLightColor:controlColor);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			getTR(hold, mouseWidth, regionGap);
			shape.setColor(mouseTRDrag?highLightColor:controlColor);
			if(hold.width > 0 && hold.height > 0)
				shape.rect(hold.x, hold.y, hold.width, hold.height);
			
			//Draw Shape
			shape.setColor(shapeColor);
			shape.setColor(mouseCenterDrag?highLightColor:shapeColor);
			shape.rect(rectangle.x, rectangle.y, rectangle.width,rectangle.height);
			
			shape.end();
		}
	}
	public void getTR(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangle.x+rectangle.width, rectangle.y+rectangle.height, mouseWidth+2*borderGap,mouseWidth+2*borderGap);
	}
	public void getTM(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangle.x+borderGap, rectangle.y+rectangle.height, rectangle.width-2*borderGap,mouseWidth);
	}
	public void getTL(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangle.x-mouseWidth-2*borderGap, rectangle.y+rectangle.height, mouseWidth+2*borderGap,mouseWidth+2*borderGap);
	}
	public void getCR(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangle.x+rectangle.width, rectangle.y+borderGap, mouseWidth,rectangle.height-2*borderGap);
	}
	public void getCL(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangle.x-mouseWidth, rectangle.y+borderGap, mouseWidth,rectangle.height-2*borderGap);
	}
	public void getBR(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangle.x+rectangle.width, rectangle.y-mouseWidth-2*borderGap, mouseWidth+2*borderGap,mouseWidth+2*borderGap);
	}
	public void getBM(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangle.x+borderGap, rectangle.y-mouseWidth, rectangle.width-2*borderGap,mouseWidth);
	}
	public void getBL(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangle.x-mouseWidth-2*borderGap, rectangle.y-mouseWidth-2*borderGap, mouseWidth+2*borderGap,mouseWidth+2*borderGap);
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
}