package com.joey.chain.old;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class ChainReactionApp implements ApplicationListener  {
	OrthographicCamera cam = new OrthographicCamera();
	ChainReaction chain;
	ShapeRenderer circle;
	ShapeRenderer line;
	
	@Override
	public void create() {
		chain = new ChainReaction(16, 16);
		chain.reset(100);
		circle = new ShapeRenderer();
		line = new ShapeRenderer();
	}

	@Override
	public void resize(int width, int height) {
//		cam.viewportWidth = width;
//		cam.viewportHeight = height;
//		cam.position.x = width / 2;
//		cam.position.y = height / 2;	
		cam = new OrthographicCamera(width, height);
		cam.translate(width / 2, height / 2, 0);
	}

	@Override
	public void render() {
		chain.updateCells();
		cam.update();
		cam.apply(Gdx.gl10);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // #14
		circle.setProjectionMatrix(cam.combined);
		line.setProjectionMatrix(cam.combined);
		float rad = 10;
		float dia = 2*rad+1;
		circle.setColor(Color.RED);
		line.setColor(Color.WHITE);
		
		float x1,y1,x2,y2;
		for(int x= 0; x  < chain.cell.length; x++){
			for(int y = 0; y  < chain.cell[x].length; y++){
				x1 = (x+1)*dia;
				y1 =(y+1)*dia;
				
				circle.begin(ShapeType.FilledCircle);
				circle.filledCircle(x1,y1, rad);
				circle.end();
				

				x2 = x1+rad*MathUtils.cosDeg(chain.cell[x][y].angle);
				y2 = y1+rad*MathUtils.sinDeg(chain.cell[x][y].angle);
				line.begin(ShapeType.Line);
				line.line(x1, y1, x2, y2);
				line.end();
			}
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}

}
