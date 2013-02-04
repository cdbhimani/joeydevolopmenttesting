package com.joey.chain.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.joey.chain.ReactorApp;
import com.joey.chain.model.travel.Cell;
import com.joey.chain.model.travel.GameGrid;

public class GameGridViewer extends GameScreen{

	float radius;
	float diameter;
	GameGrid gameGrid;
	ShapeRenderer shape;
	boolean first = true;
	
	public GameGridViewer(ReactorApp game) {
		super(game);
		gameGrid = new GameGrid(2, 1);
		shape = new ShapeRenderer();
		setRadius(20);
	}

	@Override
	public void render(float delta) {
		if(first){
			gameGrid.activate();
			first = false;
		}
		// TODO Auto-generated method stub
		super.render(delta);
		gameGrid.update();
		shape.setProjectionMatrix(stageCamera.combined);
		Cell c;
		float xPos = 0;
		float yPos = 0;
		
		for(int x = 0; x < gameGrid.getWidth(); x++){
			for(int y = 0; y < gameGrid.getHeight(); y++){
				c = gameGrid.grid[x][y];
				
				xPos = (c.currentPos.x+1)*diameter;
				yPos = (c.currentPos.y+1)*diameter;
				
				shape.begin(ShapeType.FilledCircle);
				if(c.alive){
					shape.setColor(Color.BLUE);
				}else{
					shape.setColor(Color.RED);
				}
				shape.filledCircle(xPos,yPos, radius);
				shape.end();
				
				
				xPos = (c.desiredPos.x+1)*diameter;
				yPos = (c.desiredPos.y+1)*diameter;
				
				shape.begin(ShapeType.Circle);
				if(c.alive){
					shape.setColor(Color.RED);
				}else{
					shape.setColor(Color.BLUE);
				}
				shape.circle(xPos,yPos, radius-1);
				shape.end();
			}
		}
		
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
		this.diameter = 2*radius;
	}

	public float getDiameter() {
		return diameter;
	}
}
