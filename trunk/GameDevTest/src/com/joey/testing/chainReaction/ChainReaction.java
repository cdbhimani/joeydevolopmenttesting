package com.joey.testing.chainReaction;

import java.util.Random;

public class ChainReaction {
	Random random = new Random();
	public Cell[][] cell;
	public ChainReactionState state = ChainReactionState.waiting;
	
	public ChainReaction(int sizeX, int sizeY){
		setCellSize(sizeX, sizeY);
	}
	
	public void update(){
		
	}
	
	public void updateCells(){
		int activeCount=0;
		for(int x = 0; x < cell.length; x++){
			for(int y = 0; y < cell[x].length; y++){
				if(cell[x][y].update()){
					activeCount++;
				}
			}
		}
		if(activeCount == 0){
			state = ChainReactionState.waiting;
		} else{
			state = ChainReactionState.running;
		}
	}
	
	public void setCellSize(int sizeX, int sizeY){
		cell = null;
		cell = new Cell[sizeX][sizeY];
		
		for(int x= 0; x < sizeX; x++){
			for(int y = 0; y < sizeY; y++){
				cell[x][y] = new Cell();
			}
		}
	}
	
	public void reset(int seed){
		random.setSeed(seed);
		for(int x = 0; x < cell.length; x++){
			for(int y = 0; y < cell[x].length; y++){
				switch(random.nextInt(4)){
					case 0 : cell[x][y].angle = 0; break;
					case 1 : cell[x][y].angle = 90; break;
					case 2 : cell[x][y].angle = 180; break;
					case 3 : cell[x][y].angle = 270; break;
				}
			}
		}
	}
}

class Cell{
	public boolean active = true;
	public CellState state = CellState.waiting;
	public int angle = 0;

	public boolean update(){
		if(active){
			angle++;
		}
		
		if(angle % 90 == 0){
			state = CellState.waiting;
		}
		if(angle >=360){
			angle = 0;
		}
		
		return active;
	}
}

enum CellState{
	itterating,
	waiting
}

enum ChainReactionState{
	processing,
	
	waiting;
}
