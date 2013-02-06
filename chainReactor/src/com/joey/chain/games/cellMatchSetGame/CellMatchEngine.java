package com.joey.chain.games.cellMatchSetGame;

import java.util.Arrays;
import java.util.Comparator;

public class CellMatchEngine {

	public enum CellEngineState{
		waiting,
		animating,
		animatingFinished,
		calculating;
	}
	
	Comparator<Cell> sort = new Comparator<Cell>() {
		@Override
		public int compare(Cell o1, Cell o2) {
			if(o1.isAlive() && o2.isAlive()){
				return 0;
			}
			if(o2.isAlive() && !o1.isAlive()){
				return 1;
			}
			if(!o2.isAlive() && o1.isAlive()){
				return -1;
			}
			return 0;
		}
	};
	
	public Cell[][] grid;
	CellEngineState state = CellEngineState.waiting;
	long animationStart = 0;
	float animationTime = 200f;
	
	public CellMatchEngine(int sizeX, int sizeY){
		createGrid(sizeX, sizeY);
	}
	

	public int getWidth(){
		return grid.length;
	}
	
	public int getHeight(){
		return grid[0].length;
	}
	
	public synchronized void touch(int x, int y){
		if(state == CellEngineState.waiting){
			if(kill(x,y) < 3){
				undoKill(x, y);
			}
			activate();
		}
	}
	
	public void undoKill(int x, int y){
		if(grid[x][y].isAlive() == true){
			return;
		}
		
		grid[x][y].activate();
		
		if(x > 0){
			if(grid[x][y].equals(grid[x-1][y])){
				undoKill(x-1,y);
			}
		}
		if(x < getWidth()-1){
			if(grid[x][y].equals(grid[x+1][y])){
				undoKill(x+1,y);
			}
		}
		if(y > 0){
			if(grid[x][y].equals(grid[x][y-1])){
				undoKill(x,y-1);
			}
		}
		if(y < getHeight()-1){
			if(grid[x][y].equals(grid[x][y+1])){
				undoKill(x,y+1);
			}
		}
	}
	public int kill(int x, int y){
		if(grid[x][y].isAlive() == false){
			return 0;
		}
		
		int killCount = 1;
		grid[x][y].kill();
		
		if(x > 0){
			if(grid[x][y].equals(grid[x-1][y])){
				killCount+=kill(x-1,y);
			}
		}
		if(x < getWidth()-1){
			if(grid[x][y].equals(grid[x+1][y])){
				killCount+=kill(x+1,y);
			}
		}
		if(y > 0){
			if(grid[x][y].equals(grid[x][y-1])){
				killCount+=kill(x,y-1);
			}
		}
		if(y < getHeight()-1){
			if(grid[x][y].equals(grid[x][y+1])){
				killCount+=kill(x,y+1);
			}
		}
		return killCount;
	}
	public void createGrid(int sizeX, int sizeY){
		grid = new Cell[sizeX][sizeY];
		for(int x = 0; x < sizeX; x++){
			for(int y = 0;y < sizeY; y++){
				grid[x][y] = new Cell();
				grid[x][y].setPos(x,y);
				grid[x][y].random();
			}
		}
	}
	
	public void activate(){
		if(state == CellEngineState.waiting){
			animationStart = System.currentTimeMillis();
			state = CellEngineState.animating;
		}
	}
	
	
	
	private void computeUpdateStep(float prog){
		for(int x = 0; x <grid.length; x++){
			for(int y = 0; y < grid[x].length; y++){
				grid[x][y].update(prog);
			}
		}
	}
	
	private boolean computeUpdateStepComplete(){
		boolean alive = false;
		for(int x = 0; x <grid.length; x++){
			for(int y = 0; y < grid[x].length; y++){
				alive = grid[x][y].updateFinished()||alive;
			}
		}
		return alive;
	}
	
	private boolean computeRelayoutStep(){
		int movementNeededCount=0;
		int deadCount = 0;
		for(int x = 0; x < grid.length; x++){
			sortColumn(grid[x]);
			
			deadCount = 0;
			for(int y = 0; y < grid[x].length; y++){
				if(!grid[x][y].isAlive()){
					grid[x][y].random();
					grid[x][y].lastPos.set(x, getHeight()+deadCount);
					grid[x][y].currentPos.set(x, getHeight()+deadCount);
					deadCount++;
				}
				grid[x][y].desiredPos.set(x, y);
				
				if(!grid[x][y].calculateMovementRequired()){
					movementNeededCount++;
				}
			}
		}
		return movementNeededCount==0;
	}
	
	public void sortColumn(Cell[] data){
		Arrays.sort(data, this.sort);
		
	}
	
	public synchronized void update(){
		switch(state){
			case waiting:{
				break;
			}
			case calculating:{
				if(computeRelayoutStep()==true){
					state = CellEngineState.waiting;
				}else{
					state = CellEngineState.animating;
				}
				break;
			}
			case animatingFinished:{
				if(computeUpdateStepComplete() == true){
					state = CellEngineState.calculating;
				}else{
					state = CellEngineState.animating;
				}
				break;
			}
			case animating:{
				float time = (System.currentTimeMillis()-animationStart)/animationTime;
				if(time >= 1){
					state = CellEngineState.animatingFinished;
					//Set this a next animation time (smoothens out animation)
					animationStart = System.currentTimeMillis();
				}else{
					computeUpdateStep(time);
				}
				break;
			}
		}
	}
}

