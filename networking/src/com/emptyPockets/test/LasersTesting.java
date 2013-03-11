package com.emptyPockets.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

public class LasersTesting extends Box2DScreen {
	float pixelScale = 0.1f;
	float wide = 0;
	float high = 0;
	
	TextureAtlas atlas;
	SpriteBatch spriteBatch;
	Sprite sprite;
	Body ship;
	RectangleShapeData shape;
	OrthoCamController control;
	public LasersTesting(InputMultiplexer inputProcessor) {
		super(inputProcessor);
		setShowDebug(true);
		setClearColor(Color.BLACK);
		atlas = new TextureAtlas("pack/SpaceTest.pack");
		sprite = atlas.createSprite("ships/1");
		control = new OrthoCamController(getBox2DWorldCamera());
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
	}

	@Override
	public void hide() {
		super.hide();
		spriteBatch.dispose();
		spriteBatch = null;
		atlas.dispose();
	}

	@Override
	public void createWorld(World world) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sprite.getWidth()*pixelScale, sprite.getHeight()*pixelScale, new Vector2(sprite.getWidth()/2, sprite.getHeight()/2).mul(pixelScale), 0);
		
		BodyDef def = new BodyDef();
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
	public void drawScreen(float delta) {
		spriteBatch.setProjectionMatrix(getBox2DWorldCamera().combined);
		spriteBatch.begin();

		sprite.setPosition(ship.getPosition().x*pixelScale, ship.getPosition().y*pixelScale);
		sprite.setRotation(ship.getAngle());
		sprite.draw(spriteBatch);

		spriteBatch.end();
	}

	@Override
	public void updateWorld(float delta) {
		super.updateWorld(delta);
		boolean change = false;
		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN)) {
			Vector2 force = ship.getWorldVector(new Vector2(1,0)).cpy();
			
			
			force.mul(1000);
			if(Gdx.input.isKeyPressed(Keys.DOWN)){
				force.mul(-1);
			}
			
			ship.applyForceToCenter(force);
			change = true;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			ship.applyTorque(-100000f);
			change = true;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			ship.applyTorque(100000f);
			change = true;
		}
		if(change)
		print(ship);
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
