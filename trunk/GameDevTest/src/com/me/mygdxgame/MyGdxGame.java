package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.me.mygdxgame.spatialPartitioning.QuadTree;
import com.me.mygdxgame.spatialPartitioning.QuadTreeViewer;
import com.me.mygdxgame.spatialPartitioning.Rectangle2D;

public class MyGdxGame implements ApplicationListener {
	int sizeX = 300;
	int sizeY = 300;
	
	Camera cam;
	QuadTreeViewer view;
	
	
	@Override
	public void create() {
		view = new QuadTreeViewer(new Rectangle2D(0, 0, sizeX, sizeY));
		view.addPoints(500);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		updateWorld();
		drawWorld();
	}
	
	
	public void updateWorld(){
		view.updatePoints();
		view.rebuildTree();
	}
	
	public void updateCamera(){
		//Update Camera
		cam.position.x = -Gdx.graphics.getWidth()*2;
		cam.position.y = -Gdx.graphics.getHeight()/2;
		cam.update();
	}
	public void drawWorld(){
		updateCamera();
		
		GLCommon gl = Gdx.gl;	
		gl.glClearColor(1, 0, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		view.render(gl, cam);
	}

	@Override
	public void resize(int width, int height) {
		cam = new OrthographicCamera(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
