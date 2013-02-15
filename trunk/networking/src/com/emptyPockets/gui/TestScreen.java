package com.emptyPockets.gui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad

public class TestScreen extends StageScreen{
	TextButton left;
	TextButton right;
	
	public TestScreen(InputMultiplexer inputProcessor) {
		super(inputProcessor);
		left = new TextButton("L",getSkin());
		right = new TextButton("R",getSkin());
		
		left.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				System.out.println("LEFT");
			}
		});
		right.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				System.out.println("Right");
			}
		});
		setStageTransparency(0.3f);
	}
	
	@Override
	public void drawScreen(float delta) {
		
	}

	@Override
	public void updateLogic(float delta) {
		
	}
	
	public void setStageTransparency(float value){
		
	}
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		int border = 10;
		right.x =border;
		right.y = 0;
		
		right.width = Math.round(width/2-border*1.5);
		right.height= ScreenSizeHelper.getcmtoPxlY(2);
		
		left.width = right.width;
		left.height = right.height;
		left.x = width/2+border;
	}

	@Override
	public void createStage(Stage stage) {
		stage.addActor(left);
		stage.addActor(right);
	}
	
	@Override
	public void drawStage(float delta) {
			super.drawStage(delta);
	}

	@Override
	public void drawOverlay(float delta) {
		// TODO Auto-generated method stub
		
	}

}
