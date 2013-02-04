package com.joey.chain.model.travel;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Cell {
	public boolean alive = false;
	
    public Vector2 currentPos;
    
    Vector2 lastDir;
    Vector2 lastPos;
	
	public Vector2 desiredPos;
	
	public Cell(){
		lastPos = new Vector2();
		lastDir = new Vector2();
		currentPos = new Vector2();
		desiredPos = new Vector2();
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
		
		float dx = Math.round(desiredPos.x-lastPos.x);
		float dy = Math.round(desiredPos.y-lastPos.y);
		
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
		
		System.out.println(lastDir);
		if(lastDir.len2() == 0){
			return true;
		}else{
			return false;
		}
	}

	public void setPos(float x, float y) {
		lastPos.set(x,y);
		currentPos.set(x,y);
		desiredPos.set(x,y);
		updateFinished();
	}

}
