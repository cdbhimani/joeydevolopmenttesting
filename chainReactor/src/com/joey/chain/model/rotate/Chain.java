package com.joey.chain.model.rotate;

import com.badlogic.gdx.math.MathUtils;

public class Chain {
	public enum ChainState{
		running,
		complete,
		stopped
	}
	
	Chain up = null;
	Chain down = null;
	Chain left = null;
	Chain right = null;
	
	float lastAngleDeg = 0;
	float currentAngle = 0;
	ChainState state = ChainState.stopped;
	
	public void updateProgressRunning(float progress){
		if(state == ChainState.running){
			currentAngle=lastAngleDeg+90*progress;
		}
	}
	
	public void updateStepComplete() {
		if(state==ChainState.complete){
			state =ChainState.stopped;
		}
	}
	
	public void updateProgressComplete(){
		if(state == ChainState.running){
			lastAngleDeg=MathUtils.floor(lastAngleDeg+90);
			currentAngle=lastAngleDeg;
			state = ChainState.complete;
			if(getAngle() >= 360){
				setAngle(0);
			}
		}
	}

	public void activate(){
		state = ChainState.running;
	}
	
	public ChainState getState(){
		return state;
	}
	
	public void setAngle(float angle){
		currentAngle = angle;
		lastAngleDeg = angle;
	}

	public float getAngle() {
		// TODO Auto-generated method stub
		return currentAngle;
	}
	
	/**
	 * Return the current direction
	 * 
	 * Direction 0<br>
	 * .x.<br>
	 * .xx<br>
	 * ...<br>
	 * <br>
	 * Direction 1<br>
	 * .x.<br>
	 * xx.<br>
	 * ...<br>
	 * <br>
	 * Direction 2<br>
	 * ...<br>
	 * xx.<br>
	 * .x.<br>
	 * <br>
	 * Direction 3<br>
	 * ...<br>
	 * .xx<br>
	 * .x.<br>
	 * <br>
	 * @return
	 */
	public int getDirection() {
		return (int) (getAngle() / 90);
	}
	
	public int notifyNeighbours(){
		if(this.state == ChainState.stopped){
			return 0;
		}
		
		int activeCount = 0;
		int dir = getDirection();
		int otherDir;
		if(up != null ){
			otherDir = up.getDirection();
			if((otherDir == 2 || otherDir ==3) && (dir==0 || dir ==1)){
				up.activate();
				activeCount++;
			}
		}
		if(down!=null){
			otherDir = down.getDirection();
			if((otherDir == 0 || otherDir ==1) && (dir==2 || dir ==3)){
				down.activate();
				activeCount++;
			}
		}
		if(left!=null){
			otherDir = left.getDirection();
			if((otherDir == 0 || otherDir ==3) && (dir==1 || dir ==2)){
				left.activate();
				activeCount++;
			}
		}
		if(right!=null){
			otherDir = right.getDirection();
			if((otherDir == 1 || otherDir ==2) && (dir==0 || dir ==3)){
				right.activate();
				activeCount++;
			}
		}		
		return activeCount;
	}

	public Chain getUp() {
		return up;
	}

	public void setUp(Chain up) {
		this.up = up;
	}

	public Chain getDown() {
		return down;
	}

	public void setDown(Chain down) {
		this.down = down;
	}

	public Chain getLeft() {
		return left;
	}

	public void setLeft(Chain left) {
		this.left = left;
	}

	public Chain getRight() {
		return right;
	}

	public void setRight(Chain right) {
		this.right = right;
	}

	
}
