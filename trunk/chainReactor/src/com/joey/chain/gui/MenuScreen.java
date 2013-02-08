package com.joey.chain.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.joey.chain.ReactorApp;

public class MenuScreen extends GameScreen{

	Stage stage;
	Texture texUp;
	Texture texDown;

	public MenuScreen(ReactorApp game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void show() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), false);
		super.show();
		texUp = new Texture(Gdx.files.internal("ui/button_a_down.png"));
		texDown = new Texture(Gdx.files.internal("ui/button_a_up.png"));
	
		createStage(stage);
	}
	
	public void hide() {
		super.hide();
		texUp.dispose();
		texDown.dispose();
		stage.dispose();
	};
	
	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		// TODO Auto-generated method stub
		super.addInputMultiplexer(input);
		input.addProcessor(stage);
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		// TODO Auto-generated method stub
		super.removeInputMultiplexer(input);
		input.removeProcessor(stage);
	}
	
	public void createStage(Stage stage) {
		NinePatch patchUp = new NinePatch(texUp);
		NinePatch patchDown = new NinePatch(texDown);
		
		Skin skin = getSkin();
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = patchUp;
		style.down = patchDown;
		style.font = new BitmapFont();
		
		TextButton rotateGameButton = new TextButton("Rotate Game", style);
		TextButton matchGameButton = new TextButton("Match Game", style);
		TextButton settingButton= new TextButton("Settings", style);
		TextButton highScoreButton  = new TextButton("High Score", style);
		TextButton exitButton = new TextButton("Exit", style);
		
		Table table = new Table(skin);
		
		int wideNum = 2;
		int highNum = 5;
		
		int buttonWide = Gdx.graphics.getWidth()/wideNum;
		int buttonHigh = Gdx.graphics.getHeight()/highNum;
		
		buttonHigh = Math.min(buttonHigh, 50);
		table.setFillParent(true);
		table.add(rotateGameButton).width(buttonWide).height(buttonHigh);
		table.row();
		table.add(matchGameButton).width(buttonWide).height(buttonHigh);
		table.row();
		table.add(highScoreButton).width(buttonWide).height(buttonHigh);
		table.row();
		table.add(settingButton).width(buttonWide).height(buttonHigh);
		table.row();
		table.add(exitButton).width(buttonWide).height(buttonHigh);
		stage.addActor(table);
		
		rotateGameButton.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				// TODO Auto-generated method stub
				game.setScreen(game.getCellRotateScreen());
			}
		});
		
		matchGameButton.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				// TODO Auto-generated method stub
				game.setScreen(game.getCellMatchScreen());
			}
		});
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawOverlay(float delta) {
		
	}

	@Override
	public void updateLogic(float delta) {
		// TODO Auto-generated method stub
		
	}

}
