package com.emptyPockets.bodyEditor.main.controls;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptyPockets.utils.maths.MathsToolkit;

public class PolygonControler extends BaseEntityControler{
	

	Vector2 lastMouse = new Vector2();
	Vector2 firstDown = new Vector2();
	
	ArrayList<Vector2> polygonPointData = new ArrayList<Vector2>();
	
	int mousePointSelectedIndex = -1;
	int mouseLineSelectedIndex = -1;

	ArrayList<Boolean> pointSelectionData = new ArrayList<Boolean>();
	int pointSelectedCount = 0;
	
	ArrayList<Boolean> lineSelectionData = new ArrayList<Boolean>();
	int lineSelectedCount = 0;
	
	Rectangle selectedRegion = new Rectangle();
	boolean activeSelectionRegion = false;
	
	
	
	public PolygonControler(EntityEditorScreen owner){
		super(owner);
	}
	
	private Vector2 getPoint(int index){
		return polygonPointData.get(index);
		
	}
	
	private void removePoint(int index){
		polygonPointData.remove(index);
		lineSelectionData.remove(index);
		pointSelectionData.remove(index);
	}
	private void addPoint(int index, Vector2 pos){
		polygonPointData.add(index, pos);
		lineSelectionData.add(index, false);
		pointSelectionData.add(index, false);
	}
	
	private void addPoint(Vector2 pos){
		polygonPointData.add(pos);
		lineSelectionData.add(false);
		pointSelectionData.add(false);
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		boolean returnValue = false;
		synchronized (polygonPointData) {
			owner.camToPanel(x, y, lastMouse);
			updateMouseSelection();

			//Check if there is a selection - if so remove it
			if(lineSelectedCount > 0 || pointSelectedCount > 0){
				clearGroupSelection();
				updateGroupSelection();
				returnValue = true;
			}else if(count == 1){ 
				//Add point if mouse not over another point
				if(mousePointSelectedIndex == -1){
					Vector2 pos = lastMouse.cpy();
					if(mouseLineSelectedIndex != -1){
						addPoint(mouseLineSelectedIndex+1, pos);
					}else{
						addPoint(pos);
					}
					returnValue = true;
				}
				
			}else if(count == 2){ 
				//Remove if over existing point
				if(mousePointSelectedIndex != -1){
					//Deal with a case where 
					removePoint(mousePointSelectedIndex);
					returnValue = true;
				}
			}
		}
		clearMouseSelection();
		
		return returnValue;
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		synchronized (polygonPointData) {
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
		}
		return super.touchDragged(x, y, pointer);
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		synchronized (polygonPointData) {
			owner.camToPanel(x, y, lastMouse);
			activeSelectionRegion = false;
			clearMouseSelection();
		}
		return super.touchUp(x, y, pointer, button);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		synchronized (polygonPointData) {
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
		}
		return false;
	}
	
	@Override
	public boolean mouseMoved(int x, int y) {
		synchronized (polygonPointData) {
			owner.camToPanel(x, y, lastMouse);
			updateMouseSelection();
		}
		return super.mouseMoved(x, y);
	}
	
	private int translateSelection(float dx, float dy){
		int editCount = 0;
		for(int i = 0; i < polygonPointData.size(); i++){
			if(isPointSelected(i)){
				getPoint(i).add(dx, dy);
				editCount++;
			}
		}
		return editCount;
	}
	
	private boolean isPointSelected(int index){
		if(isMousePointSelected(index) || isGroupPointSelected(index)){
			return true;
		}
		return false;
	}
	
	private boolean isMousePointSelected(int index){
		if(index == mousePointSelectedIndex){
			return true;
		}
		int lineA = index;
		int lineB = index-1;
		if(lineB < 0){
			lineB = polygonPointData.size()-1;
		}
		if(lineA == mouseLineSelectedIndex || lineB == mouseLineSelectedIndex){
			return true;
		}
		return false;
	}
	
	private boolean isGroupPointSelected(int index){
		if(pointSelectionData.get(index)){
			return true;
		}
		int lineA = index;
		int lineB = index-1;
		if(lineB < 0){
			lineB = polygonPointData.size()-1;
		}
		if(lineSelectionData.get(lineA) || lineSelectionData.get(lineB)){
			return true;
		}
		return false;
	}
	
	private boolean isLineSelected(int index){
		return isMouseLineSelected(index) || isGroupLineSelected(index);
	}
	
	private boolean isMouseLineSelected(int index){
		return mouseLineSelectedIndex == index;
	}
	
	private boolean isGroupLineSelected(int index){
		return lineSelectionData.get(index);
	}
	
	private void updateSelectedRegion(){
		selectedRegion.x=firstDown.x;
		selectedRegion.y=firstDown.y;
		selectedRegion.width  =lastMouse.x-firstDown.x;
		selectedRegion.height =lastMouse.y-firstDown.y;
		
		MathsToolkit.validateRectangle(selectedRegion);
	}

	private void updateMouseSelection(){
		clearMouseSelection();
		float mouseDistance = owner.panelToCam(minContactDistance)/2;
		float mouseDistance2 = mouseDistance*mouseDistance;
		
		boolean pointFound = false;
		for(int i = 0; i < polygonPointData.size(); i++){
			Vector2 p = polygonPointData.get(i);
			if(p.dst2(lastMouse) < mouseDistance2){
				mousePointSelectedIndex = i;
				pointFound = true;
			}
		}
		
		if(!pointFound){
			for(int i = 0; i < polygonPointData.size() && polygonPointData.size() >= 2; i++){
				Vector2 p1 = null;
				Vector2 p2 = null;
				if(i == polygonPointData.size()-1){
					p1 = polygonPointData.get(i);
					p2 = polygonPointData.get(0);
				}else{
					p1 = polygonPointData.get(i);
					p2 = polygonPointData.get(i+1);
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
		for(int i =0; i < polygonPointData.size(); i++){
			pointSelectionData.set(i, false);
			lineSelectionData.set(i, false);
		}
	}
	
	private void updateGroupSelection(){
		clearGroupSelection();
		
		//Test rectangle
		if(activeSelectionRegion){
			//Test Points
			for(int i =0; i < polygonPointData.size(); i++){
				Vector2 v = polygonPointData.get(i);
				if(selectedRegion.contains(v.x, v.y)){
					pointSelectionData.set(i, true);
				}
			}		
			
			for(int i = 0; i < polygonPointData.size(); i++){
				int p1 = i;
				int p2 = i+1;
				
				if(p2 > polygonPointData.size()-1){
					p2 = 0;
				}
				
				if(MathsToolkit.SegmentIntersectRectangle(selectedRegion, polygonPointData.get(p1), polygonPointData.get(p2))){
					lineSelectionData.set(i, true);
				}
			}
			
		}
		//Determine Selected Points
		for(int i =0; i < polygonPointData.size(); i++){
			if(pointSelectionData.get(i)){
				pointSelectedCount++;
			}
		}
		
		//Determine Selected Lines
		for(int i =0; i < polygonPointData.size(); i++){
			if(lineSelectionData.get(i)){
				lineSelectedCount++;
			}
		}
	}
	
	public void draw(ShapeRenderer shape, boolean edit){
		
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
		
		if(polygonPointData == null || polygonPointData.size() == 0){
			return;
		}
		
		synchronized (polygonPointData) {
			//Draw Lines
			Vector2 p1 = null;
			Vector2 p2 = null;
			Gdx.gl.glLineWidth(3f);
			
			shape.begin(ShapeType.Line);
			
			Color line = Color.BLUE;
			Color lineSelected = Color.CYAN;
			
			for(int i = 0; i <  polygonPointData.size() && polygonPointData.size() > 1; i++){
				if(isLineSelected(i) && edit){
					shape.setColor(lineSelected);		
				}else{
					shape.setColor(line);
				}
				if(i < polygonPointData.size()-1){
					p1 =  polygonPointData.get(i);
					p2 =  polygonPointData.get(i+1);	
				}else{
					p1 =  polygonPointData.get( polygonPointData.size()-1);
					p2 =  polygonPointData.get(0);
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
				float nodeSize = owner.panelToCam(minContactDistance)/2;
				shape.begin(ShapeType.Rectangle);
				for(int i = 0; i <  polygonPointData.size()-1; i++){
					Vector2 p = polygonPointData.get(i);
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
				if(isPointSelected(polygonPointData.size()-1)){
					shape.setColor(node);
				}else{
					shape.setColor(nodeSelected);
				}
				shape.circle(polygonPointData.get(polygonPointData.size()-1).x, polygonPointData.get(polygonPointData.size()-1).y, nodeSize);
				shape.end();
			}
		}
	}
	
	public void setPolygon(ArrayList<Vector2> points){
		this.polygonPointData = points;
		this.pointSelectionData.ensureCapacity(points.size());
		this.lineSelectionData.ensureCapacity(points.size());
	}
	
	public ArrayList<Vector2> getPolygon(){
		return polygonPointData;
	}
}
