package com.joey.png.box2D;

import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.joey.png.pixelMapHelp.PixmapHelper;
import com.joey.toolkit.camera.OrthographicCameraControler;

public class Box2DApplication implements ApplicationListener, InputProcessor {
	float metersPerPixel = 1/10f;
	OrthographicCamera cam;
	OrthographicCameraControler controller;
	World world;
	Box2DDebugRenderer render;
	RayHandler rayHandler;
	Body m_attachment;
	Body car;

	@Override
	public void create() {
		world = new World(new Vector2(0, -10), true);
		rayHandler = new RayHandler(world);
		
		render = new Box2DDebugRenderer(true, true, false, true);
		cam = new OrthographicCamera();
		controller = new OrthographicCameraControler(cam);
		
		Gdx.input.setInputProcessor(controller);
		
		createWorld(world);
	}


	public void createWorld(World world) {
		Body ground;

		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		bd.position.set(0,0);
		ground = world.createBody(bd);
		
		Shape shape;
		shape = new PolygonShape();
		((PolygonShape)shape).setAsBox(3000,1);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		ground.createFixture(fd);
		shape.dispose();

		for (int i = 0; i < 100; i++) {
			bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.position.set((int) (100 * (1 - 2 * Math.random())),
					(int) (100* (2 * Math.random())));
			car = world.createBody(bd);

			shape = new CircleShape();
			((CircleShape) shape).setRadius(1);

			fd = new FixtureDef();
			fd.density = 100f;
			fd.restitution = 0.8f;
			fd.shape = shape;
			car.createFixture(fd);
			shape.dispose();

			float alpha = 0.8f;
			Light p1;
			if(Math.random() < 0.99){
				p1= new ConeLight(rayHandler, 150,
					new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), alpha), 30, 0, 0, 0,3+(int)(Math.random()*90));
				p1.setSoft(true);
				p1.setSoftnessLenght(5);
				p1.attachToBody(car, 0, 0);
			}else if(Math.random() < 0.2){
				p1= new PointLight(rayHandler, 1000,
						new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), alpha), 30, 0, 0);
					p1.setSoft(true);
					p1.setSoftnessLenght(5);
					p1.attachToBody(car, 0, 0);
			}

		}
		
	}

	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width*this.metersPerPixel;
		cam.viewportHeight = height*this.metersPerPixel;
	}

	private void updateWorld() {
		world.step(Gdx.graphics.getDeltaTime(), 3, 3);
	}

	@Override
	public void render() {
		updateWorld();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		cam.update();
		
		
		rayHandler.setCombinedMatrix(cam.combined);
		rayHandler.updateAndRender();
		render.render(world, cam.combined);
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

	@Override
	public boolean keyDown(int keycode) {
		
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

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		
		return false;
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
		System.out.println("here:"+car.getLinearVelocity());
//		car.applyLinearImpulse(0,3,car.getPosition().x, car.getPosition().y);
		car.applyAngularImpulse(1f);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
