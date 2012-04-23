package com.joey.testing;

import com.joey.testing.game.GameWorld;
import com.joey.testing.game.VehicleControler;
import com.joey.testing.game.entities.Vehicle;
import com.joey.testing.game.shapes.Vector2D;

import java.awt.geom.AffineTransform;

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

public class TestingClass extends Game implements ApplicationListener,
		InputProcessor {
	BitmapFont font;
	BitmapFont text;
	SpriteBatch allFish;
	SpriteBatch leadFish;
	SpriteBatch spriteBatch;
	ShapeRenderer shapes;
	private Texture fishTexture;
	private TextureRegion fishRegion;

	private Texture leadTexture;
	private TextureRegion leadRegion;

	GameWorld world = new GameWorld();
	long lastUpdate = System.currentTimeMillis();
	float diffTime = 0;
	float nanoScale = 1 / 1000000f;
	float updateWorldTime = 0;
	float updateCellTime = 0;
	float renderTime = 0;

	float rad = 6f;

	float allFishScale = 3f;
	float leadFishScale = 3f;
	float scaleStep = 1;

	int moveX = 0;
	int moveY = 0;
	int stepSize = 10;

	OrthographicCamera cam;

	@Override
	public void create() {
		allFish = new SpriteBatch();
		leadFish = new SpriteBatch();
		spriteBatch = new SpriteBatch();
		shapes = new ShapeRenderer();
		font = new BitmapFont();
		text = new BitmapFont();

		text.setScale(1f);
		createEntities();
		loadTextures();

		Gdx.input.setInputProcessor(this);
	}

	public void loadTextures() {
		fishTexture = new Texture(Gdx.files.internal("fish1.png"));
		leadTexture = new Texture(Gdx.files.internal("fish2.png"));

		fishRegion = new TextureRegion(fishTexture);
		leadRegion = new TextureRegion(leadTexture);
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		for (int i = 0; i < 10; i++)
			addEntitie(x, Gdx.graphics.getHeight() - y, 400);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (Input.Keys.ESCAPE == keycode) {
			Gdx.app.exit();
		}
		if (Input.Keys.UP == keycode || Input.Keys.J == keycode) {
			moveY += stepSize;
		} else if (Input.Keys.DOWN == keycode || Input.Keys.N == keycode) {
			moveY -= stepSize;
		} else if (Input.Keys.RIGHT == keycode || Input.Keys.M == keycode) {
			moveX += stepSize;
		} else if (Input.Keys.LEFT == keycode || Input.Keys.B == keycode) {
			moveX -= stepSize;
		} else if (Input.Keys.R == keycode) {
			randId();
		} else if (Input.Keys.Q == keycode) {
			allFishScale += scaleStep;
		} else if (Input.Keys.A == keycode) {
			allFishScale -= scaleStep;
			if (allFishScale < 1) {
				allFishScale = 1;
			}
		} else if (Input.Keys.W == keycode) {
			leadFishScale += scaleStep;
		} else if (Input.Keys.S == keycode) {
			leadFishScale -= scaleStep;
			if (leadFishScale < 1) {
				leadFishScale = 1;
			}
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (Input.Keys.UP == keycode) {
			moveY -= stepSize;
		} else if (Input.Keys.DOWN == keycode) {
			moveY += stepSize;
		} else if (Input.Keys.RIGHT == keycode) {
			moveX -= stepSize;
		} else if (Input.Keys.LEFT == keycode) {
			moveX += stepSize;
		}

		return true;
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

		if (world.getVehicles().size() > 2) {
			Vehicle v1 = world.getVehicles().get(0);
			entity.steering.useRepel = true;
			entity.steering.repel = v1.pos;
		}

	}

	public void randId() {
		Vehicle v1 = world.getVehicles().get(0);

		float rotationAngle = (float) (Math.PI * 2 * Math.random());
		v1.vel.setLocation(
				(float) ((v1.vel.x) * Math.cos(rotationAngle) + (v1.vel.y)
						* Math.sin(rotationAngle)),
				(float) ((v1.vel.y) * Math.cos(rotationAngle) - (v1.vel.x)
						* Math.sin(rotationAngle)));

	}

	public void createEntities() {
		float maxVel = 500;
		float entCount = 1;
		for (int i = 0; i < entCount; i++) {
			float x = (float) (Gdx.graphics.getWidth() * Math.random());
			float y = (float) (Gdx.graphics.getHeight() * Math.random());
			addEntitie(x, y, maxVel);
		}

	}

	public void updateWorld() {

		long start = 0;
		long end = 0;
		diffTime = (System.currentTimeMillis() - lastUpdate) / 1000f;
		lastUpdate = System.currentTimeMillis();

		start = System.nanoTime();
		world.getVehicles().get(0).vel.x += moveX;
		world.getVehicles().get(0).vel.y += moveY;
		world.update(diffTime);
		end = System.nanoTime();
		updateWorldTime = (end - start) * nanoScale;
		
		start = System.nanoTime();
		world.updateCellSpace();
		end = System.nanoTime();
		updateCellTime = (end - start) * nanoScale;
		
	}

	@Override
	public void render() {
		try {
			cam.update();
			cam.apply(Gdx.gl10);
			spriteBatch.setProjectionMatrix(cam.combined);
			allFish.setProjectionMatrix(cam.combined);
			leadFish.setProjectionMatrix(cam.combined);
			shapes.setProjectionMatrix(cam.combined);
		} catch (Exception e) {
			System.out.println("HERE");
		}
		updateWorld();
		drawWorld();
		drawOverlay();

	}

	public void drawOverlay() {
		
		shapes.begin(ShapeType.FilledRectangle);
		shapes.filledRect(0, 0, 10, 10);
		shapes.end();
		
		spriteBatch.begin();
		font.setColor(Color.WHITE);

		font.draw(spriteBatch, "Render Time : " + renderTime, 20, 30);
		font.draw(spriteBatch, "World  Time : " + updateWorldTime, 20, 50);
		font.draw(spriteBatch, "Cell U Time : " + updateCellTime, 20, 70);
		font.draw(spriteBatch, "Entities: " + world.getVehicles().size(), 20, 90);
		font.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20,
				110);
		spriteBatch.end();
	}

	public void drawWorld() {

		long start = 0;
		long end = 0;
		start = System.nanoTime();

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14

		float sizeX;
		float sizeY;

		sizeX = leadTexture.getWidth() / allFishScale;
		sizeY = leadTexture.getHeight() / allFishScale;

		allFish.begin();
		for (int i = 1; i < world.getVehicles().size(); i++) {
			Vehicle entity = world.getVehicles().get(i);
			
			allFish.draw(fishRegion, entity.pos.x-sizeX/2, entity.pos.y-sizeY/2, sizeX / 2,
					sizeY / 2, sizeX, sizeY, 1, 1,
					(float) (Math.toDegrees(entity.angle)));
		}
		allFish.end();

		if (world.getVehicles().size() > 0) {
			sizeX = leadTexture.getWidth() / leadFishScale;
			sizeY = leadTexture.getHeight() / leadFishScale;

			Vehicle entity = world.getVehicles().get(0);
			leadFish.begin();

			leadFish.draw(leadRegion, entity.pos.x, entity.pos.y, sizeX / 2,
					sizeY / 2, sizeX, sizeY, 1, 1,
					(float) (Math.toDegrees(entity.angle)));
			leadFish.end();
		}

		end = System.nanoTime();
		renderTime = (end - start) * nanoScale;

	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		allFish.dispose();
		leadFish.dispose();
		font.dispose();
		world.getVehicles().clear();
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("HERE");
		world.setSize(width, height);
		cam = new OrthographicCamera(width, height);
		cam.translate(width/2, height/2, 0);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
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

class Controls extends InputAdapter {

}
