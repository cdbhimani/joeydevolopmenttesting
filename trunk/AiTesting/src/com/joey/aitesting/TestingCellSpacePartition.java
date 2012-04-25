package com.joey.aitesting;

import com.joey.aitesting.game.GameWorld;
import com.joey.aitesting.game.VehicleControler;
import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.entities.Vehicle;
import com.joey.aitesting.game.graphics.ConsoleLogger;
import com.joey.aitesting.game.graphics.ConsoleViewer;
import com.joey.aitesting.game.graphics.EventTimer;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.joey.aitesting.game.shapes.Vector2D;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class TestingCellSpacePartition extends Game implements
		ApplicationListener {
	EventTimer timer = new EventTimer();
	
	OrthographicCamera cam;
	GameWorld world;

	ConsoleLogger log;
	ConsoleViewer view;

	ShapeRenderer shape1;
	ShapeRenderer shape2;
	ShapeRenderer shape3;
	ShapeRenderer shape4;
	
	long lastUpdate = 0;
	float diffTime = 0;
	boolean firstUpdate = true;
	
	Vehicle vehicle = new Vehicle(null);
	
	@Override
	public void create() {
		shape1 = new ShapeRenderer();
		shape2 = new ShapeRenderer();
		shape3 = new ShapeRenderer();
		shape4 = new ShapeRenderer();
		
		log = new ConsoleLogger();
		view = new ConsoleViewer(log);
		Gdx.input.setInputProcessor(log);
		
		createWorld();
	}
	
	public void createWorld(){
		Rectangle2D r = new Rectangle2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		world = new GameWorld(r);
		int num = 1000;
		for(int i = 0; i < num; i++){
			float x = (float)(r.x1+Math.random()*r.getWidth());
			float y = (float)(r.y1+Math.random()*r.getHeight());
			
			addEntitie(x, y, 50);
		}
	}
	
	public void addEntitie(float x, float y, float maxVel) {
		Vehicle entity = new Vehicle(world);
		entity.pos.setLocation(x, y);
		entity.vel.setLocation((float) (maxVel * (1 - 2 * Math.random())),
				(float) (maxVel * (1 - 2 * Math.random())));

		entity.maxSpeed = maxVel;
		entity.maxForce = 500;
		entity.mass = 1;
		entity.scale = new Vector2D(1, 1);
		world.addVehicle(entity);
	}

	@Override
	public void render() {
		
		if(firstUpdate){
			lastUpdate = System.currentTimeMillis();
			firstUpdate = false;
		}else{
			timer.mark("Draw");
		}
		log.println(timer.getTitle());
		log.println(timer.getData());
		timer.clear();
		
		diffTime = (System.currentTimeMillis() - lastUpdate) / 1000f;
		lastUpdate = System.currentTimeMillis();
		
		timer.mark("World Update");
		world.update(diffTime);
		timer.tick("World Update");
		
		timer.mark("Quad Tree Update");
		world.updateQuadTree();
		timer.mark("Quad Tree Update");
		
		timer.mark("Render");
		cam.update();
		cam.apply(Gdx.gl10);
		shape1.setProjectionMatrix(cam.combined);
		shape2.setProjectionMatrix(cam.combined);
		shape3.setProjectionMatrix(cam.combined);
		shape4.setProjectionMatrix(cam.combined);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14

		shape1.setColor(Color.RED);
		shape2.setColor(Color.GREEN);
		shape3.setColor(Color.BLUE);
		shape4.setColor(Color.PINK);
		
	
		view.draw(cam);
		timer.tick("Render");
		
		timer.mark("Draw");
	}
	

	@Override
	public void dispose() {
	}

	@Override
	public void resize(int width, int height) {
		cam = new OrthographicCamera(width, height);
		cam.translate(width / 2, height / 2, 0);
		//world.setSize(width,height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	
}
