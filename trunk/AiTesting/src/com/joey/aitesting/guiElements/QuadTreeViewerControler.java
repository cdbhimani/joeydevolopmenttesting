package com.joey.aitesting.guiElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.joey.aitesting.game.graphics.QuadTreeViewer;

public class QuadTreeViewerControler extends Window{
	QuadTreeViewer quadViewer;
	Skin skin;
	public QuadTreeViewerControler(Skin skin, Stage stage){
		super("QuadTree Controler", skin);
		createStage(stage);
	}
	
	public void createStage(Stage stage) {
		skin = new Skin(Gdx.files.internal("data/uiskin.json"),
				Gdx.files.internal("data/uiskin.png"));

		final CheckBox drawGrid = new CheckBox(skin);
		final CheckBox drawParticles = new CheckBox(skin);
		final CheckBox drawBehaviours = new CheckBox(skin);
		
		drawGrid.setChecked(quadViewer.drawQuadTree);
		drawParticles.setChecked(quadViewer.drawEntities);
		drawBehaviours.setChecked(quadViewer.drawBehaviour);
		
		final Window window = this;



		window.x = 55;
		window.y = 0;

		
		defaults().spaceBottom(10);
		row().fill().expandX();
		add(new Label("Grid: ", skin));
		add(drawGrid).fill(true, false);
		row().fill().expandX();
		add(new Label("Entity: ", skin));
		add(drawParticles).fill(true, false);
		row().fill().expandX();
		add(new Label("Behaves: ", skin));
		add(drawBehaviours).fill(true, false);
		pack();

		stage.addActor(window);


		drawParticles.setClickListener(new ClickListener() {

			@Override
			public void click(Actor actor, float x, float y) {
				quadViewer.drawEntities = !quadViewer.drawEntities;
				drawParticles.setChecked(quadViewer.drawEntities);
			}
		});
		
		drawBehaviours.setClickListener(new ClickListener() {

			@Override
			public void click(Actor actor, float x, float y) {
				quadViewer.drawBehaviour= !quadViewer.drawBehaviour;
				drawBehaviours.setChecked(quadViewer.drawBehaviour);
			}
		});

		drawGrid.setClickListener(new ClickListener() {

			@Override
			public void click(Actor actor, float x, float y) {
				quadViewer.drawQuadTree = !quadViewer.drawQuadTree;
				drawGrid.setChecked(quadViewer.drawQuadTree);
			}
		});
	}
}
