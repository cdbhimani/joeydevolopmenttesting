package com.joey.chain.gui.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.joey.chain.ReactorApp;
import com.joey.chain.games.cellSwapGame.Cell;
import com.joey.chain.games.cellSwapGame.CellSwapEngine;
import com.joey.chain.games.cellSwapGame.CellSwapEngine.SwapDirection;
import com.joey.chain.gui.GameScreen;

public class CellSwapGameScreen extends GameScreen{


	int sizeX = 11;
	int sizeY = 16;
	
	float radius;
	float diameter;
	CellSwapEngine gameEngine;
	ShapeRenderer shape;
	BitmapFont font;
	SpriteBatch batch;
	
	Vector3 gridMouseDown = new Vector3();
	Vector3 gridMouseUp = new Vector3();
	Vector3 gridMouseDiff = new Vector3();
	
	boolean gridMouseValid = false;
	boolean gridMouseTouchNextToPrevious = false;
	
	public CellSwapGameScreen(ReactorApp game) {
		super(game);
	}
	
	public void show() {
		super.show();
		gameEngine = new CellSwapEngine(sizeX, sizeY);
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
	public boolean touchDown(int x, int y, int pointer) {
		System.out.println("Touch Down");
		//Check was previous cell flagged store in up holder
		if(isPositionInsideGrid(gridMouseUp) && gameEngine.getBoard()[(int) gridMouseUp.x][(int) gridMouseUp.y].isFlaged()){
			gameEngine.getBoard()[(int) gridMouseUp.x][(int) gridMouseUp.y].unFlag();
			gridMouseTouchNextToPrevious = true;
		}else{
			gridMouseTouchNextToPrevious = false;
		}
		
		gridMouseDown.x = x;
		gridMouseDown.y = y;
		
		screenToGrid(gridMouseDown);
		
		
		gridMouseDown.x = Math.round(gridMouseDown.x);
		gridMouseDown.y = Math.round(gridMouseDown.y);
		
		if(isPositionInsideGrid(gridMouseDown)){
			if(gridMouseTouchNextToPrevious){
				int dx = (int) (gridMouseDown.x-gridMouseUp.x);
				int dy = (int) (gridMouseDown.y-gridMouseUp.y);
				if((dx*dy == 0 )&& (Math.abs(dx) == 1 || Math.abs(dy) == 1)){ //one is zero other is 1
					gridMouseTouchNextToPrevious=true;
					gridMouseValid=true;
				}else{
					gridMouseTouchNextToPrevious = false;
					gridMouseValid = true;
				}
			}else{
				gridMouseValid = true;
			}
			return true;
		} else{
			return super.touchDown(x, y, pointer);
		}
	}
	
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		System.out.println("Touch Up");
		if(!gridMouseValid){
			return false;
		}
		
		if(!gridMouseTouchNextToPrevious){
			//If was not previous touch 
			gridMouseUp.x = x;
			gridMouseUp.y = y;
			screenToGrid(gridMouseUp);
			gridMouseUp.x = Math.round(gridMouseUp.x);
			gridMouseUp.y = Math.round(gridMouseUp.y);
		}else{
			gameEngine.getBoard()[(int) gridMouseUp.x][(int) gridMouseUp.y].unFlag();	
		}
		
		gridMouseDiff.set(gridMouseUp).sub(gridMouseDown);
		
		System.out.println("\nDown : "+gridMouseDown);
		System.out.println("Up   : "+gridMouseUp);
		System.out.println("Diff : "+gridMouseDiff);
		
		if(isPositionInsideGrid(gridMouseDown) && isPositionInsideGrid(gridMouseUp)){
			//Ensure valid up position
			if(Math.abs(gridMouseDiff.x) < 0.5 && Math.abs(gridMouseDiff.y) < 0.5){
				//No Difference but mark cell
				gameEngine.getBoard()[(int) gridMouseDown.x][(int) gridMouseDown.y].flag();
				return super.touchDown(x, y, pointer);
			}else if(Math.abs(gridMouseDiff.x) < 0.5 && Math.abs(gridMouseDiff.y) > 0.5){
				gameEngine.touch((int)gridMouseDown.x, (int)gridMouseDown.y, 
						gridMouseDiff.y > 0?SwapDirection.UP:SwapDirection.DOWN);
				return true;
			}else if(Math.abs(gridMouseDiff.x) > 0.5 && Math.abs(gridMouseDiff.y) < 0.5){
				gameEngine.touch((int)gridMouseDown.x, (int)gridMouseDown.y, 
						gridMouseDiff.x > 0?SwapDirection.RIGHT:SwapDirection.LEFT);
				return true;
			}
		}
		return super.touchDown(x, y, pointer);
		
	}
	
	public boolean isPositionInsideGrid(Vector3 pos){
		return(pos.x >= 0 && pos.y >= 0 && pos.x < gameEngine.getWidth() && pos.y < gameEngine.getHeight());
	}
	
	@Override
	public boolean tap(int x, int y, int count) {
		if(gameEngine.isFinished() && count > 1){
			gameEngine.reset();
		}
		return super.tap(x, y, count);
	}
	
	private void screenToGrid(Vector3 vec){
		cam.unproject(vec);
		vec.x=(vec.x/diameter-1.5f);
		vec.y=(vec.y/diameter-1.5f);
	}
	
	private void gridCenterToScreen(Vector3 vec){
		vec.x= (vec.x + 1.5f) * diameter;
		vec.y = (vec.y + 1.5f) * diameter;
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
	

		shape.begin(ShapeType.FilledCircle);
		Vector3 vec = new Vector3();
		for (int x = 0; x < gameEngine.getWidth(); x++) {
			for (int y = 0; y < gameEngine.getHeight(); y++) {
				c = gameEngine.getBoard()[x][y];
				vec.set(c.currentPos.x, c.currentPos.y, 0);
				gridCenterToScreen(vec);
				
				if(c.isFlaged()){
					shape.setColor(Color.CYAN);
					shape.filledCircle(vec.x, vec.y, radius+3, 6);
				}
				if (c.isAlive()) {
					shape.setColor(c.getColor());
					shape.filledCircle(vec.x, vec.y, radius, 6);
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
			font.draw(batch, "Score  : "+gameEngine.getScore(), 10, Gdx.graphics.getHeight()-40);
			
		}
		batch.end();
	}

	@Override
	public void updateLogic(float delta) {
		gameEngine.update();
	}
}
