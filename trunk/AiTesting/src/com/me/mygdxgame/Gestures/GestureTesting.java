package com.me.mygdxgame.Gestures;

import java.util.ArrayList;

import javax.print.attribute.standard.MediaSize.Other;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.joey.testing.game.graphics.ConsoleLogger;
import com.joey.testing.game.graphics.ConsoleViewer;

public class GestureTesting implements ApplicationListener{

	GestureDetector gesture;
	ConsoleViewer view;
	ConsoleLogger log;
	OrthographicCamera camera;
	OrthographicCamera textCamera;
	ShapeRenderer render;
	OrthoCamController cam;
	
	@Override
	public void create() {
		
		log = new ConsoleLogger();
		view = new ConsoleViewer(log);
		camera = new OrthographicCamera();
		textCamera = new OrthographicCamera();
		render = new ShapeRenderer();
		 
		gesture = new GestureDetector(log);
		cam = new OrthoCamController(camera);
		

		InputMultiplexer mp = new InputMultiplexer();
		mp.addProcessor(cam);
		mp.addProcessor(gesture);
		mp.addProcessor(log);
		Gdx.input.setInputProcessor(mp);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
		textCamera.viewportHeight = height;
		textCamera.viewportWidth = width;
		textCamera.position.x = width/2;
		textCamera.position.y = height/2;
	}

	@Override
	public void render() {		
		GL10 gl = Gdx.gl10;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		camera.apply(gl);
		
		render.setProjectionMatrix(camera.combined);	
		render.begin(ShapeType.Rectangle);
		render.rect(50, 50, 100, 100);
		render.end();

		textCamera.update();
		textCamera.apply(gl);
		view.draw(textCamera);
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
