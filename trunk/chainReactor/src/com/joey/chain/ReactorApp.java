package com.joey.chain;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.joey.chain.Chain.ChainState;
import com.joey.chain.Reactor.ReactorState;

public class ReactorApp implements ApplicationListener, GestureListener{

	long lastScore = 0;
	int sizeX = 15;
	int sizeY = 15;
	float radius =0;
	float diameter = 2*radius;
	
	Vector3 click = new Vector3();
	
	OrthographicCamera cam = new OrthographicCamera();
	Reactor reactor;
	ShapeRenderer staticCircle;
	ShapeRenderer movingCircle;
	ShapeRenderer line1;
	ShapeRenderer line2;
	
	SpriteBatch batch;
	BitmapFont font;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		staticCircle = new ShapeRenderer();
		movingCircle = new ShapeRenderer();
		line1 = new ShapeRenderer();
		line2 = new ShapeRenderer();
		reactor = new Reactor();
		reactor.createBoard(sizeX, sizeY);
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
	public void resize(int width, int height) {
		cam = new OrthographicCamera(width, height);
		cam.translate(width / 2, height / 2, 0);
	}

	public void updateApp(){
		reactor.update();
	}
	
	public void renderApp(){
		staticCircle.setProjectionMatrix(cam.combined);
		movingCircle.setProjectionMatrix(cam.combined);
		line1.setProjectionMatrix(cam.combined);
		line2.setProjectionMatrix(cam.combined);
		
		staticCircle.setColor(Color.WHITE);
		movingCircle.setColor(Color.GRAY);
		line1.setColor(Color.RED);
		line2.setColor(Color.GREEN);
		
		float x1,y1,x2,y2;
		Chain[][] board = reactor.getBoard();
		for(int x= 0; x  < board.length; x++){
			for(int y = 0; y  < board[x].length; y++){
				x1 = (x+1)*diameter;
				y1 =(y+1)*diameter;
				
				if(board[x][y].getState() == ChainState.stopped){
					staticCircle.begin(ShapeType.FilledCircle);
					staticCircle.filledCircle(x1,y1, radius);
					staticCircle.end();
				}else{
					movingCircle.begin(ShapeType.FilledCircle);
					movingCircle.filledCircle(x1,y1, radius);
					movingCircle.end();	
				}
				
				

				x2 = x1+radius*MathUtils.cosDeg(board[x][y].getAngle());
				y2 = y1+radius*MathUtils.sinDeg(board[x][y].getAngle());
				line1.begin(ShapeType.Line);
				line1.line(x1, y1, x2, y2);
				line1.end();
				
				x2 = x1+radius*MathUtils.cosDeg(board[x][y].getAngle()+90);
				y2 = y1+radius*MathUtils.sinDeg(board[x][y].getAngle()+90);
				line2.begin(ShapeType.Line);
				line2.line(x1, y1, x2, y2);
				line2.end();
			}
		}
	}
	@Override
	public void render() {
		long start = 0;
		
		start = System.currentTimeMillis();
		updateApp();
		long update = System.currentTimeMillis()-start;
		
		start = System.currentTimeMillis();
		cam.update();
		cam.apply(Gdx.gl10);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14
		
		renderApp();
		long render = System.currentTimeMillis()-start;
		renderOverlay(update,render);
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

	public Chain getSelectedChain(Vector3 click){
		int x = Math.round((click.x-diameter)/diameter);
		int y = Math.round((click.y-diameter)/diameter);
		if(x < reactor.board.length && y < reactor.board[x].length){
			return reactor.board[x][y];
		}
		return null;
	}
	@Override
	public boolean touchDown(int x, int y, int pointern) {
		return false;
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

	@Override
	public boolean fling(float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(int x, int y, int deltaX, int deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialFirstPointer,
			Vector2 initialSecondPointer, Vector2 firstPointer,
			Vector2 secondPointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

}
