package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
		
		Texture texUp = new Texture(Gdx.files.internal("ui/button_a_down.png"));
		Texture texDown = new Texture(Gdx.files.internal("ui/button_a_up.png"));
		
		NinePatch patchUp = new NinePatch(texUp);
		NinePatch patchDown = new NinePatch(texDown);
		
		Skin skin = getSkin();
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = patchUp;
		style.down = patchDown;
		style.font = new BitmapFont();
		
		TextButton newGameButton = new TextButton("New Game", style);
		TextButton settingButton= new TextButton("Settings", style);
		TextButton highScoreButton  = new TextButton("High Score", style);
		TextButton exitButton = new TextButton("Exit", style);
		
		Table table = new Table(skin);
		
		table.setFillParent(true);
		table.add(newGameButton).width(640).height(160);
		table.row();
		table.add(highScoreButton).width(640).height(160);
		table.row();
		table.add(settingButton).width(640).height(160);
		table.row();
		table.add(exitButton).width(640).height(160);
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
