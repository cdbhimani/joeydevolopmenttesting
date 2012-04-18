package com.me.mygdxgame.spatialPartitioning;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class QuadTreeViewer implements GestureListener{
	QuadTree tree;
	
	ShapeRenderer gridRender;
	ShapeRenderer particleRender;
	
	Rectangle2D region;
	Point2D worldPos = new Point2D();
	Point2D mousePos = new Point2D();
	Point2D dragOffset = new Point2D();
	
	float zoom = 1;
	long lastUpdate = 0;
	float border = 100;
	
	public QuadTreeViewer(Rectangle2D region){
		this.region = region;
		tree = new QuadTree(region, 3);
		gridRender = new ShapeRenderer();
		particleRender = new ShapeRenderer();
	}
	
	public void addPoints(int number){
		ArrayList<Point2D> points = new ArrayList<Point2D>(number);
		for (float i = 0; i < number; i++) {
			float maxVel = 100;
			Point2D p = new Point2D();
			p.x = (float) (region.x1+Math.random() * (region.x2-region.x1));
			p.y = (float) (region.y1+Math.random() * (region.y2-region.y1));
			p.vx = (float) (maxVel-Math.random() *2* maxVel);
			p.vy = (float) (maxVel-Math.random() * 2*maxVel);
			
			points.add(p);
			tree.addPoint(p);
		}
	}
	public void updatePoints(){
		float diff = (System.currentTimeMillis()-lastUpdate)/1000f;
		lastUpdate = System.currentTimeMillis();
		for(int i = 0; i < tree.points.size(); i++){
			//Update Positions
			Point2D p = tree.points.get(i);
			p.x += p.vx*diff;
			p.y += p.vy*diff;
			
			//Do Calculation
			ArrayList<Point2D> rst =tree.getPointsInRegion(
					new Rectangle2D(p.x-border, p.y-border, p.x+border, p.y+border));
			float vx = 0;
			float vy = 0;
			int count = 0;
			for(Point2D data : rst){
				vx+=data.vx;
				vy+=data.vy;
				count++;
			}
			
			//Validate position
			if(p.x <= region.x1)p.x = region.x2;
			if(p.x >= region.x2)p.x = region.x1;
			
			if(p.y <= region.y1)p.y = region.y2;
			if(p.y >= region.y2)p.y = region.y1;
		}
	}
	
	public void rebuildTree(){
		tree.rebuild();
	}
	public void render(GLCommon gl, Camera camera){
		camera.position.x = worldPos.x;
		camera.position.y = worldPos.y;
		camera.update();
		gridRender.setProjectionMatrix(camera.combined);
		particleRender.setProjectionMatrix(camera.combined);
		
		gridRender.setColor(Color.WHITE);
		gridRender.begin(ShapeType.FilledRectangle);
		gridRender.filledRect(region.x1, region.y1,region.x2-region.x1, region.y2-region.y1);
		gridRender.end();
		
		
		particleRender.setColor(Color.BLUE);
		synchronized (tree) {
			for(Point2D p : tree.points){
				particleRender.begin(ShapeType.FilledCircle);
				particleRender.filledCircle(p.x, p.y, 2);
				particleRender.end();
			}
		}
		
		
	}
	@Override
	public boolean touchDown(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean tap(int x, int y, int count) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean longPress(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean fling(float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean pan(int x, int y, int deltaX, int deltaY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		zoom = originalDistance/currentDistance;
		return true;
	}
	@Override
	public boolean pinch(Vector2 initialFirstPointer,
			Vector2 initialSecondPointer, Vector2 firstPointer,
			Vector2 secondPointer) {
		// TODO Auto-generated method stub
		return false;
	}
}
