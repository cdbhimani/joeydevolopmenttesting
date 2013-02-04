package com.joey.chain.model.travel;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.math.MathUtils;

public class GameGrid {

	public enum GridState{
		waiting,
		animating, 
		calculating;
	}
	
	Comparator<Cell> sort = new Comparator<Cell>() {
		@Override
		public int compare(Cell o1, Cell o2) {
			if(o1.alive && o2.alive){
				return 0;
			}
			if(o2.alive && !o1.alive){
				return -1;
			}
			if(!o2.alive && o1.alive){
				return +1;
			}
			return 0;
		}
	};
	
	
	Walker walk;
	public Cell[][] grid;
	GridState state = GridState.waiting;
	long animationStart = 0;
	float animationTime = 1000f;
	
	public int getWidth(){
		return grid.length;
	}
	
	public int getHeight(){
		return grid[0].length;
	}
	
	public GameGrid(int sizeX, int sizeY){
		createGrid(sizeX, sizeY);
	}
	
	public void createGrid(int sizeX, int sizeY){
		grid = new Cell[sizeX][sizeY];
		for(int x = 0; x < sizeX; x++){
			for(int y = 0;y < sizeY; y++){
				grid[x][y] = new Cell();
				grid[x][y].alive = MathUtils.randomBoolean();
				grid[x][y].setPos(x,y);
			}
		}
	}
	
	public void activate(){
		animationStart = System.currentTimeMillis();
		state = GridState.animating;
	}
	
	public void update(){
		switch(state){
			case waiting:{
				break;
			}
			case calculating:{
				if(computeRelayoutStep()==true){
					state = GridState.waiting;
				}else{
					state = GridState.animating;
					animationStart = System.currentTimeMillis();
				}
				break;
			}
			case animating:{
				float time = (System.currentTimeMillis()-animationStart)/animationTime;
				if(time >= 1){
					if(computeUpdateStepComplete() == true){
						state = GridState.calculating;
					}else{
						state = GridState.animating;
					}
				}else{
					computeUpdateStep(time);
				}
				break;
			}
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
				alive = alive || grid[x][y].updateFinished();
			}
		}
		return alive;
	}
	
	private boolean computeRelayoutStep(){
		int invalidCount=0;
		int deadCount = 0;
		for(int x = 0; x < grid.length; x++){
			sortColumn(grid[x]);
			
			deadCount = 0;
			for(int y = 0; y < grid[x].length; y++){
				if(!grid[x][y].alive){
					grid[x][y].alive = true;
					grid[x][y].currentPos.set(x, getHeight()+deadCount++);
				}
				grid[x][y].desiredPos.set(x, y);
				
				if(grid[x][y].desiredPos.epsilonEquals(grid[x][y].currentPos, 0.5f)){
					invalidCount++;
				}
			}
		}
		return invalidCount==0;
	}
	
	public void sortColumn(Cell[] data){
		Arrays.sort(data, this.sort);
		
	}
}
