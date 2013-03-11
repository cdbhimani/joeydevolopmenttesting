package com.emptyPockets.box2d.gui.bodyEditor;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.box2d.gui.Box2DScreen;
import com.emptyPockets.box2d.shape.BodyEditor;
import com.emptyPockets.gui.ScreenSizeHelper;

public class BodyEditorScreen extends Box2DScreen{
	Button createBody;
	BodyEditor bodyEditor;
	ShapeRenderer shapeRender;
	Button shapeManagerButton;
	float buttonSize = .6f; 
	
	public BodyEditorScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		shapeRender  = new ShapeRenderer();
		setClearColor(Color.DARK_GRAY);
		setShowDebug(true);
		bodyEditor = new BodyEditor(getBox2DWorldCamera());
		
	}

	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		bodyEditor.attach(input);
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		bodyEditor.detatch(input);
	}
	
	public void createPanel(Stage stage){
		bodyEditor.setBackground("default-rect");		
		bodyEditor.setVisible(false);
		shapeManagerButton=bodyEditor.getShowPanelButton();
		createBody = new TextButton("Create", getSkin());
		
		stage.addActor(bodyEditor);
		stage.addActor(shapeManagerButton);
		stage.addActor(createBody);
		setButtonSize(buttonSize);
		
		shapeManagerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				bodyEditor.showControlPanel();	
				shapeManagerButton.setVisible(false);
			}
		});
		
		createBody.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				bodyEditor.createBody(getWorld());
			}});
	}
	
	
	@Override
	public void createStage(Stage stage) {
		createPanel(stage);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		setButtonSize(buttonSize);
		bodyEditor.setBounds(0, 0, bodyEditor.getWidth(), Gdx.graphics.getHeight());
		bodyEditor.invalidateHierarchy();
	}
	
	public void setButtonSize(float size){
		float buttonWide = ScreenSizeHelper.getcmtoPxlX(size);
		float buttonHigh = ScreenSizeHelper.getcmtoPxlY(size);
		
		shapeManagerButton.setSize(buttonWide, buttonHigh);
		shapeManagerButton.setPosition(0, 0);
		bodyEditor.setButtonSize(size);
		
		createBody.setSize(buttonWide, buttonHigh);
		createBody.setPosition(Gdx.graphics.getWidth()-createBody.getWidth(), 0);
	}
	
	@Override
	public void drawStage(float delta) {
		super.drawStage(delta);
//		Table.drawDebug(getStage());
	}

	@Override
	public void drawScreen(float delta) {
		shapeRender.setProjectionMatrix(getBox2DWorldCamera().combined);
		bodyEditor.drawShapes(shapeRender);
	
		Iterator<Body> it = getWorld().getBodies();
		
		float x1,x2,y1,y2;
		shapeRender.setColor(Color.CYAN);
		while(it.hasNext()){
			Body body = it.next();
			x1 = body.getPosition().x;
			y1 = body.getPosition().y;
			
			x2 = body.getPosition().x+body.getLinearVelocity().x;
			y2 = body.getPosition().y+body.getLinearVelocity().y;
			shapeRender.begin(ShapeType.Line);
			shapeRender.line(x1, y1, x2, y2);
			shapeRender.end();
			
			shapeRender.begin(ShapeType.Circle);
			shapeRender.circle(body.getPosition().x, body.getPosition().y, 3);
			shapeRender.end();
		}
		
	}

	@Override
	public void createWorld(World world) {
		world.setGravity(new Vector2(0,-10));
		
		BodyDef floorDef = new BodyDef();
		floorDef.position.set(0, -100);
		floorDef.type=BodyType.StaticBody;
		Body floorBody = world.createBody(floorDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1000, 10);
		floorBody.createFixture(shape, 100);
	}

}
