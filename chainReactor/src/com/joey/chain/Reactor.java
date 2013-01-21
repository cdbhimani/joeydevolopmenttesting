package com.joey.chain;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;

public class Reactor {
	enum ReactorState{
		finished, //Complete Full Run
		running, //Propagating between rotations
		active //Finished between rotations but not complete
	}
	
	Chain[][] board;
	Random rand;
	ReactorState state = ReactorState.finished;
	
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
	}
	
	public void resetBorad(long seed){
		rand.setSeed(seed);
		for(int x = 0; x < board.length; x++){
			for(int y = 0; y < board[x].length; y++){
				board[x][y].setAngle(rand.nextInt(4)*90);
			}
		}
	}
	
	public void update(){
		switch (state) {
		case ReactorState.:
			
			break;

		default:
			break;
		}
	}
	
}
