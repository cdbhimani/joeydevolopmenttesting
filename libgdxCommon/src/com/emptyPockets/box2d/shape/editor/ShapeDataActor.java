package com.emptyPockets.box2d.shape.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.box2d.shape.data.ShapeData;
import com.emptyPockets.gui.Scene2DToolkit;

public class ShapeDataActor extends Table{
	ShapeData data;
	
	Label text;
	public ShapeDataActor(ShapeData shape){
		this.data = shape;
		text = new Label(shape.getName(), Scene2DToolkit.getToolkit().getSkin());
		
	}
	
	@Override
	public void setHeight(float height) {
		super.setHeight(height);
		
		clear();
		add(text).height(height).fill().expand();
	}
}
