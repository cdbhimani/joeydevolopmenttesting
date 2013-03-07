package com.emptyPockets.box2d.shape;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.box2d.shape.data.CircleShapeData;
import com.emptyPockets.box2d.shape.data.PolygonShapeData;
import com.emptyPockets.box2d.shape.data.RectangleShapeData;
import com.emptyPockets.box2d.shape.data.ShapeData;
import com.emptyPockets.box2d.shape.editor.CircleControler;
import com.emptyPockets.box2d.shape.editor.PolygonControler;
import com.emptyPockets.box2d.shape.editor.RectangleControler;
import com.emptyPockets.box2d.shape.editor.ShapeDataActor;
import com.emptyPockets.box2d.shape.editor.BaseShapeControler.ControlState;
import com.emptyPockets.gui.Scene2DToolkit;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptyPockets.gui.ViewportConvertor;

public class ShapeManager extends Table{
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
	ArrayList<ShapeData> shapes;
	HashMap<ShapeData, Node> treeData;
	
	RectangleControler rectangleControl;
	PolygonControler polygonControl;
	CircleControler circleControl;

	ShapeData selectedShape = null;
	ViewportConvertor owner;
	public ShapeManager(ViewportConvertor owner){
		super(Scene2DToolkit.getToolkit().getSkin());
		this.owner = owner;
		createPanel();
		createListeners();
		updateTree();
		debug();
	}
	
	public void createListeners(){
		rectangleControl = new RectangleControler(owner);
		circleControl = new CircleControler(owner);
		polygonControl = new PolygonControler(owner);
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
	}
	
	public void updateMouseListeners(){
		clearMouseListeners();
		
		if(selectedShape != null){
			
		}
	}
	
	public Skin getSkin(){
		return Scene2DToolkit.getToolkit().getSkin();
	}
			
	public void createPanel(){
		tree = new Tree(getSkin());
		shapes = new ArrayList<ShapeData>();
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
				addCircle();
			}});
		
		polygonButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				addCircle();
			}});
	}
	
	public void showControlPanel(){
		MoveToAction move = new MoveToAction();
		move.setDuration(menuAnimationTime);
		move.setPosition(0, Gdx.graphics.getHeight()-getHeight());
		move.setInterpolation(menuInterp);
		
		SequenceAction show = new SequenceAction();
		show.addAction(move);
		
		
		setPosition(-getWidth(), Gdx.graphics.getHeight()-getHeight());
		setVisible(true);

		addAction(show);
	}
	
	public void hideControlPanel(){
		showPanelButton.setVisible(false);
		MoveToAction move = new MoveToAction();
		move.setDuration(menuAnimationTime);
		move.setInterpolation(menuInterp);
		move.setPosition(-getWidth(), Gdx.graphics.getHeight()-getHeight());
		
		SequenceAction hide = new SequenceAction();
		hide.addAction(move);
		hide.addAction(new Action() {
			@Override
			public boolean act(float delta) {
				setVisible(false);
				showPanelButton.setVisible(true);
				return true;
			}
		});
		
		setPosition(0, Gdx.graphics.getHeight()-getHeight());
		addAction(hide);
	}
	
	public void updateTree(){
		root.removeAll();
		for(ShapeData shape : shapes){
			Node node = treeData.get(shape);
			if(node == null){
				node = new Node(new ShapeDataActor(shape));
				treeData.put(shape, node);
			}
			root.add(node);
		}
		root.setExpanded(true);
	}

	private void addShape(ShapeData shape){
		shapes.add(shape);
		updateTree();
		
		setSelectedShape(shape);
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
		setWidth(buttonWide*3);
		invalidateHierarchy();
		
	}

	public Button getShowPanelButton() {
		return showPanelButton;
	}
}
