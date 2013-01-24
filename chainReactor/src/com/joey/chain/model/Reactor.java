package com.joey.chain.model;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.joey.chain.model.Chain.ChainState;

public class Reactor {
	enum ReactorState{
		finished, //Complete Full Run
		running, //Propagating between rotations
		active //Finished between rotations but not complete
	}
	
	long animationTime = 500;
	long animationStart = 0;
	
	Chain[][] board;
	Random rand;
	private ReactorState state = ReactorState.active;
	long score = 0;
	
	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public Reactor(){
		rand = new Random();
	}
	
	public void setSize(int sizeX, int sizeY){
		createBoard(sizeX, sizeY);
	}
	
	private void createBoard(int sizeX, int sizeY){
		board = new Chain[sizeX][sizeY];
		for(int x = 0; x < sizeX; x++){
			for(int y = 0; y < sizeY; y++){
				board[x][y] = new Chain();
			}
		}
		
		//set Neighbours
		for(int x = 0; x < sizeX; x++){
			for(int y = 0; y < sizeY; y++){
				if(x < sizeX-1)
					board[x][y].setRight(board[x+1][y]);
				
				if(x > 0)
					board[x][y].setLeft(board[x-1][y]);
				
				if(y > 0)
					board[x][y].setDown(board[x][y-1]);
				
				if(y < sizeY-1)
					board[x][y].setUp(board[x][y+1]);
			}
		}
	}
	
	public void resetBorad(long seed){
		rand.setSeed(seed);
		for(int x = 0; x < board.length; x++){
			for(int y = 0; y < board[x].length; y++){
				board[x][y].setAngle(rand.nextInt(4)*90);
			}
		}
	}
	
	public int getSizeY(){
		return (board == null?0:board[0].length);
	}

	public int getSizeX(){
		return (board == null?0:board.length);
	}
	private void updateBoradAnimationRunning(float progress){
		for(int x = 0; x < board.length; x++){
			for(int y = 0; y < board[x].length; y++){
				board[x][y].updateProgressRunning(progress);
			}
		}
	}
	
	private int updateBoardNextStep(){
		int activeCount = 0;
		
		Chain chain;
		for(int x = 0; x < board.length; x++){
			for(int y = 0; y < board[x].length; y++){
				chain = board[x][y]; 
				activeCount += chain.notifyNeighbours();
			}
		}
		
		for(int x = 0; x < board.length; x++){
			for(int y = 0; y < board[x].length; y++){
				board[x][y].updateStepComplete();
			}
		}
		return activeCount;
	}
	
	private void updateBoardProgressComplete(){
		for(int x = 0; x < board.length; x++){
			for(int y = 0; y < board[x].length; y++){
				board[x][y].updateProgressComplete();
			}
		}
				
	}
	
	public Chain[][] getBoard() {
		return board;
	}

	/**
	 * The following is the update state machine of the game. 
	 * The active state is when the animations have complete and it is doing calculation
	 * The running state is when it is animating between (i.e. rotating) 
	 * The finished state is when the engine has finished running
	 */
	public void update(){
		switch (state) {
			case active:
				int activeCount = updateBoardNextStep();
				score+=activeCount;
				
				if(activeCount > 0){
					state = ReactorState.running;
				} else {
					state = ReactorState.finished;
				}			
				animationStart=System.currentTimeMillis();
				break;
			case running:
				float time = (System.currentTimeMillis()-animationStart)/(float)animationTime;
				if(time >= 1){
					updateBoardProgressComplete();
					state = ReactorState.active;
				}else{
					updateBoradAnimationRunning(time);
				}
				
				break;
			case finished:
				break;
		}
	}
	
	public void activate(){
		state = ReactorState.running;
		animationStart=System.currentTimeMillis();
		score = 0;
	}

	public ReactorState getState() {
		return state;
	}

	public long getAnimationTime() {
		return animationTime;
	}

	public void setAnimationTime(long animationTime) {
		this.animationTime = animationTime;
	}

	public void dispose() {
		board = null;
	}
	
}
