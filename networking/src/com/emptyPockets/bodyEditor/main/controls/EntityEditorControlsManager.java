package com.emptyPockets.bodyEditor.main.controls;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.bodyEditor.entity.BaseEntity;
import com.emptyPockets.bodyEditor.entity.CircleEntity;
import com.emptyPockets.bodyEditor.entity.PolygonEntity;
import com.emptyPockets.bodyEditor.entity.RectangleEntity;
import com.emptyPockets.bodyEditor.main.EntityEditorScreen;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptyPockets.utils.OrthoCamController;

public class EntityEditorControlsManager extends Table {
	EntityEditorScreen owner;

	OrthoCamController cameraControl;
	PolygonControler polygonControl;
	RectangleControler rectangleControl;
	CircleControler circleControl;
	InputMultiplexer inputMultiplexer;

	ButtonGroup buttonGroup;
	Button positionButton;
	Button editorButton;

	public EntityEditorControlsManager(EntityEditorScreen editor) {
		this.owner = editor;
		inputMultiplexer = new InputMultiplexer();

		cameraControl = new OrthoCamController(owner.getEditorCamera());
		polygonControl = new PolygonControler(owner);
		rectangleControl = new RectangleControler(owner);
		circleControl = new CircleControler(owner);
		
		positionButton = new TextButton("+", owner.getSkin(), "toggle");
		editorButton = new TextButton("E", owner.getSkin(), "toggle");
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(positionButton);
		buttonGroup.add(editorButton);
		editorButton.setChecked(true);
		buttonGroup.setMaxCheckCount(1);

		layoutButtons();
		attachListeners();
		
		ChangeListener change = new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				update();
			}
		};
		
		positionButton.addListener(change);
		editorButton.addListener(change);
	}

	public void layoutButtons() {
		float sizeX = ScreenSizeHelper.getcmtoPxlX(.6f);
		float sizeY = ScreenSizeHelper.getcmtoPxlY(.6f);
		clear();
		add(positionButton).size(sizeX, sizeY);
		row();
		add(editorButton).size(sizeX, sizeY);
		pack();
	}

	private void attachListeners() {

		if (editorButton.isChecked()) {
			if (owner.getEntity() instanceof PolygonEntity) {
				polygonControl.attach(inputMultiplexer);
			}
			
			if(owner.getEntity() instanceof RectangleEntity){
				rectangleControl.attach(inputMultiplexer);
			}
			
			if(owner.getEntity() instanceof CircleEntity){
				circleControl.attach(inputMultiplexer);
			}
//			inputMultiplexer.addProcessor(cameraControl);
		}

		if (positionButton.isChecked()) {
			inputMultiplexer.addProcessor(cameraControl);
		}
	}

	private void detachListeners() {
		inputMultiplexer.clear();
	}

	public void update() {
		detachListeners();
		attachListeners();

		if (owner.getEntity() instanceof PolygonEntity) {
			polygonControl.setPolygon(((PolygonEntity) owner.getEntity()).getPolygon());
		}
		if (owner.getEntity() instanceof RectangleEntity) {
			rectangleControl.setRectangle(((RectangleEntity)owner.getEntity()).getRectangle());
		}
		if (owner.getEntity() instanceof CircleEntity) {
			circleControl.setCircle(((CircleEntity) owner.getEntity()).getCircle());
		}
	}

	public InputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	public void setInputMultiplexer(InputMultiplexer inputMultiplexer) {
		this.inputMultiplexer = inputMultiplexer;
	}

	public void drawScreen(ShapeRenderer shape, BaseEntity entity) {
		if (entity != null) {
			if (entity instanceof PolygonEntity) {
				polygonControl.draw(shape,editorButton.isChecked());
			}
			
			if (entity instanceof RectangleEntity) {
				rectangleControl.draw(shape,editorButton.isChecked());
			}
			
			if (entity instanceof CircleEntity) {
				circleControl.draw(shape,editorButton.isChecked());
			}
		}

	}

	public void resize(int width, int height) {
		layoutButtons();
	}

}
