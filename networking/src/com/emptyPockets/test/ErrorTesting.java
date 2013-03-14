package com.emptyPockets.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
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
		control = new OrthoCamController(getBox2DWorldCamera());
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
	
	int laserCount = 10;
	int sizeX = 1000;
	int sizeY = 1000;
	Color color[];
	float angle[];
	float angleVel[];
	Vector2 pos[];
	{
		
		angle = new float[laserCount];
		color = new Color[laserCount];
		angleVel = new float[laserCount];
		pos = new Vector2[laserCount];
		
		for(int i = 0; i < laserCount; i++){
			color[i]=new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), MathUtils.random(0.5f,1));
			angle[i]= MathUtils.random(360);
			angleVel[i]=MathUtils.random(0.5f,2);
			if(MathUtils.randomBoolean()){
				angleVel[i]*=-1;
			}
			pos[i] = new Vector2(MathUtils.random(-sizeX,sizeX),MathUtils.random(-sizeY,sizeY));
		}
	}
	@Override
	public void drawScreen(float delta) {
		spriteBatch.setProjectionMatrix(getBox2DWorldCamera().combined);
		shapeRender.setProjectionMatrix(getBox2DWorldCamera().combined);
		
		spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		spriteBatch.begin();
		for(int i = 0;i < laserCount; i++){
			color[i].a = MathUtils.random(0.1f,1f);
			float back = color[i].toFloatBits();
			color[i].a = MathUtils.random(0.6f,1f);
			float overlay = color[i].toFloatBits();
			vertex.draw(spriteBatch,pos[i], angle[i]-90,back,overlay);
			angle[i]+=angleVel[i];
		}
		spriteBatch.end();
		
		
		
		shapeRender.begin(ShapeType.Line);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		float high = vertex.getSizeY();
		float wide = vertex.getSizeY();
		for(int i = 0;i < laserCount; i++){
			float x= pos[i].x+high*MathUtils.cosDeg(angle[i]);
			float y= pos[i].x+high*MathUtils.sinDeg(angle[i]);
			
			for(int j = 0;j < 3;j++){
			color[i].a = MathUtils.random(0.1f,1f);
			shapeRender.setColor(color[i]);
			Tree.drawLightning(shapeRender, pos[i].x, pos[i].y, x, y, wide/4, wide/100);
			}
		}
		shapeRender.end();
		
	}

}

