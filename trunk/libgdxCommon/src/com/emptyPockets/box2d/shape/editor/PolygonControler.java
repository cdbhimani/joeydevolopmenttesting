package com.emptyPockets.box2d.shape.editor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.box2d.shape.data.PolygonShapeData;
import com.emptyPockets.gui.ViewportConvertor;
import com.emptyPockets.utils.maths.MathsToolkit;

public class PolygonControler extends BaseShapeControler{
	

	Vector2 lastMouse = new Vector2();
	Vector2 firstDown = new Vector2();
	
	PolygonShapeData polygonData = null;
	
	int mousePointSelectedIndex = -1;
	int mouseLineSelectedIndex = -1;

	ArrayList<Boolean> pointSelectionData = new ArrayList<Boolean>();
	int pointSelectedCount = 0;
	
	ArrayList<Boolean> lineSelectionData = new ArrayList<Boolean>();
	int lineSelectedCount = 0;
	
	Rectangle selectedRegion = new Rectangle();
	boolean activeSelectionRegion = false;
	
	int segments = 50;
	
	public PolygonControler(ViewportConvertor owner){
		super(owner);
	}
	
	private Vector2 getPoint(int index){
		return polygonData.getPoint(index);
		
	}
	
	private void removePoint(int index){
		polygonData.removePoint(index);
		lineSelectionData.remove(index);
		pointSelectionData.remove(index);
	}
	private void addPoint(int index, Vector2 pos){
		polygonData.addPoint(index, pos);
		lineSelectionData.add(index, false);
		pointSelectionData.add(index, false);
	}
	
	private void addPoint(Vector2 pos){
		polygonData.addPoint(pos);
		lineSelectionData.add(false);
		pointSelectionData.add(false);
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		if(polygonData == null || state==ControlState.DISABLED){
			return false;
		}
		boolean returnValue = false;
		synchronized (polygonData) {
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
		
		return returnValue || super.tap(x, y, count, button);
	}
	
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if(polygonData == null || state==ControlState.DISABLED){
			return false;
		}
		boolean returnValue = false;
		
		synchronized (polygonData) {
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
				editCount = translateSelection(dx, dy);
			}
			
			lastMouse.set(currentMouse);
			
			if(editCount > 0){
				returnValue = true;
			}
		}
		return returnValue|| super.touchDragged(x, y, pointer);
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if(polygonData == null || state==ControlState.DISABLED){
			return false;
		}
		boolean returnValue = false;
		synchronized (polygonData) {
			owner.camToPanel(x, y, lastMouse);
			activeSelectionRegion = false;
			clearMouseSelection();
		}
		return returnValue || super.touchUp(x, y, pointer, button);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if(polygonData == null || state==ControlState.DISABLED){
			return false;
		}
		boolean returnValue = false;
		
		synchronized (polygonData) {
			owner.camToPanel(x, y, lastMouse);	
			firstDown.set(lastMouse);
			updateMouseSelection();
			
			//When no points nearby drag around points
			if(mouseLineSelectedIndex == -1 && mousePointSelectedIndex == -1 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
				activeSelectionRegion = true;
				updateSelectedRegion();
				returnValue = true;
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
		return returnValue || super.touchDown(x, y, pointer, button);
	}
	
	@Override
	public boolean mouseMoved(int x, int y) {
		if(polygonData == null || state==ControlState.DISABLED){
			return false;
		}
		synchronized (polygonData) {
			owner.camToPanel(x, y, lastMouse);
			updateMouseSelection();
		}
		return super.mouseMoved(x, y);
	}
	
	private int translateSelection(float dx, float dy){
		int editCount = 0;
		for(int i = 0; i < polygonData.getPointCount(); i++){
			if(isPointSelected(i)){
				getPoint(i).add(dx, dy);
				editCount++;
			}
		}
		return editCount;
	}
	
	private void removeSelection(){
		synchronized (polygonData) {
			ArrayList<Vector2> pointToRemove = new ArrayList<Vector2>();
			for(int i = 0;i < polygonData.getPointCount(); i++){
				if(isPointSelected(i)){
					pointToRemove.add(polygonData.getPoint(i));

				}
			}
			
			polygonData.removePoints(pointToRemove);
			clearGroupSelection();
			clearMouseSelection();
		}
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
			lineB = polygonData.getPointCount()-1;
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
			lineB = polygonData.getPointCount()-1;
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
		for(int i = 0; i < polygonData.getPointCount(); i++){
			Vector2 p = polygonData.getPoint(i);
			if(p.dst2(lastMouse) < mouseDistance2){
				mousePointSelectedIndex = i;
				pointFound = true;
			}
		}
		
		if(!pointFound){
			for(int i = 0; i < polygonData.getPointCount() && polygonData.getPointCount() >= 2; i++){
				Vector2 p1 = null;
				Vector2 p2 = null;
				if(i == polygonData.getPointCount()-1){
					p1 = polygonData.getPoint(i);
					p2 = polygonData.getPoint(0);
				}else{
					p1 = polygonData.getPoint(i);
					p2 = polygonData.getPoint(i+1);
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
		
		if(lineSelectionData.size() != polygonData.getPointCount()){
			lineSelectionData.clear();
			lineSelectionData.ensureCapacity(polygonData.getPointCount());
			for(int i =0; i < polygonData.getPointCount(); i++){
				lineSelectionData.add(false);
			}
		}
		if(pointSelectionData.size() != polygonData.getPointCount()){
			pointSelectionData.clear();
			pointSelectionData.ensureCapacity(polygonData.getPointCount());
			for(int i =0; i < polygonData.getPointCount(); i++){
				pointSelectionData.add(false);
			}
		}
		for(int i =0; i < polygonData.getPointCount(); i++){
			pointSelectionData.set(i, false);
			lineSelectionData.set(i, false);
		}
	}
	
	private void updateGroupSelection(){
		if(!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
			clearGroupSelection();
		}
		
		//Test rectangle
		if(activeSelectionRegion){
			//Test Points
			for(int i =0; i < polygonData.getPointCount(); i++){
				Vector2 v = polygonData.getPoint(i);
				if(selectedRegion.contains(v.x, v.y)){
					pointSelectionData.set(i, true);
				}
			}		
			
			for(int i = 0; i < polygonData.getPointCount(); i++){
				int p1 = i;
				int p2 = i+1;
				
				if(p2 > polygonData.getPointCount()-1){
					p2 = 0;
				}
				
				if(MathsToolkit.SegmentIntersectRectangle(selectedRegion, polygonData.getPoint(p1), polygonData.getPoint(p2))){
					lineSelectionData.set(i, true);
				}
			}
			
		}
		//Determine Selected Points
		for(int i =0; i < polygonData.getPointCount(); i++){
			if(pointSelectionData.get(i)){
				pointSelectedCount++;
			}
		}
		
		//Determine Selected Lines
		for(int i =0; i < polygonData.getPointCount(); i++){
			if(lineSelectionData.get(i)){
				lineSelectedCount++;
			}
		}
	}
	public void draw(ShapeRenderer rend, PolygonShapeData shape){
		draw(rend, shape, shapeColor);
	}
	public void draw(ShapeRenderer rend, PolygonShapeData shape, Color shapeColor){
		//Draw Lines
		Vector2 p1 = null;
		Vector2 p2 = null;
		Gdx.gl.glLineWidth(3f);
		rend.begin(ShapeType.Line);
		for(int i = 0; i <  shape.getPointCount() && shape.getPointCount() > 1; i++){
			rend.setColor(shapeColor);
			if(i < shape.getPointCount()-1){
				p1 =  shape.getPoint(i);
				p2 =  shape.getPoint(i+1);	
			}else{
				p1 =  shape.getPoint( shape.getPointCount()-1);
				p2 =  shape.getPoint(0);
			}
			rend.line(p1.x, p1.y, p2.x, p2.y);
		}
		rend.end();
		Gdx.gl.glLineWidth(1f);
	}
	public void draw(ShapeRenderer shape){
		boolean edit = state ==ControlState.EDIT;
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
		
		if(polygonData == null || polygonData.getPointCount() == 0){
			return;
		}
		
		synchronized (polygonData) {
			
			//Draw Lines
			Vector2 p1 = null;
			Vector2 p2 = null;
			Gdx.gl.glLineWidth(3f);
			
			shape.begin(ShapeType.Line);
			for(int i = 0; i <  polygonData.getPointCount() && polygonData.getPointCount() > 1; i++){
				if(isLineSelected(i) && edit){
					shape.setColor(shapeHighlightColor);		
				}else{
					shape.setColor(shapeColor);
				}
				if(i < polygonData.getPointCount()-1){
					p1 =  polygonData.getPoint(i);
					p2 =  polygonData.getPoint(i+1);	
				}else{
					p1 =  polygonData.getPoint( polygonData.getPointCount()-1);
					p2 =  polygonData.getPoint(0);
				}
				shape.line(p1.x, p1.y, p2.x, p2.y);
			}
			shape.end();
			
			if(edit){
				Gdx.gl.glLineWidth(1f);
				//Draw Nodes
				float nodeSize = owner.panelToCam(minContactDistance)/2;
				shape.begin(ShapeType.Rectangle);
				for(int i = 0; i <  polygonData.getPointCount()-1; i++){
					Vector2 p = polygonData.getPoint(i);
					if(isPointSelected(i)){
						shape.setColor(controlHighlightColor);
					}else{
						shape.setColor(controlColor);
					}
					shape.rect(p.x-nodeSize, p.y-nodeSize, 2*nodeSize, 2*nodeSize);
				}
				shape.end();
				
				//Draw last node as a circle
				shape.begin(ShapeType.Circle);
				if(isPointSelected(polygonData.getPointCount()-1)){
					shape.setColor(controlHighlightColor);
				}else{
					shape.setColor(controlColor);
				}
				shape.circle(polygonData.getPoint(polygonData.getPointCount()-1).x, polygonData.getPoint(polygonData.getPointCount()-1).y, nodeSize, segments);
				shape.end();
			}
		}
	}
	
	@Override
	public boolean keyDown(int code) {
		synchronized (polygonData) {
			if(Input.Keys.DEL == code){
				removeSelection();
			}	
		}
		return super.keyDown(code);
	}
	public void setPolygon(PolygonShapeData points){
		this.polygonData = points;
		if(points == null){
			this.pointSelectionData.clear();
			this.pointSelectionData.trimToSize();
			this.lineSelectionData.clear();
			this.lineSelectionData.trimToSize();
			
		}else{
			this.pointSelectionData.ensureCapacity(points.getPointCount());
			this.lineSelectionData.ensureCapacity(points.getPointCount());
			clearGroupSelection();
		}
	}
}
