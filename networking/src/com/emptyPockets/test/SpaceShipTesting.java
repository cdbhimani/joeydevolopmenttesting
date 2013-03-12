package com.emptyPockets.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptyPockets.box2d.gui.Box2DScreen;
import com.emptyPockets.box2d.shape.data.RectangleShapeData;
import com.emptyPockets.utils.OrthoCamController;

public class SpaceShipTesting extends Box2DScreen {
	float pixelScale = 0.1f;
	
	float mainThruster = 10000;
	float brakeThruster = 5000;
	float turnThruster = 1000;
	
	TextureAtlas atlas;
	
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRender;
	
	Sprite sprite;
	Body ship;
	RectangleShapeData shape;
	OrthoCamController control;
	public SpaceShipTesting(InputMultiplexer inputProcessor) {
		super(inputProcessor);
		setShowDebug(true);
		setClearColor(Color.BLACK);
		atlas = new TextureAtlas("pack/SpaceTest.pack");
		control = new OrthoCamController(getBox2DWorldCamera());
	}

	@Override
	public void updateWorldCamera(OrthographicCamera worldCamera) {
		super.updateWorldCamera(worldCamera);
		Vector2 pos= ship.getPosition();
		worldCamera.position.x = pos.x;
		worldCamera.position.y = pos.y;
	}
	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		input.addProcessor(control);
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(control);
	}
	@Override
	public void show() {
		super.show();
		spriteBatch = new SpriteBatch();
		shapeRender = new ShapeRenderer();
	}

	@Override
	public void hide() {
		super.hide();
		spriteBatch.dispose();
		spriteBatch = null;
		shapeRender.dispose();
		shapeRender = null;
		atlas.dispose();
	}

	@Override
	public void createWorld(World world) {
		sprite = atlas.createSprite("ships/10");
		sprite.setSize(sprite.getWidth()*pixelScale,sprite.getHeight()*pixelScale);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2, new Vector2(0, 0).mul(pixelScale), 0);
		
		BodyDef def = new BodyDef();
		def.angularDamping = 1;
		def.type =BodyType.DynamicBody;
		ship = world.createBody(def);
		ship.createFixture(shape, 1);
		
		shape.dispose();
	}

	@Override
	public void createStage(Stage stage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawBackground(float delta) {
		super.drawBackground(delta);
	}
	@Override
	public void drawScreen(float delta) {
		
		Vector2 shipPos = ship.getPosition();
		sprite.setPosition(shipPos.x-sprite.getWidth()/2, shipPos.y-sprite.getHeight()/2);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setRotation(0);
		sprite.rotate(ship.getAngle()*MathUtils.radDeg);
		
		spriteBatch.setProjectionMatrix(getBox2DWorldCamera().combined);
		spriteBatch.begin();
		sprite.draw(spriteBatch);
		spriteBatch.end();
	
		
		shapeRender.setProjectionMatrix(getBox2DWorldCamera().combined);
		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(Color.RED);
		
		Vector2 force;
		Vector2 pos;
			
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			force = ship.getWorldVector(new Vector2(1,0)).cpy();
			force.mul(-brakeThruster);
			pos = new Vector2(-sprite.getWidth()/2, 0);
			pos =  ship.getWorldPoint(pos).cpy();
			shapeRender.line(pos.x, pos.y, pos.x-force.x, pos.y-force.y);
		}
		
		if (Gdx.input.isKeyPressed(Keys.UP) ) {
			force = ship.getWorldVector(new Vector2(1,0)).cpy();
			force.mul(mainThruster);
			pos = new Vector2(-sprite.getWidth()/2, 0);
			pos =  ship.getWorldPoint(pos).cpy();
			shapeRender.line(pos.x, pos.y, pos.x-force.x, pos.y-force.y);
		}
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			force = ship.getWorldVector(new Vector2(1,0)).cpy();
			force.mul(turnThruster);
			pos = new Vector2(-sprite.getWidth()/2, -sprite.getHeight()/4);
			pos =  ship.getWorldPoint(pos).cpy();
			shapeRender.line(pos.x, pos.y, pos.x-force.x, pos.y-force.y);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			force = ship.getWorldVector(new Vector2(1,0)).cpy();
			force.mul(turnThruster);
			pos = new Vector2(-sprite.getWidth()/2, +sprite.getHeight()/4);
			pos =  ship.getWorldPoint(pos).cpy();
			shapeRender.line(pos.x, pos.y, pos.x-force.x, pos.y-force.y);
		}
		shapeRender.end();
		
	}

	@Override
	public void updateWorld(float delta) {
		super.updateWorld(delta);
		boolean change = false;

		Vector2 force;
		Vector2 pos;
			
		Vector2 currentRightNormal = ship.getWorldVector(new Vector2(0,1)).cpy();
		currentRightNormal.mul(-0.5f*currentRightNormal.dot(ship.getLinearVelocity())*ship.getMass());
		ship.applyLinearImpulse(currentRightNormal, ship.getWorldCenter());
		
		if(Gdx.input.isKeyPressed(Keys.DOWN)){
			force = ship.getWorldVector(new Vector2(1,0)).cpy();
			force.mul(-brakeThruster);
			pos = new Vector2(-sprite.getWidth()/2, 0);
			pos =  ship.getWorldPoint(pos).cpy();
			ship.applyForce(force,pos);
		}
		
		if (Gdx.input.isKeyPressed(Keys.UP) ) {
			force = ship.getWorldVector(new Vector2(1,0)).cpy();
			force.mul(mainThruster);
			if(Gdx.input.isKeyPressed(Keys.DOWN)){
				force.mul(-1);
			}
			pos = new Vector2(-sprite.getWidth()/2, 0);
			pos =  ship.getWorldPoint(pos).cpy();
			ship.applyForce(force,pos);
		}
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			
			force = ship.getWorldVector(new Vector2(1,0)).cpy();
			force.mul(turnThruster);
			if(Gdx.input.isKeyPressed(Keys.DOWN)){
				force.mul(-1);
			}
			pos = new Vector2(-sprite.getWidth()/2, -sprite.getHeight()/4);
			pos =  ship.getWorldPoint(pos).cpy();
			ship.applyForce(force,pos);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			force = ship.getWorldVector(new Vector2(1,0)).cpy();
			force.mul(turnThruster);
			pos = new Vector2(-sprite.getWidth()/2, +sprite.getHeight()/4);
			pos =  ship.getWorldPoint(pos).cpy();
			ship.applyForce(force,pos);
		}
	}
	
	public void print(Body body){
		System.out.printf("V[%3.3f,%3.3f] A[%f] AV[%f] M[%3.3f]\n",
				body.getLinearVelocity().x, 
				body.getLinearVelocity().y, 
				body.getAngle(),
				body.getAngularVelocity(),
				body.getMass()
				);
	}
}
