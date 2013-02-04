package com.joey.chain.model.travel;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.math.MathUtils;

public class GameGrid {

	Walker walk;
	Cell[][] grid;
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
			}
		}
	}
	private void updateStep(float prog){
		
	}
	
	private void computeStep(){
		
	}
	
	private void computeRelayout(){
		for(int x = 0; x < grid.length; x++){
			sortColumn(grid[x]);
			
			for(int y = 0; y < grid[x].length; y++){
				if(grid[x][y].alive){
					g
				}
			}
		}
	}
	
	public void sortColumn(Cell[] data){
		Arrays.sort(data, this.sort);
		
	}
	public void update(){
		computeRelayout();
	}
	
	public static void main(String input[]){
		
		GameGrid grid = new GameGrid(10,10);
		grid.update();
			
		
		for(int y = 0; y < grid.getHeight(); y++){
			for(int x = 0; x < grid.getWidth(); x++){
				System.out.print(grid.grid[x][y].alive?'O':'X');
			}
			System.out.println();
		}
		
		
		
	}
}
