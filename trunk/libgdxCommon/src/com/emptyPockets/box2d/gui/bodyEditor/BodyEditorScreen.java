package com.emptyPockets.box2d.gui.bodyEditor;

import java.util.ArrayList;

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
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.emptyPockets.box2d.gui.Box2DScreen;
import com.emptyPockets.box2d.shape.ShapeManager;
import com.emptyPockets.box2d.shape.data.ShapeData;
import com.emptyPockets.gui.ScreenSizeHelper;

public class BodyEditorScreen extends Box2DScreen{

	Table menuButtons;
	Button showMenuButton;
	Button hideMenuButton;
	TextButton circleButton;
	TextButton rectangleButton;
	TextButton polygonButton;
	
	float buttonSize = 0.6f;
	float menuAnimationTime = 1f;
	Interpolation menuInterp = Interpolation.exp10Out;
	
	ArrayList<ShapeData> shapes = new ArrayList<ShapeData>();
	
	ShapeManager manager;
	
	public BodyEditorScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		setClearColor(Color.DARK_GRAY);
		setShowDebug(true);
		
	}

	public void createMenuBar(Stage stage){
		showMenuButton = new TextButton("M",getSkin());
		hideMenuButton = new TextButton("M", getSkin());
		circleButton = new TextButton("C", getSkin());
		rectangleButton = new TextButton("R", getSkin());
		polygonButton = new TextButton("P", getSkin());
				
		menuButtons = new Table(getSkin());
		menuButtons.setBackground("default-rect");		
		setMenuButtonSize(buttonSize);
		
		stage.addActor(showMenuButton);
		stage.addActor(menuButtons);
		
		manager = new ShapeManager();
		Window win = new Window("test", getSkin());
		win.add(manager).fill().expand().top().left();
		win.setSize(200, 200);
		
		stage.addActor(win);
		
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
		circleButton.setSize(buttonWide, buttonHigh);
		rectangleButton.setSize(buttonWide, buttonHigh);
		polygonButton.setSize(buttonWide, buttonHigh);
		showMenuButton.setSize(buttonWide, buttonHigh);
		hideMenuButton.setSize(buttonWide, buttonHigh);
		showMenuButton.setPosition(0, Gdx.graphics.getHeight()-buttonHigh);
		menuButtons.setBounds(0, Gdx.graphics.getHeight()-buttonHigh, Gdx.graphics.getWidth(), buttonHigh);
		
		menuButtons.clear();

		menuButtons.add(hideMenuButton).size(buttonWide, buttonHigh).fillY().expandY();
		menuButtons.add(circleButton).size(buttonWide, buttonHigh).fillY().expandY();
		menuButtons.add(rectangleButton).size(buttonWide, buttonHigh).fillY().expandY();
		menuButtons.add(polygonButton).size(buttonWide, buttonHigh).fillY().expandY();
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
	}

	@Override
	public void createWorld(World world) {
	}

}
