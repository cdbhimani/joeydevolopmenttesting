package com.emptyPockets.box2d.shape.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.box2d.shape.data.RectangleShapeData;
import com.emptyPockets.gui.ViewportConvertor;
import com.emptyPockets.utils.maths.MathsToolkit;

public class RectangleControler extends BaseShapeControler{
		
	boolean newRectangle = false;
	RectangleShapeData rectangleShape;
	
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
	
	
	public RectangleControler(ViewportConvertor owner) {
		super(owner);
	}
	
	private void updateMouseState(){
		clearMouseState();
		float mouseDistance= owner.panelToCam(this.minContactDistance);
		float regionGap = owner.panelToCam(this.borderGap);
		
		Rectangle rect = new Rectangle();
		rect.set(rectangleShape.getRectangle());
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
		if(rectangleShape == null || state == ControlState.DISABLED){
			return false;
		}
		return super.tap(x, y, count, button);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if(rectangleShape == null || state == ControlState.DISABLED){
			return false;
		}
		boolean returnValue = false;
		
		synchronized (rectangleShape.getRectangle()) {
			owner.camToPanel(x, y, firstMouse);	
			lastMouse.set(firstMouse);
			if(newRectangle){
				rectangleShape.getRectangle().x = firstMouse.x;
				rectangleShape.getRectangle().y = firstMouse.y;
				rectangleShape.getRectangle().width  = 1;
				rectangleShape.getRectangle().height = 1;
				MathsToolkit.validateRectangle(rectangleShape.getRectangle());
				returnValue=true;
			}
			
			updateMouseState();
			if(mouseCenterDrag ||((mouseBLDrag||mouseBMDrag||mouseBRDrag||mouseTLDrag||mouseTMDrag||mouseTRDrag||mouseCLDrag||mouseCRDrag)&&state==ControlState.EDIT)){
				returnValue = true;
			}
		}
		
		return returnValue||super.touchDown(x, y, pointer, button);
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if(rectangleShape== null || state == ControlState.DISABLED){
			return false;
		}
		boolean returnValue = false;
		
		synchronized (rectangleShape.getRectangle()) {
			//Building new Rectangle
			if(newRectangle){
				owner.camToPanel(x, y, lastMouse);	
				rectangleShape.getRectangle().x = firstMouse.x;
				rectangleShape.getRectangle().y = firstMouse.y;
				rectangleShape.getRectangle().width = lastMouse.x - firstMouse.x;
				rectangleShape.getRectangle().height = lastMouse.y - firstMouse.y;
				MathsToolkit.validateRectangle(rectangleShape.getRectangle());
				returnValue = true;
			}else{
				//Change occouring
				Vector2 currentMouse= new Vector2();
				owner.camToPanel(x, y, currentMouse);	
				float dx = currentMouse.x-lastMouse.x;
				float dy = currentMouse.y-lastMouse.y;
				lastMouse.set(currentMouse);
				
				if(mouseCenterDrag ||((mouseBLDrag||mouseBMDrag||mouseBRDrag||mouseTLDrag||mouseTMDrag||mouseTRDrag||mouseCLDrag||mouseCRDrag)&&state==ControlState.EDIT)){
					returnValue = true;
				}
				
				if(mouseCenterDrag){
					//Drag inside Rectangle
					rectangleShape.getRectangle().x +=dx;
					rectangleShape.getRectangle().y +=dy;
				}else if(state==ControlState.EDIT){
					if(mouseTRDrag){
						rectangleShape.getRectangle().width+=dx;
						rectangleShape.getRectangle().height+=dy;
					}else if(mouseTMDrag){
						rectangleShape.getRectangle().height+=dy;
					}else if(mouseTLDrag){
						rectangleShape.getRectangle().x+=dx;
						rectangleShape.getRectangle().width-=dx;
						rectangleShape.getRectangle().height+=dy;
					}else if(mouseCLDrag){
						rectangleShape.getRectangle().x+=dx;
						rectangleShape.getRectangle().width-=dx;
					}else if(mouseCRDrag){
						rectangleShape.getRectangle().width+=dx;
					}else if(mouseBLDrag){
						rectangleShape.getRectangle().x+=dx;
						rectangleShape.getRectangle().width-=dx;
						rectangleShape.getRectangle().y+=dy;
						rectangleShape.getRectangle().height-=dy;
					}else if(mouseBMDrag){
						rectangleShape.getRectangle().y+=dy;
						rectangleShape.getRectangle().height-=dy;
					}else if(mouseBRDrag){
						rectangleShape.getRectangle().width+=dx;
						rectangleShape.getRectangle().y+=dy;
						rectangleShape.getRectangle().height-=dy;
					}
				}
				if(rectangleShape.getRectangle().width < 0){
					//Allows moving if size below 0
					if(mouseTRDrag || mouseBRDrag || mouseCRDrag){
						rectangleShape.getRectangle().x+=rectangleShape.getRectangle().width;
					}
					rectangleShape.getRectangle().width = 0;
				}
				if(rectangleShape.getRectangle().height < 0){
					if(mouseTMDrag || mouseTLDrag || mouseTRDrag){
						rectangleShape.getRectangle().y+=rectangleShape.getRectangle().height;
					}
					rectangleShape.getRectangle().height= 0;
				}
			}
		}
		return returnValue||super.touchDragged(x, x, pointer);
	}
	
	@Override
	public boolean mouseMoved(int x, int y) {
		if(rectangleShape == null || state == ControlState.DISABLED){
			return false;
		}
		boolean returnValue = false;
		
		synchronized (rectangleShape.getRectangle()) {
			owner.camToPanel(x, y, lastMouse);	
			updateMouseState();
		}
		return returnValue||super.mouseMoved(x, y);
	}
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(rectangleShape== null || state == ControlState.DISABLED){
			return false;
		}
		boolean returnValue = false;
		synchronized (rectangleShape.getRectangle()) {
			owner.camToPanel(x, y, lastMouse);	
			clearMouseState();
			
			if(newRectangle){
				newRectangle = false;
				rectangleShape.getRectangle().x = firstMouse.x;
				rectangleShape.getRectangle().y = firstMouse.y;
				rectangleShape.getRectangle().width = lastMouse.x - firstMouse.x;
				rectangleShape.getRectangle().height = lastMouse.y - firstMouse.y;
				MathsToolkit.validateRectangle(rectangleShape.getRectangle());
				returnValue=true;
			}
		}
		return returnValue||super.touchUp(x, y, pointer, button);
	}
	
	public void draw(ShapeRenderer shape){
		if(rectangleShape == null){
			return;
		}
		synchronized (rectangleShape.getRectangle()) {
			shape.begin(ShapeType.Rectangle);

			if(state==ControlState.EDIT){
				float mouseWidth = owner.panelToCam(this.minContactDistance);
				float regionGap = owner.panelToCam(this.borderGap);
				
				Rectangle hold = new Rectangle();
				
				//Bottom
				getBL(hold, mouseWidth, regionGap);
				shape.setColor(mouseBLDrag?controlHighlightColor:controlColor);
				if(hold.width > 0 && hold.height > 0)
					shape.rect(hold.x, hold.y, hold.width, hold.height);
				
				getBM(hold, mouseWidth, regionGap);
				shape.setColor(mouseBMDrag?controlHighlightColor:controlColor);
				if(hold.width > 0 && hold.height > 0)
					shape.rect(hold.x, hold.y, hold.width, hold.height);
				
				getBR(hold, mouseWidth, regionGap);
				shape.setColor(mouseBRDrag?controlHighlightColor:controlColor);
				if(hold.width > 0 && hold.height > 0)
					shape.rect(hold.x, hold.y, hold.width, hold.height);
				
				//Center
				getCL(hold, mouseWidth, regionGap);
				shape.setColor(mouseCLDrag?controlHighlightColor:controlColor);
				if(hold.width > 0 && hold.height > 0)
					shape.rect(hold.x, hold.y, hold.width, hold.height);
				
				getCR(hold, mouseWidth, regionGap);
				shape.setColor(mouseCRDrag?controlHighlightColor:controlColor);
				if(hold.width > 0 && hold.height > 0)
					shape.rect(hold.x, hold.y, hold.width, hold.height);
				
				//TOP
				getTL(hold, mouseWidth, regionGap);
				shape.setColor(mouseTLDrag?controlHighlightColor:controlColor);
				if(hold.width > 0 && hold.height > 0)
					shape.rect(hold.x, hold.y, hold.width, hold.height);
				
				getTM(hold, mouseWidth, regionGap);
				shape.setColor(mouseTMDrag?controlHighlightColor:controlColor);
				if(hold.width > 0 && hold.height > 0)
					shape.rect(hold.x, hold.y, hold.width, hold.height);
				
				getTR(hold, mouseWidth, regionGap);
				shape.setColor(mouseTRDrag?controlHighlightColor:controlColor);
				if(hold.width > 0 && hold.height > 0)
					shape.rect(hold.x, hold.y, hold.width, hold.height);
			}
			//Draw Shape
			Gdx.gl.glLineWidth(2f);
			shape.setColor(shapeColor);
			shape.setColor(mouseCenterDrag?shapeHighlightColor:shapeColor);
			shape.rect(rectangleShape.getRectangle().x, rectangleShape.getRectangle().y, rectangleShape.getRectangle().width,rectangleShape.getRectangle().height);
			Gdx.gl.glLineWidth(1f);
			shape.end();
		}
	}
	public void getTR(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangleShape.getRectangle().x+rectangleShape.getRectangle().width, rectangleShape.getRectangle().y+rectangleShape.getRectangle().height, mouseWidth+2*borderGap,mouseWidth+2*borderGap);
	}
	public void getTM(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangleShape.getRectangle().x+borderGap, rectangleShape.getRectangle().y+rectangleShape.getRectangle().height, rectangleShape.getRectangle().width-2*borderGap,mouseWidth);
	}
	public void getTL(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangleShape.getRectangle().x-mouseWidth-2*borderGap, rectangleShape.getRectangle().y+rectangleShape.getRectangle().height, mouseWidth+2*borderGap,mouseWidth+2*borderGap);
	}
	public void getCR(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangleShape.getRectangle().x+rectangleShape.getRectangle().width, rectangleShape.getRectangle().y+borderGap, mouseWidth,rectangleShape.getRectangle().height-2*borderGap);
	}
	public void getCL(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangleShape.getRectangle().x-mouseWidth, rectangleShape.getRectangle().y+borderGap, mouseWidth,rectangleShape.getRectangle().height-2*borderGap);
	}
	public void getBR(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangleShape.getRectangle().x+rectangleShape.getRectangle().width, rectangleShape.getRectangle().y-mouseWidth-2*borderGap, mouseWidth+2*borderGap,mouseWidth+2*borderGap);
	}
	public void getBM(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangleShape.getRectangle().x+borderGap, rectangleShape.getRectangle().y-mouseWidth, rectangleShape.getRectangle().width-2*borderGap,mouseWidth);
	}
	public void getBL(Rectangle hold, float mouseWidth, float borderGap){
		hold.set(rectangleShape.getRectangle().x-mouseWidth-2*borderGap, rectangleShape.getRectangle().y-mouseWidth-2*borderGap, mouseWidth+2*borderGap,mouseWidth+2*borderGap);
	}
	
	public Rectangle getRectangle() {
		return rectangleShape.getRectangle();
	}

	public void setRectangle(RectangleShapeData rectangle) {
		this.rectangleShape = rectangle;
	}
}