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
import com.emptyPockets.bodyEditor.entity.Entity;
import com.emptyPockets.bodyEditor.entity.PolygonEntity;
import com.emptyPockets.bodyEditor.main.controls.EntityEditorControlsManager;
import com.emptyPockets.gui.StageScreen;

public class EntityEditorScreen extends StageScreen {
	
	EntityEditorControlsManager controls;
	Entity entity;
	
	//Temp for camera 
	Vector3 _tmpCam2MouseVec = new Vector3();
	OrthographicCamera editorCamera;
	ShapeRenderer shape;
	
	public EntityEditorScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		setClearColor(Color.DARK_GRAY);
		
		
		editorCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		controls = new EntityEditorControlsManager(this);
		controls.update();
		
		setEntity(new PolygonEntity());
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
		input.addProcessor(controls.getInputMultiplexer());		
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(controls.getInputMultiplexer());		
	}
	
	@Override
	public void resize(int width, int height) {
		editorCamera.viewportWidth = width;
		editorCamera.viewportHeight = height;
	}

	@Override
	public void drawBackground(float delta) {
		editorCamera.update();
		shape.setProjectionMatrix(editorCamera.combined);
		shape.begin(ShapeType.Rectangle);
		shape.setColor(Color.RED);
		shape.rect(0, 0, 100, 100);
		shape.end();
	}

	@Override
	public void drawScreen(float delta) {
		editorCamera.update();
		shape.setProjectionMatrix(editorCamera.combined);
		
		Entity entity = getEntity();
		
		controls.drawScreen(shape, entity);

	}

	
	public void camToPanel(float x, float y, Vector2 vec){
		synchronized (_tmpCam2MouseVec) {
			_tmpCam2MouseVec.set(x,y,0);
			editorCamera.unproject(_tmpCam2MouseVec);
			vec.x = _tmpCam2MouseVec.x;
			vec.y = _tmpCam2MouseVec.y;
		}
	}
	
	public void updateScreenControl(){
		controls.update();
	}

	@Override
	public void drawOverlay(float delta) {
	}

	@Override
	public void drawStage(float delta) {
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
		updateScreenControl();
	}

	public OrthographicCamera getEditorCamera() {
		return editorCamera;
	}
	
	
}

