package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.joey.chain.ReactorApp;
import com.joey.chain.games.cellRotateChainGame.Chain;
import com.joey.chain.games.cellRotateChainGame.Chain.ChainState;
import com.joey.chain.games.cellRotateChainGame.RotateCellEngine;

public class CellRotateChainScreen extends GameScreen {

	long lastScore = 0;
	int sizeX = 11;
	int sizeY = 16;
	float radius =0;
	float diameter = 2*radius;
	Color bg = new Color();
	Vector3 click = new Vector3();
	
	RotateCellEngine engine;
	
	Texture texture;
	TextureRegion activeRegion;
	TextureRegion disabledRegion;
	ShapeRenderer shape;
	
	SpriteBatch batch;
	BitmapFont font;

	public CellRotateChainScreen(ReactorApp game) {
		super(game);
		bg.a = 1;
		bg.r = 215/255f;
		bg.g = 238/255f;
		bg.b = 244/255f;
		setClearColor(bg);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		long start = 0;
		start = System.currentTimeMillis();
		updateApp();
		long update = System.currentTimeMillis()-start;
		
		start = System.currentTimeMillis();
		
		renderGame();
		long render = System.currentTimeMillis()-start;
		renderOverlay(update,render);
	}

	public void updateApp(){
		engine.update();
	}
	
	public void renderGame(){		
		float x1,y1,wide,high;
		Chain[][] board = engine.getBoard();
		
		x1 = radius/2;
		y1 = radius/2;
		wide = diameter*(board.length+.5f);
		high = diameter*(board[0].length+.5f);
		
		
		shape.setProjectionMatrix(cam.combined);
		shape.setColor(bg);
		shape.begin(ShapeType.FilledRectangle);
		shape.filledRect(x1, y1, wide, high);
		shape.end();
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		for(int x= 0; x  < board.length; x++){
			for(int y = 0; y  < board[x].length; y++){
				x1 = (x+1)*diameter;
				y1 =(y+1)*diameter;
				
				if(board[x][y].getState()!=ChainState.stopped){
					batch.draw(activeRegion,x1-radius,y1-radius, radius,radius, diameter,diameter,1,1,board[x][y].getAngle());
				}else{
					batch.draw(disabledRegion,x1-radius,y1-radius, radius,radius, diameter,diameter,1,1,board[x][y].getAngle());
				}
				
			}
		}
		batch.end();
	}
	
	public void renderOverlay(long update, long render){
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		font.setColor(Color.RED);
		
		float x = Gdx.graphics.getWidth()*.7f;
		font.draw(batch, "Update : "+update, x, Gdx.graphics.getHeight()-10);
		font.draw(batch, "Render : "+render, x, Gdx.graphics.getHeight()-30);
		font.draw(batch, "Score  : "+engine.getScore(), x, Gdx.graphics.getHeight()-50);
		batch.end();
	
	}	
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
		this.diameter = 2*radius;
	}
	
	@Override
	public void show() {
		super.show();
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		texture = new Texture(Gdx.files.internal("data/cell.png"));
		activeRegion  = new TextureRegion(texture, 0,0,256,256);
		disabledRegion  = new TextureRegion(texture, 0,256,256,256);
		shape = new ShapeRenderer();
		engine = new RotateCellEngine();
		engine.setSize(sizeX, sizeY);
		engine.resetBorad(1);
	
		float rx =Gdx.graphics.getWidth()/(2f*(sizeX+1));
		float ry =Gdx.graphics.getHeight()/(2f*(sizeY+1)); 
		setRadius(Math.min(rx, ry));
		Gdx.input.setInputProcessor(new GestureDetector(this));
	}
	
	@Override
	public void hide() {
		super.hide();
		activeRegion = null;
		disabledRegion = null;
		
		texture.dispose();
		texture = null;
		
		batch.dispose();
		batch = null;
		
		font.dispose();
		font = null;
		
		engine.dispose();
		engine = null;
	}

	@Override
	public boolean tap(int x, int y, int count) {
		if(count > 1){
			engine.resetBorad(System.currentTimeMillis());
			return true;
		}
		click.x = x;
		click.y = y;
		cam.unproject(click);
		

		int xPos = Math.round((click.x-diameter)/diameter);
		int yPos = Math.round((click.y-diameter)/diameter);
		
		engine.tap(xPos, yPos);
		return true;
	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawOverlay(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLogic(float delta) {
		// TODO Auto-generated method stub
		
	}

	

}
