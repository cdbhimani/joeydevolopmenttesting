package com.joey.testing.game.graphics;

public class FPSCounter {
	long lastUpdate;
	boolean first = true;

	float updateTime = 0;
	float lastUpdateTime = 0;

	float smoothing = 0.01f;
	public void reset(){
		first = true;
	}

	public void tick(){
		if(first){
			first = false;
		}
		else{
			lastUpdateTime = System.currentTimeMillis()-lastUpdate;
			updateTime = (1-smoothing)*updateTime+smoothing*(lastUpdateTime);
		}
		lastUpdate = System.currentTimeMillis();
	}

	public float getFPS(){
		return 1000/updateTime;
	}

	public float getUpdateTime(){
		return updateTime;
	}
}
