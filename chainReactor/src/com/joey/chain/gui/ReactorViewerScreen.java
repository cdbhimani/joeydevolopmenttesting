package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.joey.chain.ReactorApp;
import com.joey.chain.model.Chain;
import com.joey.chain.model.Chain.ChainState;
import com.joey.chain.model.Reactor;

public class ReactorViewerScreen extends GameScreen {

	long lastScore = 0;
	int sizeX = 15;
	int sizeY = 15;
	float radius =0;
	float diameter = 2*radius;
	
	Vector3 click = new Vector3();
	
	OrthographicCamera cam = new OrthographicCamera();
	Reactor reactor;
	
	Texture texture;
	TextureRegion activeRegion;
	TextureRegion disabledRegion;
	
	SpriteBatch batch;
	BitmapFont font;

	public ReactorViewerScreen(ReactorApp game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float delta) {
		long start = 0;
		start = System.currentTimeMillis();
		updateApp();
		long update = System.currentTimeMillis()-start;
		
		start = System.currentTimeMillis();
		cam.update();
		cam.apply(Gdx.gl10);
		Gdx.gl.glClearColor(1, 1,1,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14
		
		renderGame();
		long render = System.currentTimeMillis()-start;
		renderOverlay(update,render);
	}

	public void updateApp(){
		reactor.update();
	}
	
	public void initializeRender(){
		cam.update();
		cam.apply(Gdx.gl10);
		Gdx.gl.glClearColor(1, 1,1,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14
	}
	
	public void renderGame(){		
		float x1,y1,x2,y2;
		Chain[][] board = reactor.getBoard();
		
		for(int x= 0; x  < board.length; x++){
			for(int y = 0; y  < board[x].length; y++){
				x1 = (x+1)*diameter;
				y1 =(y+1)*diameter;
				batch.begin();
				if(board[x][y].getState()!=ChainState.stopped){
					batch.draw(activeRegion,x1-radius,y1-radius, radius,radius, diameter,diameter,1,1,board[x][y].getAngle());
				}else{
					batch.draw(disabledRegion,x1-radius,y1-radius, radius,radius, diameter,diameter,1,1,board[x][y].getAngle());
				}
				batch.end();
			}
		}
	}
	
	public void renderOverlay(long update, long render){
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		font.setColor(Color.RED);
		
		float x = Gdx.graphics.getWidth()*.7f;
		font.draw(batch, "Update : "+update, x, Gdx.graphics.getHeight()-10);
		font.draw(batch, "Render : "+render, x, Gdx.graphics.getHeight()-30);
		font.draw(batch, "Score  : "+reactor.getScore(), x, Gdx.graphics.getHeight()-50);
		batch.end();
	
	}	
	
	@Override
	public void resize(int width, int height) {
		cam = new OrthographicCamera(width, height);
		cam.translate(width / 2, height / 2, 0);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		texture = new Texture(Gdx.files.internal("data/cell.png"));
		activeRegion  = new TextureRegion(texture, 0,0,256,256);
		disabledRegion  = new TextureRegion(texture, 0,256,256,256);
		
		reactor = new Reactor();
		reactor.setSize(sizeX, sizeY);
		reactor.resetBorad(1);
	
		float rx =Gdx.graphics.getWidth()/(2f*(sizeX+1));
		float ry =Gdx.graphics.getHeight()/(2f*(sizeY+1)); 
		setRadius(Math.min(rx, ry));
		Gdx.input.setInputProcessor(new GestureDetector(this));
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
		this.diameter = 2*radius;
	}
	
	@Override
	public void dispose() {
		activeRegion = null;
		disabledRegion = null;
		
		texture.dispose();
		texture = null;
		
		batch.dispose();
		batch = null;
		
		font.dispose();
		font = null;
		
		reactor.dispose();
		reactor = null;
	}
	
	public Chain getSelectedChain(Vector3 click){
		int x = Math.round((click.x-diameter)/diameter);
		int y = Math.round((click.y-diameter)/diameter);
		if(x < reactor.getBoard().length && y < reactor.getBoard()[x].length){
			return reactor.getBoard()[x][y];
		}
		return null;
	}

	@Override
	public boolean tap(int x, int y, int count) {
		if(count > 1){
			reactor.resetBorad(System.currentTimeMillis());
			return true;
		}
		click.x = x;
		click.y = y;
		cam.unproject(click);
		Chain chain = getSelectedChain(click);
		if(chain != null){
			chain.activate();
			reactor.activate();
		}
		return true;
	}

	

}
