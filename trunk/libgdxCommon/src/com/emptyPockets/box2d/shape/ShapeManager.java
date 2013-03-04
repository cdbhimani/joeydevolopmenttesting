package com.emptyPockets.box2d.shape;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
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

	public ShapeManager(){
		createPanel();
		createFakeData();
		updateTree();
		debug();
		float size = ScreenSizeHelper.getcmtoPxlX(2);
		setIconSize(size, size);
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
	
	public void setIconSize(float sizeX, float sizeY){
		setNodeHeight(root, sizeY);
		tree.invalidateHierarchy();
	}
	
	public void setNodeHeight(Node node, float high){
		node.getActor().setHeight(high);
		for(Node n : node.getChildren()){
			setNodeHeight(n, high);
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
		root = new Node(new Label("Shapes", getSkin()));
		tree.add(root);
		
		
		ScrollPane scroll = new ScrollPane(tree, getSkin());
		
		
		
		add(circleButton).expandX().fillX();
		add(rectangleButton).expandX().fillX();
		add(polygonButton).expandX().fillX();
		row();
		add(scroll).colspan(3).left().fill().expand();
		row();
		add(deleteButton).colspan(3).expandX().fillX();
		
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
}
