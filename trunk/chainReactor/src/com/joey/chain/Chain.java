package com.joey.chain;

public class Chain {
	float lastAngleDeg = 0;
	boolean active = true;
	float currentAngle = 0;

	public void update(float progress){
		if(active){
			currentAngle=lastAngleDeg*90*progress;
		}
	}

	public void activateRevolution(){
		active = true;
	}
	
	public void setAngle(float angle){
		
	}
}
