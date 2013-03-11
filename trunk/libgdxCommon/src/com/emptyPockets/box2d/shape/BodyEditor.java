package com.emptyPockets.box2d.shape;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.box2d.body.BodyData;
import com.emptyPockets.box2d.shape.data.CircleShapeData;
import com.emptyPockets.box2d.shape.data.PolygonShapeData;
import com.emptyPockets.box2d.shape.data.RectangleShapeData;
import com.emptyPockets.box2d.shape.data.ShapeData;
import com.emptyPockets.box2d.shape.editor.BaseShapeControler.ControlState;
import com.emptyPockets.box2d.shape.editor.CircleControler;
import com.emptyPockets.box2d.shape.editor.PolygonControler;
import com.emptyPockets.box2d.shape.editor.RectangleControler;
import com.emptyPockets.box2d.shape.editor.ShapeDataActor;
import com.emptyPockets.box2d.shape.editor.ShapeDataChangeListener;
import com.emptyPockets.gui.OrthographicsCameraConvertor;
import com.emptyPockets.gui.Scene2DToolkit;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptyPockets.gui.ViewportConvertor;
import com.emptyPockets.utils.OrthoCamController;

public class BodyEditor extends Table implements ShapeDataChangeListener{
	float menuAnimationTime = 1f;
	Interpolation menuInterp = Interpolation.exp10Out;
	Tree tree;
	ScrollPane treeScroll;
	Button hidePanelButton;
	Button showPanelButton;
	Button deleteButton;
	TextButton circleButton;
	TextButton rectangleButton;
	TextButton polygonButton;
	
	Node root;
	HashMap<ShapeData, Node> treeData;
	
	InputMultiplexer input;
	OrthoCamController positionControler;
	RectangleControler rectangleControl;
	PolygonControler polygonControl;
	CircleControler circleControl;

	ShapeData selectedShape = null;
	ViewportConvertor viewConvertor;
	
	
	BodyData bodyData;
	public BodyEditor(OrthographicCamera camera){
		super(Scene2DToolkit.getToolkit().getSkin());
		this.viewConvertor = new OrthographicsCameraConvertor(camera);
		this.positionControler = new OrthoCamController(camera);
		this.input = new InputMultiplexer();
		
		createPanel();
		createListeners();
		updateMouseListeners();
		updateTree();
		debug();
		
		bodyData = new BodyData(new ArrayList<ShapeData>());
	}
	
	public void attach(InputMultiplexer control){
		control.addProcessor(input);
	}
	
	public void detatch(InputMultiplexer control){
		control.removeProcessor(input);
	}
	
	
	public void createListeners(){
		rectangleControl = new RectangleControler(viewConvertor);
		circleControl = new CircleControler(viewConvertor);
		polygonControl = new PolygonControler(viewConvertor);
		
		rectangleControl.addListener(this);
		circleControl.addListener(this);
		polygonControl.addListener(this);
	}
	public void setSelectedShape(ShapeData shape){
		this.selectedShape = shape;
		updateMouseListeners();
	}
	
	public void clearMouseListeners(){
		rectangleControl.setRectangle(null);
		circleControl.setCircle(null);
		polygonControl.setPolygon(null);
		
		rectangleControl.setState(ControlState.DISABLED);
		polygonControl.setState(ControlState.DISABLED);
		circleControl.setState(ControlState.DISABLED);
		
		rectangleControl.detach(input);
		polygonControl.detach(input);
		circleControl.detach(input);
		input.removeProcessor(positionControler);
	}
	
	public void updateMouseListeners(){
		clearMouseListeners();
		
		if(selectedShape != null){
			if(selectedShape instanceof CircleShapeData){
				circleControl.setCircle((CircleShapeData) selectedShape);
				circleControl.setState(ControlState.EDIT);
				circleControl.attach(input);
			}
			
			if(selectedShape instanceof RectangleShapeData){
				rectangleControl.setRectangle((RectangleShapeData) selectedShape);
				rectangleControl.setState(ControlState.EDIT);
				rectangleControl.attach(input);
			}
			
			if(selectedShape instanceof PolygonShapeData){
				polygonControl.setPolygon((PolygonShapeData) selectedShape);
				polygonControl.setState(ControlState.EDIT);
				polygonControl.attach(input);
			}
		}
		input.addProcessor(positionControler);
	}
	
	public Skin getSkin(){
		return Scene2DToolkit.getToolkit().getSkin();
	}
			
	public void createPanel(){
		tree = new Tree(getSkin());
		treeData = new HashMap<ShapeData, Node>();
		
		circleButton = new TextButton("C", getSkin());
		rectangleButton = new TextButton("R", getSkin());
		polygonButton = new TextButton("P", getSkin());
		deleteButton = new TextButton("Delete", getSkin());
		
		hidePanelButton = new TextButton("Hide", getSkin());
		showPanelButton = new TextButton("Show", getSkin());
		
		root = new Node(new Label("Shapes", getSkin()));
		tree.add(root);
		
		
		treeScroll = new ScrollPane(tree, getSkin());
		treeScroll.setFadeScrollBars(false);
		setButtonSize(1f);
		
		hidePanelButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				hideControlPanel();
			}
		});
		
		showPanelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				showControlPanel();
			}
		});
		
		circleButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				addCircle();
		}});
		
		rectangleButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				addRectangle();
		}});
		
		polygonButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				addPolygon();
		}});
		
		deleteButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				deleteSelectedShape();
			}});
		tree.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(tree != null && tree.getSelection().size > 0){
					Node nodeData = tree.getSelection().get(0);
					if(nodeData.getObject() instanceof ShapeData){
						setSelectedShape(((ShapeData)nodeData.getObject()));	
					}else{
						setSelectedShape(null);
					}
					
				}
			}});
	}
	
	public void deleteSelectedShape(){
		if(selectedShape != null){
			synchronized (bodyData.getShapes()) {
				Node node = treeData.get(selectedShape);
				tree.remove(node);
				treeData.remove(node);
				node.setObject(null);
				node.removeAll();
				bodyData.getShapes().remove(selectedShape);
				bodyData.updateAABoundingBox();
				updateTree();
				setSelectedShape(null);
			}
		}
	}
	public void showControlPanel(){
		setPosition(-getWidth(), Gdx.graphics.getHeight()-getHeight());
		setVisible(true);
		addAction(sequence(moveTo(0, Gdx.graphics.getHeight()-getHeight(),menuAnimationTime,menuInterp)));
	}
	
	public void hideControlPanel(){
		showPanelButton.setVisible(false);
		
		
		Runnable finishedRun = new Runnable() {
			@Override
			public void run() {
				setVisible(false);
				showPanelButton.setVisible(true);
			}
		};
		
		setPosition(0, Gdx.graphics.getHeight()-getHeight());
		addAction(
		   sequence(
				moveTo(-getWidth(), Gdx.graphics.getHeight()-getHeight(),menuAnimationTime,menuInterp),
				run(finishedRun)
				));
	}
	
	public void updateTree(){
		root.removeAll();
		if(bodyData != null){
			synchronized (bodyData.getShapes()) {
				for(ShapeData shape : bodyData.getShapes()){
					Node node = treeData.get(shape);
					if(node == null){
						ShapeDataActor data = new ShapeDataActor(shape);
						node = new Node(data);
						node.setObject(shape);
						treeData.put(shape, node);
					}
					root.add(node);
					if(node.getObject() == selectedShape){
						tree.setSelection(node);
					}
		
				}
				root.setExpanded(true);
			}
		}
	}
		

	private void addShape(ShapeData shape){
		synchronized (bodyData.getShapes()) {
			bodyData.getShapes().add(shape);	
			bodyData.updateAABoundingBox();
		}
		setSelectedShape(shape);
		updateTree();
	}
	public void addCircle(){
		addShape(new CircleShapeData());
	}
	
	public void addRectangle(){
		addShape(new RectangleShapeData());
	}
	
	public void addPolygon(){
		addShape(new PolygonShapeData());
	}
	
	public void setButtonSize(float size) {
		float buttonHigh = ScreenSizeHelper.getcmtoPxlY(size);
		float buttonWide = ScreenSizeHelper.getcmtoPxlX(size);
		showPanelButton.setSize(buttonWide, buttonHigh);
		clear();
		add(circleButton).height(buttonHigh).expandX().fillX();
		add(rectangleButton).height(buttonHigh).expandX().fillX();
		add(polygonButton).height(buttonHigh).expandX().fillX();
		row();
		add(treeScroll).colspan(3).left().fill().expand();
		row();
		add(deleteButton).height(buttonHigh).colspan(3).expandX().fillX();
		row();
		add(hidePanelButton).height(buttonHigh).colspan(3).expandX().fillX();
		setWidth(300);
		invalidateHierarchy();
		
	}

	public Button getShowPanelButton() {
		return showPanelButton;
	}

	public void drawShapes(ShapeRenderer shapeRender) {
		if(bodyData == null){
			return;
		}
		synchronized (bodyData.getShapes()) {
			rectangleControl.draw(shapeRender);
			circleControl.draw(shapeRender);
			polygonControl.draw(shapeRender);

			for(ShapeData shape : bodyData.getShapes()){
				rectangleControl.draw(shapeRender, shape.getAABoundingBox(),Color.MAGENTA);
				if(shape instanceof RectangleShapeData){
					rectangleControl.draw(shapeRender, (RectangleShapeData) shape);
				}
				
				if(shape instanceof CircleShapeData){
					circleControl.draw(shapeRender, (CircleShapeData) shape);
				}
				
				if(shape instanceof PolygonShapeData){
					polygonControl.draw(shapeRender, (PolygonShapeData)shape);
				}
			}
			
			rectangleControl.draw(shapeRender, bodyData.getAABoundingBox(),Color.CYAN);
		}
	}
	
	public void createBody(World world){
		bodyData.createBody(world);
	}

	@Override
	public void shapeDataChanged(ShapeData data, boolean finished) {
		data.updateBoundingBox();
		bodyData.updateAABoundingBox();
	}
}
