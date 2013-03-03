package com.emptyPockets.box2d.gui;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class BodySettingPanel extends Table{
	Body body;
	TextField posField;
	TextField velField;
	TextField aglField;
	TextField aglVelField;
	
	public BodySettingPanel(Body body, Skin skin){
		this.body = body;
		
		createPanel(skin);
	}
	
	public void createPanel(Skin skin){
		row();
		add(new Label("Position", skin));
		row();
		add(new Label("Velocity", skin));
		row();
		add(new Label("Angle", skin));
		row();
		add(new Label("Angle Vel", skin));
	}
	
	public void setBody(Body body){
		this.body = body;
	}
}
