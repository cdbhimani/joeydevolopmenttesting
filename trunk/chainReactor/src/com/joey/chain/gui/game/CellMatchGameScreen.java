package com.joey.chain.gui.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.joey.chain.ReactorApp;
import com.joey.chain.games.cellMatchSetGame.Cell;
import com.joey.chain.games.cellMatchSetGame.CellMatchEngine;
import com.joey.chain.gui.GameScreen;

public class CellMatchGameScreen extends GameScreen{

	int sizeX = 11;
	int sizeY = 16;
	
	float radius;
	float diameter;
	CellMatchEngine gameEngine;
	ShapeRenderer shape;
	BitmapFont font;
	SpriteBatch batch;
	
	public CellMatchGameScreen(ReactorApp game) {
		super(game);
	}
	
	public void show() {
		super.show();
		gameEngine = new CellMatchEngine(sizeX, sizeY);
		shape = new ShapeRenderer();
		font = new BitmapFont();
		batch = new SpriteBatch();
		
		float rx = Gdx.graphics.getWidth()/(2*(sizeX+1));
		float ry = Gdx.graphics.getHeight()/(2*(sizeY+1));
		setRadius(Math.min(rx, ry));
	}
	
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		gameEngine = null;
		shape.dispose();
		font.dispose();
		batch.disableBlending();
	}

	@Override
	public boolean tap(int x, int y, int count) {
		if(gameEngine.isFinished() && count > 1){
			gameEngine.reset();
		}else{
			Vector3 v = new Vector3(x,y,0);
			cam.unproject(v);
			int xP = MathUtils.round(v.x/diameter-1f);
			int yP = MathUtils.round(v.y/diameter-1f);
			
			if(xP >= 0 && yP >= 0 && xP < gameEngine.getWidth() && yP < gameEngine.getHeight()){
				gameEngine.touch(xP, yP);
			}
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

	@Override
	public void drawScreen(float delta) {

		if(gameEngine.isFinished()){
			return;
		}
		shape.setProjectionMatrix(cam.combined);
		Cell c;
		float xPos = 0;
		float yPos = 0;


		shape.begin(ShapeType.FilledCircle);
		
		for (int x = 0; x < gameEngine.getWidth(); x++) {
			for (int y = 0; y < gameEngine.getHeight(); y++) {
				c = gameEngine.getBoard()[x][y];

				xPos = (c.currentPos.x + 1) * diameter;
				yPos = (c.currentPos.y + 1) * diameter;

				if (c.isAlive()) {
					shape.setColor(c.getColor());
					shape.filledCircle(xPos, yPos, radius, 6);
					
				}
			}
		}
		shape.end();

	}

	@Override
	public void drawOverlay(float delta) {
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		if(gameEngine.isFinished()){
			font.setColor(Color.RED);
			String textA = 
					"  Game Over\n" +
					"Tap to Restart ";
			TextBounds boundA = font.getMultiLineBounds(textA);
			
			String textB = "Score :"+gameEngine.getScore();
			TextBounds boundB = font.getBounds(textB);
			
			font.draw(batch, textB, Gdx.graphics.getWidth()/2-boundB.width/2, Gdx.graphics.getHeight()/4+boundB.height/2);
			font.drawMultiLine(batch, textA, Gdx.graphics.getWidth()/2-boundA.width/2, Gdx.graphics.getHeight()/2+boundA.height/2);
		}else{
			font.setColor(Color.RED);
			font.draw(batch, "lives : "+gameEngine.getLives(), 10, Gdx.graphics.getHeight()-20);
			font.draw(batch, "Score  : "+gameEngine.getScore(), 10, Gdx.graphics.getHeight()-40);
			
		}
		batch.end();
	}

	@Override
	public void updateLogic(float delta) {
		gameEngine.update();
	}
}
