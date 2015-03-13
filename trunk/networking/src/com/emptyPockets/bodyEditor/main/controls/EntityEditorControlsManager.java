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
import com.emptyPockets.bodyEditor.main.controls.shape.BaseShapeControler.ControlState;
import com.emptyPockets.bodyEditor.main.controls.shape.CircleControler;
import com.emptyPockets.bodyEditor.main.controls.shape.PolygonControler;
import com.emptyPockets.bodyEditor.main.controls.shape.RectangleControler;
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
	Button shapeButton;
	public EntityEditorControlsManager(EntityEditorScreen editor) {
		this.owner = editor;
		inputMultiplexer = new InputMultiplexer();

		cameraControl = new OrthoCamController(owner.getEditorCamera());
		polygonControl = new PolygonControler(owner);
		rectangleControl = new RectangleControler(owner);
		circleControl = new CircleControler(owner);
		
		positionButton = new TextButton("+", owner.getSkin(), "toggle");
		editorButton = new TextButton("E", owner.getSkin(), "toggle");
		shapeButton = new TextButton("S", owner.getSkin(), "toggle");
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(positionButton);
		buttonGroup.add(editorButton);
		
		editorButton.setChecked(true);
		buttonGroup.setMaxCheckCount(1);
		buttonGroup.setMinCheckCount(0);
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
		
		shapeButton.addListener(new ChangeListener(){
			int count = 0;
			CircleEntity circle = new CircleEntity();
			RectangleEntity rectangle = new RectangleEntity();
			PolygonEntity polygon= new PolygonEntity();
			
			public void changed(ChangeEvent event, Actor actor) {
				switch(count){
				case 0 : owner.setEntity(circle);break;
				case 1 : owner.setEntity(rectangle);break;
				case 2 : owner.setEntity(polygon);break;
				}
				count++;
				if(count > 2){
					count = 0;
				}
				update();
			}});
	}

	public void layoutButtons() {
		float sizeX = ScreenSizeHelper.getcmtoPxlX(.6f);
		float sizeY = ScreenSizeHelper.getcmtoPxlY(.6f);
		clear();
		add(positionButton).size(sizeX, sizeY);
		row();
		add(editorButton).size(sizeX, sizeY);
		row();
		add(shapeButton).size(sizeX, sizeY);
		pack();
	}

	private void attachListeners() {

		//Attach
		if (owner.getEntity() instanceof PolygonEntity) {
			polygonControl.attach(inputMultiplexer);
		}
		if(owner.getEntity() instanceof RectangleEntity){
			rectangleControl.attach(inputMultiplexer);
		}
		if(owner.getEntity() instanceof CircleEntity){
			circleControl.attach(inputMultiplexer);
		}

		//Update controls
		if (editorButton.isChecked()) {
			inputMultiplexer.addProcessor(cameraControl);
			
			polygonControl.setState(ControlState.EDIT);
			circleControl.setState(ControlState.EDIT);
			rectangleControl.setState(ControlState.EDIT);
		}else if (positionButton.isChecked()) {
			inputMultiplexer.addProcessor(cameraControl);

			polygonControl.setState(ControlState.POSITION);
			circleControl.setState(ControlState.POSITION);
			rectangleControl.setState(ControlState.POSITION);
		} else{
			inputMultiplexer.addProcessor(cameraControl);
			
			polygonControl.setState(ControlState.DISABLED);
			circleControl.setState(ControlState.DISABLED);
			rectangleControl.setState(ControlState.DISABLED);
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
				polygonControl.draw(shape);
			}
			
			if (entity instanceof RectangleEntity) {
				rectangleControl.draw(shape);
			}
			
			if (entity instanceof CircleEntity) {
				circleControl.draw(shape);
			}
		}

	}

	public void resize(int width, int height) {
		layoutButtons();
	}

}
