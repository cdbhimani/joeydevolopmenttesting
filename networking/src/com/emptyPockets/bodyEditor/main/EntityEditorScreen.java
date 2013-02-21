package com.emptyPockets.bodyEditor.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.emptyPockets.bodyEditor.entity.PolygonEntity;
import com.emptyPockets.bodyEditor.main.controls.EntityControler;
import com.emptyPockets.gui.StageScreen;
import com.emptyPockets.utils.OrthoCamController;

public class EntityEditorScreen extends StageScreen {
	private PolygonEntity entity;
	OrthographicCamera cam;
	OrthoCamController camControl;
	ShapeRenderer shape;
	EntityControler currentControler;
	
	//Temp for camera 
	Vector3 _tmpCam2MouseVec = new Vector3();
	
	
	public EntityEditorScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		setEntity(new PolygonEntity());
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camControl= new OrthoCamController(cam);
		setClearColor(Color.DARK_GRAY);
	}
	
	public float getMouseDistance(){
		return 5;
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
		for(Vector2 p : getEntity().getPolygon()){
			shape.rect(p.x-nodeSize, p.y-nodeSize, 2*nodeSize, 2*nodeSize);
		}
		shape.end();
		
		//Draw Lines
		Vector2 p1;
		Vector2 p2;
		shape.begin(ShapeType.Line);
		shape.setColor(Color.WHITE);
		for(int i = 1; i < getEntity().getPolygon().size(); i++){
			p1 = getEntity().getPolygon().get(i-1);
			p2 = getEntity().getPolygon().get(i);
			shape.line(p1.x, p1.y, p2.x, p2.y);
		}
		//Close Shape
		if(getEntity().getPolygon().size() > 2){
			p1 = getEntity().getPolygon().get(getEntity().getPolygon().size()-1);
			p2 = getEntity().getPolygon().get(0);
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
	public void drawOverlay(float delta) {
	}

	@Override
	public void drawStage(float delta) {
	}

	public PolygonEntity getEntity() {
		return entity;
	}

	public void setEntity(PolygonEntity entity) {
		this.entity = entity;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		if(currentControler != null){
			return currentControler.touchDown(x, y, pointer, button);
		}
		return super.touchDown(x, y, pointer, button);
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return super.touchUp(screenX, screenY, pointer, button);
	}
	@Override
	public boolean touchUp(float x, float y, int pointer, int button) {
		if(currentControler != null){
			return currentControler.touchDown(x, y, pointer, button);
		}
		return super.touchDown(x, y, pointer, button);
	}
}
