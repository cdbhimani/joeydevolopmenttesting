package com.joey.chain.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.joey.chain.ReactorApp;
import com.joey.chain.model.travel.Cell;
import com.joey.chain.model.travel.GameGrid;

public class GameGridViewer extends GameScreen{

	int sizeX = 20;
	int sizeY = 20;
	
	float radius;
	float diameter;
	GameGrid gameGrid;
	ShapeRenderer shape;
	
	public GameGridViewer(ReactorApp game) {
		super(game);
		gameGrid = new GameGrid(sizeX, sizeY);
		shape = new ShapeRenderer();
		setRadius(10);
	}

	@Override
	public void render(float delta) {
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
				
				if(c.alive){
					shape.begin(ShapeType.FilledCircle);
					shape.setColor(c.c);
					shape.filledCircle(xPos,yPos, radius);
					shape.end();
				}
				
				
				shape.begin(ShapeType.Circle);
				if(c.alive){
					shape.setColor(Color.RED);
				}else{
					shape.setColor(Color.GREEN);
				}
				shape.circle(xPos,yPos, radius-1);
				shape.end();
			}
		}
		
	}

	@Override
	public boolean tap(int x, int y, int count) {
		if(count == 1){
			gameGrid.activate();
		}else{
			gameGrid.createGrid(sizeX,sizeY);
		}
		return super.tap(x, y, count);
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
