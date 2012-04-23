package com.joey.testing.game.shapes;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.JPanel;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.joey.testing.game.graphics.ConsoleLogger;
import com.joey.testing.game.graphics.EventTimer;
import com.joey.testing.game.graphics.FPSCounter;

public class ShapeViewer implements ApplicationListener, InputProcessor {

	FPSCounter render = new FPSCounter();
	EventTimer timeer = new EventTimer();
	
	ShapeRenderer shapeRender;
	
	ShapeRenderer shapeIntersectRender;
	ShapeRenderer shapeContainsRender;
	
	BitmapFont consoleFont;
	SpriteBatch consoleBatch;

	OrthographicCamera cam;

	ArrayList<Shape2D> shapes = new ArrayList<Shape2D>();
	ArrayList<Shape2D> intersects = new ArrayList<Shape2D>();
	ArrayList<Shape2D> contains = new ArrayList<Shape2D>();
	
	Shape2D selectedShape = null;
	Vector2D hitPoint = null;

	ConsoleLogger console = new ConsoleLogger();

	@Override
	public void create() {
		
		Gdx.input.setInputProcessor(this);
		shapeRender = new ShapeRenderer();
		shapeIntersectRender = new ShapeRenderer();
		shapeContainsRender = new ShapeRenderer();
		
		consoleBatch = new SpriteBatch();
		consoleFont = new BitmapFont();

		int numX = 10;
		int numY = 10;
		
		float sizeX = Gdx.graphics.getWidth()/numX;
		float sizeY = Gdx.graphics.getHeight()/numY;
		float deltaX = 0.0001f;
		float deltaY = 0.0001f;
		
		shapes.add(new Rectangle2D(100, 100, 200,200));
		for(int x = 0; x < numX; x++){
			for(int y = 0; y < numY; y++){
				float posX = x*(sizeX+deltaX);
				float posY = y*(sizeY+deltaY);
				shapes.add(new Rectangle2D(posX, posY, sizeX, sizeY));
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.translate(Gdx.graphics.getWidth()/ 2, Gdx.graphics.getHeight()/ 2, 0);
	}

	public static void drawShape(ShapeRenderer render, Shape2D s){
		switch(s.type)
		{
			case Rectangle2D :
				Rectangle2D s1 = (Rectangle2D)s;
				render.begin(ShapeType.Rectangle);
				render.rect(s1.x, s1.y, s1.sizeX, s1.sizeY);
				render.end();
				break;
			
			case Circle2D : 
				Circle2D e1 = (Circle2D)s;		
				render.begin(ShapeType.Circle);
				render.circle(e1.x, e1.y, e1.rad);
				render.end();
				
				render.begin(ShapeType.Circle);
				render.circle(e1.x, e1.y, e1.rad);
				render.end();
				break;
		}
	}
	
	public void updateIntersectTests(){
		intersects.clear();
		contains.clear();
		for(Shape2D s1 : shapes){
			for(Shape2D s2 : shapes){
				if(s1 != s2){
					if(s1.intersects(s2)){
						intersects.add(s1);
					}
				}
			}	
		}
		
	}
	
	@Override
	public synchronized void render() {
		timeer.clear();
		render.tick();
		
		timeer.mark("Physics");
		updateIntersectTests();
		timeer.tick("Physics");
		
		timeer.mark("Draw");
		cam.update();
		cam.apply(Gdx.gl10);
		shapeRender.setProjectionMatrix(cam.combined);
		shapeIntersectRender.setProjectionMatrix(cam.combined);
		shapeContainsRender.setProjectionMatrix(cam.combined);
		consoleBatch.setProjectionMatrix(cam.combined);

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14


		console.println("Found ["+shapes.size()+"] - I["+intersects.size()+"] - C["+contains.size()+"]");
		shapeRender.setColor(Color.RED);
		for (Shape2D s : shapes) {
			drawShape(shapeRender, s);
		}

		shapeIntersectRender.setColor(Color.GREEN);
		for (Shape2D s : intersects) {
			drawShape(shapeIntersectRender, s);
		}
		
		shapeContainsRender.setColor(Color.BLUE);
		for (Shape2D s : contains) {
			drawShape(shapeContainsRender, s);
		}
		
		consoleFont.setScale(1f);
		consoleFont.setColor(Color.WHITE);
		consoleBatch.begin();
		consoleFont.drawMultiLine(consoleBatch, console.toString(), 0,
				Gdx.graphics.getHeight());
		consoleBatch.end();
		shapeRender.end();
		timeer.tick("Draw");
		console.printf("FPS [%f] Physics[%f] Draw [%f]\n", render.getFPS(), timeer.getTime("Physics"),timeer.getTime("Draw") );
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Vector2D toImageSpace(int x, int y) {
		Vector3 v = new Vector3(x, y, 0);
		cam.unproject(v);
		return new Vector2D(v.x, v.y);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	public Shape2D getMouseTouchShape(Vector2D p){
		
		for(Shape2D s : shapes){
			if(s.contains(p)){
				return s;
			}
		}
		return null;
	}
	@Override
	public synchronized  boolean touchDown(int x, int y, int pointer, int button) {
		Vector2D p = toImageSpace(x, y);
		selectedShape = getMouseTouchShape(p);
		if(selectedShape != null){
			hitPoint = p;
			hitPoint.x = p.x - selectedShape.x;
			hitPoint.y = p.y - selectedShape.y;
		} else {
			selectedShape = null;
		}
		return true;
	}

	@Override
	public synchronized boolean touchUp(int x, int y, int pointer, int button) {
		selectedShape = null;
		hitPoint = null;
		return false;
	}

	@Override
	public synchronized boolean touchDragged(int x, int y, int pointer) {
		Vector2D p = toImageSpace(x, y);
		if (selectedShape != null) {
			selectedShape.moveTo(p.x - hitPoint.x, p.y - hitPoint.y);
		}
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
