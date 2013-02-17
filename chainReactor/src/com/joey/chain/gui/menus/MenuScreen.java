package com.joey.chain.gui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.joey.chain.ReactorApp;
import com.joey.chain.gui.StageScreen;

public class MenuScreen extends StageScreen{

	Texture texUp;
	Texture texDown;

	public MenuScreen(ReactorApp game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void show() {
		texUp = new Texture(Gdx.files.internal("ui/button_a_down.png"));
		texDown = new Texture(Gdx.files.internal("ui/button_a_up.png"));
		super.show();
	}
	
	public void hide() {
		super.hide();
		texUp.dispose();
		texDown.dispose();
	};
	
	public void createStage(Stage stage) {
		NinePatch patchUp = new NinePatch(texUp);
		NinePatch patchDown = new NinePatch(texDown);

		Skin skin = getSkin();
		
		TextButtonStyle style = new TextButtonStyle();
		style.up = new NinePatchDrawable(patchUp);
		style.down = new NinePatchDrawable(patchDown);
		style.font = new BitmapFont();
		
		TextButton rotateGameButton = new TextButton("Rotate Game", style);
		TextButton matchGameButton = new TextButton("Match Game", style);
		TextButton swapGameButton = new TextButton("Swap Game", style);
		TextButton networkButton= new TextButton("Server", style);
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
		table.add(swapGameButton).width(buttonWide).height(buttonHigh);
		table.row();
		table.add(networkButton).width(buttonWide).height(buttonHigh);
		table.row();
		table.add(exitButton).width(buttonWide).height(buttonHigh);
		stage.addActor(table);
		
		rotateGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent even, float x, float y) {
				getGame().setScreen(getGame().getCellRotateScreen());
			}
		});
		
		matchGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent even, float x, float y) {
				// TODO Auto-generated method stub
				getGame().setScreen(getGame().getCellMatchScreen());
			}
		});
		
		swapGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent even, float x, float y) {
				// TODO Auto-generated method stub
				getGame().setScreen(getGame().getCellSwapScreen());
			}
		});

		networkButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent even, float x, float y) {
				getGame().setScreen(getGame().getNetworkScreen());
			}
		});
		
	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawOverlay(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLogic(float delta) {
		// TODO Auto-generated method stub
		
	}

}
