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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.emptyPockets.box2d.shape.data.RectangleShapeData;
import com.emptyPockets.box2d.shape.data.ShapeData;
import com.emptyPockets.box2d.shape.editor.CircleControler;
import com.emptyPockets.box2d.shape.editor.PolygonControler;
import com.emptyPockets.box2d.shape.editor.RectangleControler;
import com.emptyPockets.box2d.shape.editor.ShapeDataActor;
import com.emptyPockets.gui.Scene2DToolkit;
import com.emptyPockets.gui.ScreenSizeHelper;

public class ShapeManager extends Table{
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
	PolygonControler polyonControl;
	CircleControler circleControl;

	float menuAnimationTime = 1f;
	Interpolation menuInterp = Interpolation.exp10Out;
	
	public ShapeManager(){
		super(Scene2DToolkit.getToolkit().getSkin());
		createPanel();
		createFakeData();
		updateTree();
		debug();
	}
	
	public Skin getSkin(){
		return Scene2DToolkit.getToolkit().getSkin();
	}
		
	public void createFakeData(){
		for(int i = 0;i < 10; i++){
			RectangleShapeData data= new RectangleShapeData();
			data.setName("Rectangle : "+i);
			shapes.add(data);
		}
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
