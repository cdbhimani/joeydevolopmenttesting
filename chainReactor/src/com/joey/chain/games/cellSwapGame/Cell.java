package com.joey.chain.games.cellSwapGame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Cell {
	private boolean flag = false;
	private boolean alive = false;
	private boolean selected = false;
    public Vector2 currentPos;
    
    Vector2 lastDir;
    Vector2 lastPos;
    
    private Color color = Color.BLUE;
	private String colorName = "BLUE";
	
	public Vector2 desiredPos;
	
	public Cell(){
		lastPos = new Vector2();
		lastDir = new Vector2();
		currentPos = new Vector2();
		desiredPos = new Vector2();
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public boolean isFlaged(){
		return flag;
	}
	
	public void random(int difficulity){
		Color c = null;
		switch(MathUtils.random(difficulity-1)){
			case 0: c = Color.RED;colorName="RED";break;
			case 1: c = Color.GREEN;colorName="GREEN";break;
			case 2: c = Color.BLUE;colorName="BLUE";break;
			case 3: c = Color.ORANGE;colorName="ORANGE";break;
			case 4: c = Color.PINK;colorName="PINK";break;
			case 5: c = Color.ORANGE;colorName="ORANGE";break;
			case 6: c = Color.CYAN;colorName="CYAN";break;
			case 7: c = Color.YELLOW;colorName="YELLOW";break;
			case 8: c = Color.MAGENTA;colorName="MAGENTA";break;
			case 9: c = Color.BLACK;colorName="BLACK";break;
			case 10: c = Color.LIGHT_GRAY;colorName="LIGHT_GRAY";break;
		}
		color = c;
		alive = true;
		flag = false;
	}
	
	public void flag(){
		flag =true;
	}
	
	public void unFlag(){
		flag = false;
	}
	public void unKill(){
		alive =true;
	}
	public void kill(){
		alive = false;
	}
	
	public boolean equals(Cell c){
		return c.getColor() == this.getColor() && alive;
	}
	public Color getColor(){
		return color;
	}
	public boolean calculateMovementRequired(){
		int dx = Math.round(2*(desiredPos.x-currentPos.x));
		int dy = Math.round(2*(desiredPos.y-currentPos.y));
		
		if(dx == 0){
			lastDir.x = 0;
		} else if(dx > 0){
			lastDir.x = 1;
		} else if(dx < 0){
			lastDir.x = -1;
		}
		
		if(dy == 0){
			lastDir.y = 0;
		} else if(dy > 0){
			lastDir.y = 1;
		} else if(dy < 0){
			lastDir.y = -1;
		}
		if(lastDir.len2() == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public void update(float progress){
		currentPos.x = (lastPos.x+lastDir.x*progress);
		currentPos.y = (lastPos.y+lastDir.y*progress);
	}
	
	public boolean updateFinished(){
		update(1);
		lastPos.set(currentPos);
		
		lastPos.x = MathUtils.round(lastPos.x);
		lastPos.y = MathUtils.round(lastPos.y);
		
		return calculateMovementRequired();
	}
	
	public String getTypeName(){
		return colorName;
	}

	public void setPos(float x, float y) {
		lastDir.set(0,0);
		lastPos.set(x,y);
		currentPos.set(x,y);
		desiredPos.set(x,y);
		updateFinished();
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
