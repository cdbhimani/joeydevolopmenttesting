package com.joey.chain.model.travel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.sun.media.sound.DataPusher;

public class GameGrid {

	public enum GridState{
		waiting,
		animating,
		animatingFinished,
		calculating;
	}
	
	Comparator<Cell> sort = new Comparator<Cell>() {
		@Override
		public int compare(Cell o1, Cell o2) {
			if(o1.alive && o2.alive){
				return 0;
			}
			if(o2.alive && !o1.alive){
				return 1;
			}
			if(!o2.alive && o1.alive){
				return -1;
			}
			return 0;
		}
	};
	
	
	Walker walk;
	public Cell[][] grid;
	GridState state = GridState.waiting;
	long animationStart = 0;
	float animationTime = 500f;
	
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
				grid[x][y].c = new Color(MathUtils.random(),MathUtils.random(),MathUtils.random(),1);
			}
		}
	}
	
	public void activate(){
		if(state == GridState.waiting){
			animationStart = System.currentTimeMillis();
			state = GridState.animating;
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
				if(!grid[x][y].alive){
					grid[x][y].alive = true;
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
				}
				break;
			}
			case animatingFinished:{
				if(computeUpdateStepComplete() == true){
					state = GridState.calculating;
				}else{
					state = GridState.animating;
				}
				break;
			}
			case animating:{
				float time = (System.currentTimeMillis()-animationStart)/animationTime;
				if(time >= 1){
					state = GridState.animatingFinished;
					//Set this a next animation time (smoothens out animation)
					animationStart = System.currentTimeMillis();
				}else{
					computeUpdateStep(time);
				}
				break;
			}
		}
	}
	
	public static void printGrid(Cell[][] data){
		ArrayList<String> out[][] = new ArrayList[data.length][data[0].length];
		int sX = data.length;
		int sY = data[0].length; 
		
		int maxWidth = 0;
		int maxRows = 0;
		int rowCount = 0;
		for(int x = 0; x < sX; x++){
			for(int y = 0; y < sY; y++){
				rowCount = 0;
				Cell c = data[x][y];
				out[x][y] = new ArrayList<String>();
				String dir = String.format("Dir:[%1.2f,%1.2f]", c.lastDir.x,c.lastDir.y);
				String pos = String.format("Pos:[%1.2f,%1.2f]", c.lastPos.x,c.lastPos.y);
				String cur = String.format("Cur:[%1.2f,%1.2f]", c.currentPos.x,c.currentPos.y);
				String des = String.format("Dst:[%1.2f,%1.2f]", c.desiredPos.x,c.desiredPos.y);
				
				maxWidth = Math.max(dir.length(), maxWidth);
				maxWidth = Math.max(pos.length(), maxWidth);
				maxWidth = Math.max(cur.length(), maxWidth);
				maxWidth = Math.max(des.length(), maxWidth);
				
				out[x][y].add(dir);rowCount++;
				out[x][y].add(pos);rowCount++;
				out[x][y].add(cur);rowCount++;
				out[x][y].add(des);rowCount++;
				
				maxRows = Math.max(maxRows, rowCount);
			}
		}
		
		System.out.println("\n\n\n");
		for(int y = 0; y < sY; y++){
			System.out.println("");
			for(int i = 0; i < 1000; i++){
				System.out.print("X");
			}
			System.out.print("\n|\t");
			
			for(int i = 0; i < maxRows; i++){		
				for(int x = 0; x < sX; x++){
					Cell c = data[x][y];
					ArrayList<String> strings = out[x][y];
					String s = strings.get(i);
					System.out.printf("%"+maxWidth+"s \t|\t", s);
				}
				System.out.print("\n|\t");
			}
		}
		System.out.println("\n\n\n");
	}
		
	
	public static void main(String input[]) throws InterruptedException{
		GameGrid game = new GameGrid(3,3);
		game.grid[0][0].alive=false;
		Cell c= game.grid[0][0];
		boolean first = true;
		while(true){
			game.update();
			
			printGrid(game.grid);
			Thread.sleep(500);
			if(first){
				first = false;
				game.activate();
			}
		}
		
	}
}

