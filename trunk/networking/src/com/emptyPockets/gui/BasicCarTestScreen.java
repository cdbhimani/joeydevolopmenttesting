package com.emptyPockets.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.emptyPockets.box2d.Car;

public class BasicCarTestScreen extends StageScreen{
	public float worldToScreen = .51f;
	OrthographicCamera worldCam;
	
	float worldBorderInset = 0.1f;
	float worldBorder = 10f;
	
	float playerRotationPad = MathUtils.degreesToRadians*30000;
	
	Touchpad pad;
	
	TextButton left;
	TextButton right;
	
	TextButton in;
	TextButton out;
	
	TextButton accel;
	TextButton decel;
	
	Vector2 velocityTemp = new Vector2();	
	Car car;
	
	long zoomLast = 0;
	long zoomDelay = 100;
	
	Box2DDebugRenderer render;
	World world;
	
	public BasicCarTestScreen(InputMultiplexer inputProcessor) {
		super(inputProcessor);
		
		
		in= new TextButton("+",getSkin());
		out = new TextButton("-",getSkin());
		
		left = new TextButton("L",getSkin());
		right = new TextButton("R",getSkin());
		accel = new TextButton("A",getSkin());
		decel = new TextButton("D",getSkin());
		
		worldCam = new OrthographicCamera();
		setClearColor(Color.BLACK);
		render = new Box2DDebugRenderer();
		
		pad = new Touchpad(5f, getSkin());
		setupBox2D();
		
	}
	
	public void setupBox2D(){
		world= new World(new Vector2(), true);
//		createGround();
		createPlayer();
		createObjects(100, 2,5);
		
	}
	
	@Override
	public boolean scrolled(int amount) {
		zoom(amount);
		return true;
	}
	
	public void zoom(int amount) {
		float scale = 0.03f;
		if(amount >0){
			worldToScreen*=1+scale;
		}else{
			worldToScreen*=1-scale;
		}
	};

	private void createObjects(int count, float minSize, float maxSize){
		for(int i = 0; i < count; i++){
			BodyDef bodyDef = new BodyDef();
			bodyDef.type=BodyType.DynamicBody;
		
			float radius = MathUtils.random(minSize, maxSize); 
			float x= MathUtils.random(0,Gdx.graphics.getWidth())*worldToScreen;
			float y= MathUtils.random(0,Gdx.graphics.getHeight())*worldToScreen;
			bodyDef.position.set(x,y);
			
			Body body = world.createBody(bodyDef);
			body.setLinearDamping(1);
			body.setAngularDamping(0.1f);
			
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.density = MathUtils.random(0.5f, 1f);
			
		
			Shape s = null;
			switch(MathUtils.random(1)){
				case(0):
					s = new CircleShape();
					s.setRadius(radius);
					fixtureDef.shape = s;
					break;
				case(1):
					s= new PolygonShape();
					((PolygonShape)s).setAsBox(radius, radius*MathUtils.random(0.5f, 2f));
					fixtureDef.shape = s;
					break;
			}
			
			Fixture fixture = body.createFixture(fixtureDef);
			fixture.setRestitution(0.99f);
			
			if(s!=null)
				s.dispose();
		}
	}
	
	private void createGround(){
		//Bottom
		{
			BodyDef groundBodyDef =new BodyDef();  
			groundBodyDef.position.set(new Vector2(0, 0));  
	
			Body groundBody = world.createBody(groundBodyDef);  
			
			PolygonShape groundBox = new PolygonShape();  
			groundBox.setAsBox(worldBorder/2, worldBorderInset);
			
			Fixture fix = groundBody.createFixture(groundBox, 0.0f); 
			
			fix.setRestitution(0.9f);
			groundBox.dispose();
		}
		
		//TOP
		{
			BodyDef groundBodyDef =new BodyDef();  
			groundBodyDef.position.set(new Vector2(0, Gdx.graphics.getHeight()-worldBorderInset));  
	
			Body groundBody = world.createBody(groundBodyDef);  
			
			PolygonShape groundBox = new PolygonShape();  
			groundBox.setAsBox(worldBorder/2, worldBorderInset);
			
			Fixture fix = groundBody.createFixture(groundBox, 0.0f); 
			
			fix.setRestitution(0.9f);
			groundBox.dispose();
	
		}
		
		//LEFT
		{
			BodyDef groundBodyDef =new BodyDef();  
			groundBodyDef.position.set(0,0);  
	
			Body groundBody = world.createBody(groundBodyDef);  
			
			PolygonShape groundBox = new PolygonShape();  
			groundBox.setAsBox(worldBorderInset,worldBorder/2);
			
			Fixture fix = groundBody.createFixture(groundBox, 0.0f); 
			
			fix.setRestitution(0.9f);
			groundBox.dispose();
		}
		
		//RIGHT
		{
			BodyDef groundBodyDef =new BodyDef();  
			groundBodyDef.position.set(Gdx.graphics.getWidth()-worldBorderInset,0);  
	
			Body groundBody = world.createBody(groundBodyDef);  
			
			PolygonShape groundBox = new PolygonShape();  
			groundBox.setAsBox(worldBorderInset,worldBorder/2);
			
			Fixture fix = groundBody.createFixture(groundBox, 0.0f); 
			
			fix.setRestitution(0.9f);
			groundBox.dispose();
		}
	}
	
	private void createPlayer() {
		car = new Car(world);
		
	}

	@Override
	public void updateLogic(float delta) {
		float x = 0;
		float y = 0;
		
		if(left.isPressed())x=-1;
		if(right.isPressed())x=1;
		
		if(accel.isPressed())y=1;
		if(decel.isPressed())y=-1;
		
		if(in.isPressed() || out.isPressed()){
			if(System.currentTimeMillis() > zoomLast+zoomDelay){
				if(in.isPressed()){
					scrolled(+1);
				}
				if(out.isPressed()){
					scrolled(-1);
				}
				zoomLast = System.currentTimeMillis();
			}
		}
		car.update(x, y);
		world.step(delta, 1, 1);
	}

	public void updateCameraViewport(){
		worldCam.viewportWidth = Gdx.graphics.getWidth()*worldToScreen;
		worldCam.viewportHeight= Gdx.graphics.getHeight()*worldToScreen;
		worldCam.position.set(car.getBody().getPosition().x, car.getBody().getPosition().y,0);
		worldCam.update();
	}
	
	@Override
	public void drawScreen(float delta) {
		updateCameraViewport();
		render.render(world, worldCam.combined);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		

		float border = 4;
		float buttonWide = Gdx.graphics.getWidth()/5;
		float buttonHeight = 1f;
		
		in.setSize(ScreenSizeHelper.getcmtoPxlX(.6f), ScreenSizeHelper.getcmtoPxlY(.6f));
		out.setSize(ScreenSizeHelper.getcmtoPxlX(.6f), ScreenSizeHelper.getcmtoPxlY(.6f));
		
		in.setPosition(0, Gdx.graphics.getHeight()-in.getHeight());
		out.setPosition(Gdx.graphics.getWidth()-out.getWidth(), Gdx.graphics.getHeight()-out.getHeight());
		
		left.setSize(buttonWide-2*border,  ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		right.setSize(buttonWide-2*border, ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		accel.setSize(buttonWide-2*border, ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		decel.setSize(buttonWide-2*border, ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		
		left.setPosition(border, border);
		right.setPosition(border+buttonWide, border);
		
		accel.setPosition(Gdx.graphics.getWidth()-2*buttonWide+border, border);
		decel.setPosition(Gdx.graphics.getWidth()-buttonWide+border, border);
		
	}

	@Override
	public void createStage(Stage stage) {
		pad.setBounds(10, 10, ScreenSizeHelper.getcmtoPxlX(2), ScreenSizeHelper.getcmtoPxlY(2));
		
		stage.addActor(left);
		stage.addActor(right);
		stage.addActor(accel);
		stage.addActor(decel);
		stage.addActor(in);
		stage.addActor(out);
	}

	@Override
	public void drawOverlay(float delta) {
		
	}

}
