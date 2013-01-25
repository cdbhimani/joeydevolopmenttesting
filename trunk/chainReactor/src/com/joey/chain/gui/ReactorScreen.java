package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.joey.chain.ReactorApp;

public class ReactorScreen extends GameScreen{

	public ReactorScreen(ReactorApp game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void createStage(Stage stage) {
		// TODO Auto-generated method stub
		super.createStage(stage);
		
		Skin skin = getSkin();
		Label nameLabel = new Label("Name:", skin);
		TextField nameText = new TextField(skin);
		Label addressLabel = new Label("Address:", skin);
		TextField addressText = new TextField(skin);

		
		Table table = new Table(skin);
		table.setFillParent(true);
		table.add(nameLabel);
		table.add(nameText).width(100);
		table.row();
		table.add(addressLabel);
		table.add(addressText).width(100);
		
		stage.addActor(table);
		
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
				Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
				Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

				stage.act(delta);
				stage.draw();
//				Table.drawDebug(stage);
	}

}
