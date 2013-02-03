package com.joey.chain.model.travel;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Walker {
	
	Vector2 currentPos;
	
	Vector2 lastPos;
	Vector2 desiredDir;
	
	public Walker(){
		currentPos = new Vector2();
		lastPos = new Vector2();
		desiredDir = new Vector2();
	}
	
	public void update(float progress){
		currentPos.x = (lastPos.x+desiredDir.x*progress);
		currentPos.y = (lastPos.y+desiredDir.y*progress);
	}
	
	public void updateFinished(){
		lastPos.x = MathUtils.round(lastPos.x+desiredDir.x);
		lastPos.y = MathUtils.round(lastPos.y+desiredDir.y);
	}
	
	public void setDirection(int x, int y){
		desiredDir.x = x;
		desiredDir.y = y;
	}
}
