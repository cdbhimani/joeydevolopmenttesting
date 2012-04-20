package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.me.mygdxgame.spatialPartitioning.Point2D;
import com.me.mygdxgame.spatialPartitioning.QuadTree;
import com.me.mygdxgame.spatialPartitioning.QuadTreeViewer;
import com.me.mygdxgame.spatialPartitioning.Rectangle2D;

public class MyGdxGame implements ApplicationListener, InputProcessor{
	int sizeX = 4000;
	int sizeY = 4000;
	
	Point2D worldPos = new Point2D();
	Point2D mousePos = new Point2D();
	Point2D dragOffset = new Point2D();

	Point2D p = new Point2D();
	boolean drag = false;

	float zoom = 3;
	float zoomChange = 1;
	float zoomIncrement = 0.1f;
	boolean zoomChanged = false;
	
    Skin skin;
    Stage stage;
    SpriteBatch batch;
    Actor root;
    
	OrthographicCamera cam;
	QuadTreeViewer view;
	
	@Override
	public void create() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"), Gdx.files.internal("data/uiskin.png"));
		
        final Button windowButton = new TextButton("Single", skin.getStyle(TextButtonStyle.class), "button-sl");
        final Button button = new TextButton("Single", skin.getStyle(TextButtonStyle.class), "button-sl");
        final Window window = new Window("Window", skin.getStyle(WindowStyle.class), "window");
        
        button.x = 10;
        button.y = 10;
        button.width =30;
        button.height =30;
        
        window.x = 0;
        window.y = 0;
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        window.add(windowButton).fill(false, true);
        window.pack();
        stage.addActor(window);
		stage.addActor(button);
        windowButton.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				view.addPoints(1000);
			}
		});
        
        button.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				window.visible = !window.visible;
			}
		});
		
		view = new QuadTreeViewer(new Rectangle2D(0, 0, sizeX, sizeY));
		view.addPoints(10);
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
	
	public void updateCamera(GL10 gl){
		cam.position.set(Gdx.graphics.getWidth()/2+worldPos.x+dragOffset.x, Gdx.graphics.getHeight()/2+worldPos.y+dragOffset.y, 0);
		cam.zoom = zoom;
		cam.update();
		cam.apply(gl);
	}
	public void drawWorld(){
		GLCommon gl = Gdx.gl;
		updateCamera(Gdx.gl10);	
		
		gl.glClearColor(1, 0, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		view.render(gl, cam);
		
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		cam = new OrthographicCamera(width, height);
        stage.setViewport(width, height, false);
	}

	public void setPosition(float x, float y){
		worldPos.x = x;
		worldPos.y = y;
	}
	
	public void translate(float x, float y){
		worldPos.x += x;
		worldPos.y += y;
	}
	
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
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
		drag = true;
		p.x = x;
		p.y = y;
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		drag = false;
		worldPos.x += dragOffset.x;
		worldPos.y += dragOffset.y;
		dragOffset.x = 0;
		dragOffset.y = 0;
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if (drag) {
			dragOffset.x = x - p.x;
			dragOffset.y = y - p.y;
		}
		return true;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
	return true;
	}
	
	
}
