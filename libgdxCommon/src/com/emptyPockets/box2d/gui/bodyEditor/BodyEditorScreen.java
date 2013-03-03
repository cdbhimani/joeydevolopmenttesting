package com.emptyPockets.box2d.gui.bodyEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.emptyPockets.box2d.gui.Box2DScreen;
import com.emptyPockets.gui.ScreenSizeHelper;

public class BodyEditorScreen extends Box2DScreen{

	Table menuButtons;
	Button showMenuButton;
	Button hideMenuButton;
	TextButton buttonA;
	TextButton buttonB;
	TextButton buttonC;
	
	float buttonSize = 0.6f;
	float menuAnimationTime = 1f;
	Interpolation menuInterp = Interpolation.exp10Out;
	
	public BodyEditorScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		setClearColor(Color.DARK_GRAY);
		setShowDebug(true);
	}

	public void createMenuBar(Stage stage){
		showMenuButton = new TextButton("Show",getSkin());
		hideMenuButton = new TextButton("Hide", getSkin());
		buttonA = new TextButton("A", getSkin());
		buttonB = new TextButton("B", getSkin());
		buttonC = new TextButton("C", getSkin());
				
		menuButtons = new Table(getSkin());
		menuButtons.setBackground("default-rect");		
		setMenuButtonSize(buttonSize);
		
		stage.addActor(showMenuButton);
		stage.addActor(menuButtons);
		
		showMenuButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				showButtonBar();
			}
		});
		
		hideMenuButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				hideButtonBar();
			}
		});
	}
	
	public void showButtonBar(){
//		menuButtons.setVisible(true);
		
		MoveToAction move = new MoveToAction();
		move.setDuration(menuAnimationTime);
		move.setPosition(0, Gdx.graphics.getHeight()-menuButtons.getHeight());
		move.setInterpolation(menuInterp);
		
		SequenceAction show = new SequenceAction();
		show.addAction(move);
		
		menuButtons.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()-menuButtons.getHeight());
		menuButtons.addAction(show);
		
		showMenuButton.setVisible(false);
	}
	
	public void hideButtonBar(){
//		menuButtons.setVisible(false);
		
		MoveToAction move = new MoveToAction();
		move.setDuration(menuAnimationTime);
		move.setInterpolation(menuInterp);
		move.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()-menuButtons.getHeight());
		
		SequenceAction hide = new SequenceAction();
		hide.addAction(move);

		menuButtons.setPosition(0, Gdx.graphics.getHeight()-menuButtons.getHeight());
		menuButtons.addAction(hide);
		showMenuButton.setVisible(true);
	}
	@Override
	public void createStage(Stage stage) {
		createMenuBar(stage);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		setMenuButtonSize(buttonSize);
	}
	
	public void setMenuButtonSize(float size){
		float buttonWide = ScreenSizeHelper.getcmtoPxlX(size);
		float buttonHigh = ScreenSizeHelper.getcmtoPxlY(size);
		buttonA.setSize(buttonWide, buttonHigh);
		buttonB.setSize(buttonWide, buttonHigh);
		buttonC.setSize(buttonWide, buttonHigh);
		showMenuButton.setSize(buttonWide, buttonHigh);
		hideMenuButton.setSize(buttonWide, buttonHigh);
		showMenuButton.setPosition(0, Gdx.graphics.getHeight()-buttonHigh);
		menuButtons.setBounds(0, Gdx.graphics.getHeight()-buttonHigh, Gdx.graphics.getWidth(), buttonHigh);
		
		menuButtons.clear();

		menuButtons.add(hideMenuButton).size(buttonWide, buttonHigh).fillY().expandY();
		menuButtons.add(buttonA).size(buttonWide, buttonHigh).fillY().expandY();
		menuButtons.add(buttonB).size(buttonWide, buttonHigh).fillY().expandY();
		menuButtons.add(buttonC).size(buttonWide, buttonHigh).fillY().expandY();
		menuButtons.add().fill().expand();

		menuButtons.invalidateHierarchy();
	}
	@Override
	public void drawStage(float delta) {
		super.drawStage(delta);
		Table.drawDebug(getStage());
	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createWorld(World world) {
		// TODO Auto-generated method stub
		
	}

}
