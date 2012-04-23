package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.me.mygdxgame.spatialPartitioning.Entity2D;
import com.me.mygdxgame.spatialPartitioning.QuadTree;
import com.me.mygdxgame.spatialPartitioning.QuadTreeViewer;
import com.me.mygdxgame.spatialPartitioning.Rectangle2D;

public class MyGdxGame implements ApplicationListener, InputProcessor{
	int sizeX = 6000;
	int sizeY = 6000;
	
	Entity2D worldPos = new Entity2D();
	Entity2D startMouse = new Entity2D();
	Entity2D dragOffset = new Entity2D();

	boolean drag = false;

	float zoom =1;
	float zoomChange = 1;
	float zoomIncrement = 0.1f;
	boolean zoomChanged = false;
	
    Skin skin;
    Stage stage;
    SpriteBatch batch;
    Actor root;
    
	OrthographicCamera cam;
	QuadTreeViewer view;

	
	TextField field;
	@Override
	public void create() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        batch = new SpriteBatch();
        
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(stage);
        multi.addProcessor(this);
        
        Gdx.input.setInputProcessor(multi);
        
        
        skin = new Skin(Gdx.files.internal("data/uiskin.json"), Gdx.files.internal("data/uiskin.png"));
		
        final Button windowButton = new TextButton("Single", skin.getStyle(TextButtonStyle.class), "button-sl");
        final Button button = new TextButton("Single", skin.getStyle(TextButtonStyle.class), "button-sl");
        field= new TextField("Count", "asdf", skin.getStyle(TextFieldStyle.class), "styles2");
        final Window window = new Window("Window", skin.getStyle(WindowStyle.class), "window");
        
        button.x = 10;
        button.y = 10;
        button.width =30;
        button.height =30;
        
        window.x = 50;
        window.y = 50;
        
        window.width=200;
        window.height=200;
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        window.add(windowButton).fill(true, false);
        window.row().fill().expandX();
        window.add(field).fill(true, false);
        
        
        Table t = new Table(skin);
        t.row().fill().expandX();
        t.add("hello");
        
        t.x = 0;
        t.y = 0;
        t.width = 100;
        t.height = 100;
        
        stage.addActor(t);
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
		
		view = new QuadTreeViewer(new Rectangle2D(-sizeX, -sizeY, sizeX, sizeY));
		view.addPoints(100);
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
		cam.position.set(worldPos.x+dragOffset.x, worldPos.y+dragOffset.y, 0);
		cam.zoom = zoom;
		cam.update();
		cam.apply(gl);
	}
	public void drawWorld(){
		GLCommon gl = Gdx.gl;
		updateCamera(Gdx.gl10);	
		
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Vector3 p1 = new Vector3(0,0,0);
		Vector3 p2 = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),0);
		
		cam.unproject(p1);
		cam.unproject(p2);
		
		Rectangle2D drawRegion = new Rectangle2D(p1.x, p2.y, p2.x, p1.y);
		view.render(gl, cam, drawRegion);
		
		
		field.setText(""+view.tree.points.size());
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
		startMouse.x = x;
		startMouse.y = y;
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

	public static void MouseToCamera(float x, float y, Camera cam, Entity2D p){
		Vector3 v = new Vector3(x, y, 0);
		cam.unproject(v);
		p.x = v.x;
		p.y = v.y;	
	}
	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		Entity2D currentMouseTransformed = new Entity2D();
		Entity2D orignalMouseTransformed = new Entity2D();
		MouseToCamera(startMouse.x, startMouse.y, cam, orignalMouseTransformed);
		MouseToCamera(x, y, cam, currentMouseTransformed);
		if (drag) {
			dragOffset.x = orignalMouseTransformed.x-currentMouseTransformed.x ;
			dragOffset.y = orignalMouseTransformed.y-currentMouseTransformed.y ;
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
		zoom=zoom*(amount>0?1+zoomIncrement:1-zoomIncrement);
	return true;
	}
	
	
}
