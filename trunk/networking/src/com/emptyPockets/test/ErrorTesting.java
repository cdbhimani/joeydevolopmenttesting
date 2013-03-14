package com.emptyPockets.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptyPockets.box2d.gui.Box2DScreen;
import com.emptyPockets.utils.OrthoCamController;

public class ErrorTesting extends Box2DScreen{
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRender;
	OrthoCamController control;
	VertexTool vertex;
	
	public ErrorTesting(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		setClearColor(Color.BLACK);
//		setShowDebug(true);
		control = new OrthoCamController(getStageCamera());
		vertex = new VertexTool();
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
		vertex.create();
	}

	@Override
	public void hide() {
		super.hide();
		spriteBatch.dispose();
		spriteBatch = null;
		shapeRender.dispose();
		shapeRender = null;
		vertex.dispose();
		vertex = null;
	}
	
	@Override
	public void createWorld(World world) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createStage(Stage stage) {
		// TODO Auto-generated method stub
		
	}
	
	int laserCount = 1000;
	int sizeX = 8000;
	int sizeY = 6000;
	float maxSpeed =1000;
	Color color[];
	float angle[];
	Rectangle bounds = new Rectangle(-sizeX/2,-sizeY/2, sizeX, sizeY);
	Vector2 pos[];
	Vector2 vel[];
	{
		angle = new float[laserCount];
		color = new Color[laserCount];
		vel = new Vector2[laserCount];
		pos = new Vector2[laserCount];
		
		for(int i = 0; i < laserCount; i++){
			float speed = MathUtils.random(maxSpeed);
			color[i]=new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), MathUtils.random(0.5f,1));
			angle[i]= MathUtils.random(360);
//			angle[i]= 90;
			vel[i] = new Vector2(speed*MathUtils.cosDeg(angle[i]), speed*MathUtils.sinDeg(angle[i]));
			pos[i] = new Vector2(MathUtils.random(bounds.x,bounds.x+bounds.width),MathUtils.random(bounds.y, bounds.y+bounds.height));
		}
	}
	@Override
	public void drawScreen(float delta) {
		spriteBatch.setProjectionMatrix(getStageCamera().combined);
		shapeRender.setProjectionMatrix(getStageCamera().combined);
		
		shapeRender.begin(ShapeType.Rectangle);
		shapeRender.setColor(Color.RED);
		shapeRender.rect(bounds.x, bounds.y,  bounds.width, bounds.height);
		shapeRender.end();
		
		spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		spriteBatch.begin();
		for(int i = 0;i < laserCount; i++){
			pos[i].x += vel[i].x*delta;
			pos[i].y += vel[i].y*delta;
			if(pos[i].x > bounds.x+bounds.width){
				pos[i].x = bounds.x;
			}
			if(pos[i].x < bounds.x){
				pos[i].x = bounds.x+bounds.width;
			}
			
			if(pos[i].y > bounds.y+bounds.height){
				pos[i].y = bounds.y;
			}
			if(pos[i].y < bounds.y){
				pos[i].y = bounds.y+bounds.height;
			}
			
			color[i].a = MathUtils.random(0.1f,1f);
			float back = color[i].toFloatBits();
			color[i].a = MathUtils.random(0.6f,1f);
			float overlay = color[i].toFloatBits();
			vertex.draw(spriteBatch,pos[i], angle[i],back,overlay);
		}
		spriteBatch.end();
		
		
		
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		Gdx.gl.glEnable(GL10.GL_LINE_SMOOTH);
		Gdx.gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
		shapeRender.begin(ShapeType.Line);
		
		float high = vertex.getSizeY();
		float wide = vertex.getSizeX();
		for(int i = 0;i < laserCount; i++){
			float x= pos[i].x+high*MathUtils.cosDeg(angle[i]);
			float y= pos[i].y+high*MathUtils.sinDeg(angle[i]);
			
			for(int j = 0;j < 3;j++){
				color[i].a = MathUtils.random(0.1f,.1f);
				shapeRender.setColor(color[i]);
				Tree.drawLightning(shapeRender, pos[i].x, pos[i].y, x, y, wide, wide/100);
			}
		}
		shapeRender.end();
		
	}

}

