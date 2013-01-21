package com.joey.chain;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.joey.chain.old.ChainReaction;

public class ReactorApp implements ApplicationListener, InputProcessor{

	float RADIUT = 15;
	float DIAMTER = 2*RADIUT;
	OrthographicCamera cam = new OrthographicCamera();
	Reactor reactor;
	ShapeRenderer circle;
	ShapeRenderer line1;
	ShapeRenderer line2;
	
	@Override
	public void create() {
		int sizeX = 15;
		int sizeY = 15;
		
		circle = new ShapeRenderer();
		line1 = new ShapeRenderer();
		line2 = new ShapeRenderer();
		reactor = new Reactor();
		reactor.createBoard(sizeX, sizeY);
		reactor.resetBorad(1);
		
		reactor.board[MathUtils.random(sizeX-1)][MathUtils.random(sizeY-1)].activate();
	}

	@Override
	public void resize(int width, int height) {
		cam = new OrthographicCamera(width, height);
		cam.translate(width / 2, height / 2, 0);
	}

	@Override
	public void render() {
		reactor.update();
		
		cam.update();
		cam.apply(Gdx.gl10);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14
		circle.setProjectionMatrix(cam.combined);
		line1.setProjectionMatrix(cam.combined);
		line2.setProjectionMatrix(cam.combined);
		
		circle.setColor(Color.WHITE);
		line1.setColor(Color.RED);
		line2.setColor(Color.GREEN);
		
		float x1,y1,x2,y2;
		Chain[][] board = reactor.getBoard();
		for(int x= 0; x  < board.length; x++){
			for(int y = 0; y  < board[x].length; y++){
				x1 = (x+1)*DIAMTER;
				y1 =(y+1)*DIAMTER;
				
				circle.begin(ShapeType.FilledCircle);
				circle.filledCircle(x1,y1, RADIUT);
				circle.end();
				

				x2 = x1+RADIUT*MathUtils.cosDeg(board[x][y].getAngle());
				y2 = y1+RADIUT*MathUtils.sinDeg(board[x][y].getAngle());
				line1.begin(ShapeType.Line);
				line1.line(x1, y1, x2, y2);
				line1.end();
				
				x2 = x1+RADIUT*MathUtils.cosDeg(board[x][y].getAngle()+90);
				y2 = y1+RADIUT*MathUtils.sinDeg(board[x][y].getAngle()+90);
				line2.begin(ShapeType.Line);
				line2.line(x1, y1, x2, y2);
				line2.end();
			}
		}
		
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
