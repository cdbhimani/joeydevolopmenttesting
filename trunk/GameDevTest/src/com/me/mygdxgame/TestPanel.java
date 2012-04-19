package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.me.mygdxgame.graphics.Panel;
import com.me.mygdxgame.spatialPartitioning.Point2D;
import com.me.mygdxgame.spatialPartitioning.Rectangle2D;

public class TestPanel implements ApplicationListener {
	OrthographicCamera cam;
	Rectangle2D bounds = new Rectangle2D(0, 0, 1, 1);
	Point2D worldPos = new Point2D();
	Panel panel;
	
	public void updateCamera(GL10 gl){
		cam.position.set(Gdx.graphics.getWidth()/2+worldPos.x, Gdx.graphics.getHeight()/2+worldPos.y, 0);
		cam.update();
		cam.apply(gl);
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		panel = new Panel(100,100,100,100);
	}

	@Override
	public void resize(int width, int height) {
		cam = new OrthographicCamera(width, height);
		bounds.x2 = width;
		bounds.y2 = height;
	}

	@Override
	public void render() {
		GL10 gl = Gdx.gl10;
		updateCamera(gl);	
		
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gl.glScissor((int)bounds.x1, (int)bounds.y1, (int)bounds.getWidth(), (int)bounds.getHeight());
		panel.paint(gl, cam, bounds);
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

}
