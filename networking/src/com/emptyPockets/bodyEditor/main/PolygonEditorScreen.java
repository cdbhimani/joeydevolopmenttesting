package com.emptyPockets.bodyEditor.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptyPockets.bodyEditor.entity.PolygonEntity;
import com.emptyPockets.gui.StageScreen;
import com.emptyPockets.utils.OrthoCamController;

public class PolygonEditorScreen extends StageScreen {
	PolygonEntity entity;
	OrthographicCamera cam;
	OrthoCamController camControl;
	ShapeRenderer shape;
	
	//Temp for camera 
	Vector3 _tmpCam2MouseVec = new Vector3();
	
	
	public PolygonEditorScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		entity = new PolygonEntity();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camControl= new OrthoCamController(cam);
		setClearColor(Color.DARK_GRAY);
	}

	@Override
	public void createStage(Stage stage) {
	}

	@Override
	public void show() {
		super.show();
		shape = new ShapeRenderer();
	}
	
	@Override
	public void hide() {
		super.hide();
		shape.dispose();
		shape = null;
	}
	
	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		input.addProcessor(camControl);		
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(camControl);		
	}
	
	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportHeight = height;
	}

	@Override
	public void drawBackground(float delta) {

	}

	@Override
	public void drawScreen(float delta) {
		cam.update();
		shape.setProjectionMatrix(cam.combined);
		
		//Draw Nodes
		float nodeSize = getMouseDistance();
		shape.begin(ShapeType.Rectangle);
		shape.setColor(Color.RED);
		for(Vector2 p : entity.getPolygon()){
			shape.rect(p.x-nodeSize, p.y-nodeSize, 2*nodeSize, 2*nodeSize);
		}
		shape.end();
		
		//Draw Lines
		Vector2 p1;
		Vector2 p2;
		shape.begin(ShapeType.Line);
		shape.setColor(Color.WHITE);
		for(int i = 1; i < entity.getPolygon().size(); i++){
			p1 = entity.getPolygon().get(i-1);
			p2 = entity.getPolygon().get(i);
			shape.line(p1.x, p1.y, p2.x, p2.y);
		}
		//Close Shape
		if(entity.getPolygon().size() > 2){
			p1 = entity.getPolygon().get(entity.getPolygon().size()-1);
			p2 = entity.getPolygon().get(0);
			shape.line(p1.x, p1.y, p2.x, p2.y);
		}
		shape.end();
		
		
	}

	public void camToPanel(float x, float y, Vector2 vec){
		synchronized (_tmpCam2MouseVec) {
			_tmpCam2MouseVec.set(x,y,0);
			cam.unproject(_tmpCam2MouseVec);
			vec.x = _tmpCam2MouseVec.x;
			vec.y = _tmpCam2MouseVec.y;
		}
	}
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		Vector2 mouse = new Vector2();
		camToPanel(x, y, mouse);
		float mouseDistance = getMouseDistance();
		float mouseDistance2 = mouseDistance*mouseDistance;
		for(Vector2 p : entity.getPolygon()){
			if(p.dst2(mouse) < mouseDistance2){
				return true;
			}
		}
		return false;
	}
	
	public float getMouseDistance(){
		return 5;
	}
	
	@Override
	public boolean tap(float x, float y, int count, int button) {
		synchronized (entity) {
			Vector2 v = new Vector2();
			camToPanel(x, y, v);
			entity.getPolygon().add(v);
		}
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
		
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}
	@Override
	public void drawOverlay(float delta) {

	}

	@Override
	public void drawStage(float delta) {
	}
}
