package com.joey.chain.games.cellRotateChainGame;

import java.util.Random;

public class RotateCellEngine {
	enum EngineState{
		waiting, //Complete Full Run
		animating, //Propagating between rotations
		calculating //Finished between rotations but not complete
	}
	
	private long animationTime = 500;
	private long animationStart = 0;	
	private Chain[][] board;
	private Random rand;
	private EngineState state = EngineState.waiting;
	long score = 0;
	
	public long getScore() {
		return score;
	}

	private void setScore(long score) {
		this.score = score;
	}

	public RotateCellEngine(){
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

	public int getWidth(){
		return board.length;
	}
	
	public int getHeight(){
		return board[0].length;
		
	}
	public void tap(int x, int y){
		if(state == EngineState.waiting){
			if(x >= 0 && y >= 0 && x < getWidth() && y < getHeight()){
				board[x][y].activate();
				activate();
			}
		}
	}
	/**
	 * The following is the update state machine of the game. 
	 * The active state is when the animations have complete and it is doing calculation
	 * The running state is when it is animating between (i.e. rotating) 
	 * The finished state is when the engine has finished running
	 */
	public void update(){
		switch (state) {
			case calculating:
				int activeCount = updateBoardNextStep();
				score+=activeCount;
				
				if(activeCount > 0){
					state = EngineState.animating;
				} else {
					state = EngineState.waiting;
				}			
				animationStart=System.currentTimeMillis();
				break;
			case animating:
				float time = (System.currentTimeMillis()-animationStart)/(float)animationTime;
				if(time >= 1){
					updateBoardProgressComplete();
					state = EngineState.calculating;
				}else{
					updateBoradAnimationRunning(time);
				}
				
				break;
			case waiting:
				break;
		}
	}
	
	private void activate(){
		state = EngineState.animating;
		animationStart=System.currentTimeMillis();
		score = 0;
	}

	public EngineState getState() {
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
