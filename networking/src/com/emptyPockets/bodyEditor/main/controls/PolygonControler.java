package com.emptyPockets.bodyEditor.main.controls;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.bodyEditor.entity.PolygonEntity;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptyPockets.utils.maths.MathsToolkit;

public class PolygonControler extends EntityControler{
	EntityEditorScreen owner;
	float minPixelDistance = ScreenSizeHelper.getcmtoPxlX(.25f);

	Vector2 lastMouse = new Vector2();
	Vector2 firstDown = new Vector2();
	
	ArrayList<Vector2> polygonData = new ArrayList<Vector2>();
	
	int mousePointSelectedIndex = -1;
	int mouseLineSelectedIndex = -1;

	ArrayList<Boolean> pointSelectionData = new ArrayList<Boolean>();
	int pointSelectedCount = 0;
	
	ArrayList<Boolean> lineSelectionData = new ArrayList<Boolean>();
	int lineSelectedCount = 0;
	
	Rectangle selectedRegion = new Rectangle();
	boolean activeSelectionRegion = false;
	
	
	
	public PolygonControler(EntityEditorScreen owner){
		this.owner = owner;
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		owner.camToPanel(x, y, lastMouse);
		updateMouseSelection();
		//Check if there is a selection - if so remove it
		if(lineSelectedCount > 0 || pointSelectedCount > 0){
			clearGroupSelection();
			updateGroupSelection();
		}else if(count == 1){
			//Ignore if mouse on point
			if(mousePointSelectedIndex == -1){
				Vector2 pos = lastMouse.cpy();
				if(mouseLineSelectedIndex != -1){
					polygonData.add(mouseLineSelectedIndex+1, pos);
					lineSelectionData.add(mouseLineSelectedIndex+1, false);
					pointSelectionData.add(mouseLineSelectedIndex+1, false);
				}else{
					polygonData.add(pos);
					lineSelectionData.add(false);
					pointSelectionData.add(false);
				}
			}
		}else{ //Double Tap Remove
			if(pointSelectedCount == 0){
				//Deal with a case where 
				polygonData.remove(mousePointSelectedIndex);
				lineSelectionData.remove(mousePointSelectedIndex);
				pointSelectionData.remove(mousePointSelectedIndex);
				clearGroupSelection();
				return true;
			}
		}
		clearGroupSelection();
		return false;
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		Vector2 currentMouse = new Vector2();
		owner.camToPanel(x, y, currentMouse);
		int editCount = 0;
		if(activeSelectionRegion == true){
			updateSelectedRegion();
			updateGroupSelection();
			editCount++;
		}else{
			float dx = currentMouse.x-lastMouse.x;
			float dy = currentMouse.y-lastMouse.y;
			translateSelection(dx, dy);
		}
		
		lastMouse.set(currentMouse);
		
		if(editCount > 0){
			return true;
		}
		return super.touchDragged(x, y, pointer);
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		owner.camToPanel(x, y, lastMouse);
		activeSelectionRegion = false;
		return super.touchUp(x, y, pointer, button);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		owner.camToPanel(x, y, lastMouse);	
		firstDown.set(lastMouse);
		updateMouseSelection();
		
		//When no points nearby drag around points
		if(mouseLineSelectedIndex == -1 && mousePointSelectedIndex == -1){
			activeSelectionRegion = true;
			updateSelectedRegion();
		}else{
			//Deals with dragging so that only on selected points
			boolean clearSelection = false;
			if(mouseLineSelectedIndex != -1){
				if(!isGroupLineSelected(mouseLineSelectedIndex)){
					clearSelection= true;
				}
			}
			
			if(mousePointSelectedIndex != -1){
				if(!isGroupPointSelected(mousePointSelectedIndex)){
					clearSelection = true;
				}
			}
			
			if(clearSelection){
				
				clearGroupSelection();
			}
		}
		return false;
	}
	
	@Override
	public boolean mouseMoved(int x, int y) {
		owner.camToPanel(x, y, lastMouse);
		updateMouseSelection();
		return super.mouseMoved(x, y);
	}
	
	public int translateSelection(float dx, float dy){
		int editCount = 0;
		for(int i = 0; i < polygonData.size(); i++){
			if(isPointSelected(i)){
				polygonData.get(i).add(dx, dy);
				editCount++;
			}
		}
		return editCount;
	}
	
	public boolean isPointSelected(int index){
		if(isMousePointSelected(index) || isGroupPointSelected(index)){
			return true;
		}
		return false;
	}
	
	public boolean isMousePointSelected(int index){
		if(index == mousePointSelectedIndex){
			return true;
		}
		int lineA = index;
		int lineB = index-1;
		if(lineB < 0){
			lineB = polygonData.size()-1;
		}
		if(lineA == mouseLineSelectedIndex || lineB == mouseLineSelectedIndex){
			return true;
		}
		return false;
	}
	
	public boolean isGroupPointSelected(int index){
		if(pointSelectionData.get(index)){
			return true;
		}
		int lineA = index;
		int lineB = index-1;
		if(lineB < 0){
			lineB = polygonData.size()-1;
		}
		if(lineSelectionData.get(lineA) || lineSelectionData.get(lineB)){
			return true;
		}
		return false;
	}
	
	public boolean isLineSelected(int index){
		return isMouseLineSelected(index) || isGroupLineSelected(index);
	}
	
	public boolean isMouseLineSelected(int index){
		return mouseLineSelectedIndex == index;
	}
	
	public boolean isGroupLineSelected(int index){
		return lineSelectionData.get(index);
	}
	
	public void updateSelectedRegion(){
		selectedRegion.x=firstDown.x;
		selectedRegion.y=firstDown.y;
		selectedRegion.width  =lastMouse.x-firstDown.x;
		selectedRegion.height =lastMouse.y-firstDown.y;
		
		if(selectedRegion.width < 0){
			selectedRegion.x+=selectedRegion.width;
			selectedRegion.width*=-1;
		}
		if(selectedRegion.height < 0){
			selectedRegion.y+=selectedRegion.height;
			selectedRegion.height*=-1;
		}
	}

	private void updateMouseSelection(){
		clearMouseSelection();
		float mouseDistance = owner.panelToCam(minPixelDistance);
		float mouseDistance2 = mouseDistance*mouseDistance;
		
		boolean pointFound = false;
		for(int i = 0; i < polygonData.size(); i++){
			Vector2 p = polygonData.get(i);
			if(p.dst2(lastMouse) < mouseDistance2){
				mousePointSelectedIndex = i;
				pointFound = true;
			}
		}
		
		if(!pointFound){
			for(int i = 0; i < polygonData.size() && polygonData.size() >= 2; i++){
				Vector2 p1 = null;
				Vector2 p2 = null;
				if(i == polygonData.size()-1){
					p1 = polygonData.get(i);
					p2 = polygonData.get(0);
				}else{
					p1 = polygonData.get(i);
					p2 = polygonData.get(i+1);
				}
				if(MathsToolkit.getLineSegmentDistance(p1, p2, lastMouse) < mouseDistance){
					mouseLineSelectedIndex = i;
				}
			}
			
		}
	}
	
	private void clearMouseSelection(){
		mousePointSelectedIndex = -1;
		mouseLineSelectedIndex = -1;
	}
	
	private void clearGroupSelection(){
		pointSelectedCount = 0;
		lineSelectedCount = 0;
		for(int i =0; i < polygonData.size(); i++){
			pointSelectionData.set(i, false);
			lineSelectionData.set(i, false);
		}
	}
	
	private void updateGroupSelection(){
		clearGroupSelection();
		
		//Test rectangle
		if(activeSelectionRegion){
			//Test Points
			for(int i =0; i < polygonData.size(); i++){
				Vector2 v = polygonData.get(i);
				if(selectedRegion.contains(v.x, v.y)){
					pointSelectionData.set(i, true);
				}
			}		
			
			for(int i = 0; i < polygonData.size(); i++){
				int p1 = i;
				int p2 = i+1;
				
				if(p2 > polygonData.size()-1){
					p2 = 0;
				}
				
				if(MathsToolkit.SegmentIntersectRectangle(selectedRegion, polygonData.get(p1), polygonData.get(p2))){
					lineSelectionData.set(i, true);
				}
			}
			
		}
		//Determine Selected Points
		for(int i =0; i < polygonData.size(); i++){
			if(pointSelectionData.get(i)){
				pointSelectedCount++;
			}
		}
		
		//Determine Selected Lines
		for(int i =0; i < polygonData.size(); i++){
			if(lineSelectionData.get(i)){
				lineSelectedCount++;
			}
		}
	}
	
	public void drawPolygon(ShapeRenderer shape, ArrayList<Vector2> data, boolean edit){
		if(activeSelectionRegion){
			Color c = Color.LIGHT_GRAY;
			c.a = 0.5f;
			shape.begin(ShapeType.Rectangle);
			shape.setColor(c);
			shape.rect(selectedRegion.x, selectedRegion.y, selectedRegion.width, selectedRegion.height);
			shape.end();
			
			c.a = 0.1f;
			shape.begin(ShapeType.FilledRectangle);
			shape.setColor(c);
			shape.filledRect(selectedRegion.x, selectedRegion.y, selectedRegion.width, selectedRegion.height);
			shape.end();
		}
		
		if(data == null || data.size() == 0){
			return;
		}
		//Draw Lines
		Vector2 p1 = null;
		Vector2 p2 = null;
		Gdx.gl.glLineWidth(3f);
		
		shape.begin(ShapeType.Line);
		
		Color line = Color.BLUE;
		Color lineSelected = Color.CYAN;
		
		for(int i = 0; i <  data.size() && data.size() > 1; i++){
			if(isLineSelected(i) && edit){
				shape.setColor(lineSelected);		
			}else{
				shape.setColor(line);
			}
			if(i < data.size()-1){
				p1 =  data.get(i);
				p2 =  data.get(i+1);	
			}else{
				p1 =  data.get( data.size()-1);
				p2 =  data.get(0);
			}
			shape.line(p1.x, p1.y, p2.x, p2.y);
		}
		shape.end();
		
		if(edit){
			Color node = Color.RED;
			Color nodeSelected = Color.GREEN;
			node.a = 0.6f;
			nodeSelected.a = 0.6f;
			
			Gdx.gl.glLineWidth(1f);
			//Draw Nodes
			float nodeSize = owner.panelToCam(minPixelDistance);
			shape.begin(ShapeType.Rectangle);
			for(int i = 0; i <  data.size()-1; i++){
				Vector2 p = data.get(i);
				if(isPointSelected(i)){
					shape.setColor(node);
				}else{
					shape.setColor(nodeSelected);
				}
				shape.rect(p.x-nodeSize, p.y-nodeSize, 2*nodeSize, 2*nodeSize);
			}
			shape.end();
			
			//Draw last node as a circle
			shape.begin(ShapeType.Circle);
			if(isPointSelected(data.size()-1)){
				shape.setColor(node);
			}else{
				shape.setColor(nodeSelected);
			}
			shape.circle(data.get(data.size()-1).x, data.get(data.size()-1).y, nodeSize);
			shape.end();
		}
	}
	
	public void setPolygon(ArrayList<Vector2> points){
		this.polygonData = points;
		this.pointSelectionData.ensureCapacity(points.size());
		this.lineSelectionData.ensureCapacity(points.size());
	}
	
	public ArrayList<Vector2> getPolygon(){
		return polygonData;
	}
}
