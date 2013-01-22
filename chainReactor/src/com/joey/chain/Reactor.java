package com.joey.chain;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.joey.chain.Chain.ChainState;

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
	
	public void createBoard(int sizeX, int sizeY){
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
	
	public void updateBoradAnimationRunning(float progress){
		for(int x = 0; x < board.length; x++){
			for(int y = 0; y < board[x].length; y++){
				board[x][y].updateProgressRunning(progress);
			}
		}
	}
	
	public int updateBoardNextStep(){
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
	
	public void updateBoardProgressComplete(){
		for(int x = 0; x < board.length; x++){
			for(int y = 0; y < board[x].length; y++){
				board[x][y].updateProgressComplete();
			}
		}
				
	}
	
	public Chain[][] getBoard() {
		return board;
	}

	public void update(){
		//Gdx.app.debug(this.getClass().getName(), "State : "+state);
		
		switch (state) {
			case active:
				int activeCount = updateBoardNextStep();
				score+=activeCount;
				
				if(activeCount > 0){
					state = ReactorState.running;
				} else {
					Gdx.app.debug(getClass().getName(), "Final Score : "+score);
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
	
}
