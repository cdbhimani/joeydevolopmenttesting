package com.emptyPockets.gui;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.emptyPockets.box2d.Box2DScreen;
import com.emptyPockets.box2d.Car;
import com.emptyPockets.box2d.BulletLauncher;

public class BasicCarTestScreen extends Box2DScreen{
	
	float playerRotationPad = MathUtils.degreesToRadians*30000;
	
	Touchpad pad;
	
	TextButton left;
	TextButton right;
	
	TextButton in;
	TextButton out;
	
	TextButton shoot;
	TextButton accel;
	TextButton decel;
	
	TextButton rotateUsingPhone;
	TextButton rotateScreen;
	
	TextField bulletCount;
	
	
	Vector2 velocityTemp = new Vector2();	
	Car car;
	BulletLauncher gunA;
	BulletLauncher gunB;
	
	long zoomLast = 0;
	long zoomDelay = 50;
	
	boolean useRotate = false;
	
	public BasicCarTestScreen(InputMultiplexer inputProcessor) {
		super(inputProcessor);
		setShowDebug(true);
		in= new TextButton("+",getSkin());
		out = new TextButton("-",getSkin());
		
		left = new TextButton("L",getSkin());
		right = new TextButton("R",getSkin());
		accel = new TextButton("A",getSkin());
		decel = new TextButton("D",getSkin());
		shoot = new TextButton("Shoot", getSkin(),"toggle");
		bulletCount = new TextField("Value", getSkin());
		
		rotateScreen = new TextButton("Fixed", getSkin(), "toggle");
		rotateUsingPhone = new TextButton("Rotate", getSkin(), "toggle");
		
		setClearColor(Color.BLACK);
		
		pad = new Touchpad(5f, getSkin());
		
	}
	
	@Override
	public void createWorld(World world) {	
		createPlayer();
		createObjects(500, 1,5, 800,800);	
	}
	
	@Override
	public boolean scrolled(int amount) {
		zoom(amount);
		return true;
	}
	
	public void zoom(int amount) {
		float scale = 0.03f;
		if(amount >0){
			setWorldScale(getWorldScale()*(1+scale));
		}else{
			setWorldScale(getWorldScale()*(1-scale));
		}
	};

	private void createObjects(int count, float minSize, float maxSize, float wide, float high){
		for(int i = 0; i < count; i++){
			BodyDef bodyDef = new BodyDef();
			bodyDef.type=BodyType.DynamicBody;
		
			float radius = MathUtils.random(minSize, maxSize); 
			float x= MathUtils.random(-wide,wide);
			float y= MathUtils.random(-high,high);
			bodyDef.position.set(x,y);
			
			Body body = getWorld().createBody(bodyDef);
			body.setLinearDamping(1);
			body.setAngularDamping(1f);
			
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.density = 0.8f;
			
		
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
	
	private void createPlayer() {
		car = new Car(getWorld());
		gunA = new BulletLauncher(car.getBody(), new Vector2(-.5f,11), new Vector2(0,1), 1000, 100);
		gunB = new BulletLauncher(car.getBody(), new Vector2(+.5f,11), new Vector2(0,1), 1000, 100);
	}

	public void removeInvaidBullets(){
		int bulletCount = 0;
		Iterator<Body> bodies = getWorld().getBodies();
		while(bodies.hasNext()){
			Body body = bodies.next();
			if(body.getUserData() instanceof BulletLauncher){
				bulletCount++;
				BulletLauncher source= (BulletLauncher) body.getUserData();
				//Remove Slow bullets
				if(body.getLinearVelocity().len2() < source.getBulledMinSpeed2()
				  || body.getPosition().dst2(car.getBody().getPosition()) > source.getBulletMaxDistance2()
						){
					removeBody(body);
				}
			}
		}
		this.bulletCount.setText(""+bulletCount);
	}
	@Override
	public void updateLogic(float delta) {
		removeInvaidBullets();
		float x = 0;
		float y = 0;
		
		if(rotateUsingPhone.isChecked()){
			float rot = Gdx.input.getAccelerometerY();
			if(rot > 1){
				x=rot;
			}
			if(rot < -1){
				x = rot;
			}
			x/=10;
		}
		if(left.isPressed() || Gdx.input.isKeyPressed(Input.Keys.A)){
			x=-1;
		}
		if(right.isPressed()|| Gdx.input.isKeyPressed(Input.Keys.D)){
			x=1;
		}
		
		if(accel.isPressed()|| Gdx.input.isKeyPressed(Input.Keys.W)){
			y=1;
		}
		if(decel.isPressed()|| Gdx.input.isKeyPressed(Input.Keys.S)){
			y=-1;
		}
		
		int zoom = 0;
		if(in.isPressed() || Gdx.input.isKeyPressed(Input.Keys.PLUS)){
			zoom = 1;
		}else if(out.isPressed() || Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
			zoom = -1;
		}

		if((zoom != 0) && (System.currentTimeMillis() > zoomLast+zoomDelay)){
			zoom(zoom);
			zoomLast = System.currentTimeMillis();
		}

		if(shoot.isChecked()){
			if(MathUtils.randomBoolean()){
				gunA.shoot();
			}else{
				gunB.shoot();
			}
		}
		car.update(x, y);
		
		super.updateLogic(delta);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		float border = 4;
		float buttonWide = Gdx.graphics.getWidth()/5;
		float buttonHeight = 0.6f;
		
		in.setSize(ScreenSizeHelper.getcmtoPxlX(.6f), ScreenSizeHelper.getcmtoPxlY(.6f));
		out.setSize(ScreenSizeHelper.getcmtoPxlX(.6f), ScreenSizeHelper.getcmtoPxlY(.6f));	
		left.setSize(buttonWide-2*border,  ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		right.setSize(buttonWide-2*border, ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		accel.setSize(buttonWide-2*border, ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		decel.setSize(buttonWide-2*border, ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		shoot.setSize(2*buttonWide-2*border, ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		rotateUsingPhone.setSize(buttonWide-2*border, ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		rotateScreen.setSize(buttonWide-2*border, ScreenSizeHelper.getcmtoPxlY(buttonHeight));
		
		
		bulletCount.setPosition(border, Gdx.graphics.getHeight()/2);
		left.setPosition(border, border);
		right.setPosition(border+buttonWide, border);
		accel.setPosition(Gdx.graphics.getWidth()-2*buttonWide+border, border);
		decel.setPosition(Gdx.graphics.getWidth()-buttonWide+border, border);
		shoot.setPosition(accel.getX(), accel.getHeight()+border);
		in.setPosition(0, Gdx.graphics.getHeight()-in.getHeight());
		out.setPosition(Gdx.graphics.getWidth()-out.getWidth(), Gdx.graphics.getHeight()-out.getHeight());

		rotateUsingPhone.setPosition(Gdx.graphics.getWidth()/2-buttonWide, Gdx.graphics.getHeight()-rotateUsingPhone.getHeight());
		rotateScreen.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()-rotateScreen.getHeight());
		
		in.invalidate();
		out.invalidate();
		left.invalidate();
		right.invalidate();
		accel.invalidate();
		shoot.invalidate();
		decel.invalidate();
		rotateUsingPhone.invalidate();
		rotateScreen.invalidate();
		bulletCount.invalidate();
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
		stage.addActor(rotateUsingPhone);
		stage.addActor(rotateScreen);
		stage.addActor(shoot);
		stage.addActor(bulletCount);
	}

	@Override
	public void updateWorldCamera(OrthographicCamera worldCamera) {
		super.updateWorldCamera(worldCamera);
		
		if(rotateScreen.isChecked()){
			Vector2 v = car.getBody().getWorldVector(new Vector2(0,1)).cpy().nor();
			worldCamera.up.set(v.x, v.y, 0);
		}
		worldCamera.position.x = car.getBody().getPosition().x;
		worldCamera.position.y = car.getBody().getPosition().y;
	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub
		
	}
}
