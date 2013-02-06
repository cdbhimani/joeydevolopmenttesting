package com.joey.chain.games.cellMatchSetGame;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.math.MathUtils;

public class CellMatchEngine {

	public enum CellEngineState{
		finshed,
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
	
	private int[] horizontalRowSorter;
	private Cell[][] board;
	private CellEngineState state = CellEngineState.waiting;
	private long animationStart = 0;
	private float animationTime = 200f;
	
	private float scoreMultiplier = 1.6f;
	private long score = 0;
	private int lives = 10;
	private int minDestroy = 3;
	private int difficulty = 1;
	
	
	private boolean contineous = false;
	
	public CellMatchEngine(int sizeX, int sizeY){
		createGrid(sizeX, sizeY);
	}
	

	public int getWidth(){
		return board.length;
	}
	
	public int getHeight(){
		return board[0].length;
	}
	
	public synchronized void touch(int x, int y){
		if(state == CellEngineState.waiting){
			boolean killValid = false;
			if(getBoard()[x][y].isAlive()){
				int killCount = kill(x,y, true);
				if(killCount < minDestroy){
					if(getLives() > 0){
						killValid = true;
						setLives(getLives() - 1);	
					}
				}else{
					killValid = true;
				}
				
				if(killValid){
					kill(x,y, false);
					score+=Math.pow(scoreMultiplier,killCount);
				}
				activate();
			}
		}
	}
	
	public int kill(int x, int y, boolean onlyCount){
		
		int count = flag(x, y);
		if(onlyCount){
			unflagAll(false);
		}else{
			unflagAll(true);
		}
		return count;
	}
	
	private void unflagAll(boolean kill){
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0; y < getHeight(); y++){
				if(board[x][y].isFlaged()){
					if(kill){
						board[x][y].kill();
					}
					board[x][y].unFlag();
				}
			}
		}
		
	}
	
	public int flag(int x, int y){
		Cell cell = board[x][y];
		if(cell.isAlive() == false || cell.isFlaged()){
			return 0;
		}
		
		int flagCount = 1;
		cell.flag();
		
		if(x > 0){
			if(cell.equals(board[x-1][y])){
				flagCount+=flag(x-1,y);
			}
		}
		if(x < getWidth()-1){
			if(cell.equals(board[x+1][y])){
				flagCount+=flag(x+1,y);
			}
		}
		if(y > 0){
			if(cell.equals(board[x][y-1])){
				flagCount+=flag(x,y-1);
			}
		}
		if(y < getHeight()-1){
			if(cell.equals(board[x][y+1])){
				flagCount+=flag(x,y+1);
			}
		}
		return flagCount;
	}
	public void createGrid(int sizeX, int sizeY){
		board = new Cell[sizeX][sizeY];
		horizontalRowSorter = new int[sizeX];
		for(int x = 0; x < sizeX; x++){
			for(int y = 0;y < sizeY; y++){
				board[x][y] = new Cell();
				board[x][y].setPos(x,y);
				board[x][y].random(difficulty);
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
		for(int x = 0; x <board.length; x++){
			for(int y = 0; y < board[x].length; y++){
				board[x][y].update(prog);
			}
		}
	}
	
	private boolean computeUpdateStepComplete(){
		boolean alive = false;
		for(int x = 0; x <board.length; x++){
			for(int y = 0; y < board[x].length; y++){
				alive = board[x][y].updateFinished()||alive;
			}
		}
		return alive;
	}
	
	private boolean hasValidMovesRemain(){
		for(int x=0;x < getWidth(); x++){
			for(int y = 0; y < getHeight(); y++){
				int count = kill(x, y, true);
				if(count > minDestroy && lives > 0){
					return true;
				}
			}
		}
		return false;
	}
	private boolean computeRelayoutStep(){
		int movementNeededCount=0;
		int deadCount = 0;
		for(int x = 0; x < board.length; x++){
			sortColumn(board[x]);
			
			deadCount = 0;
			for(int y = 0; y < board[x].length; y++){
				if(!board[x][y].isAlive()){
					if(contineous){
						board[x][y].random(difficulty);
						board[x][y].lastPos.set(x, getHeight()+deadCount);
						board[x][y].currentPos.set(x, getHeight()+deadCount);
					}
					else{
						board[x][y].lastPos.set(x, y);
						board[x][y].currentPos.set(x, y);
					}
					deadCount++;
				}
				board[x][y].desiredPos.set(x, y);
				
				if(!board[x][y].calculateMovementRequired()){
					movementNeededCount++;
				}
			}
		}
		
		if(movementNeededCount==0 && contineous == false){//Downward motion complete do right movement
			boolean needsTest =  false;
			boolean firstEmpty = false;
			//Check order of rows
			for(int x= getWidth()-1; x >= 0; x--){
				horizontalRowSorter[x] = x+1;
				//Check bottom row empty (should only need to do bottom row)
				if(!board[x][0].isAlive()){
					horizontalRowSorter[x] *= -1;
					firstEmpty = true;
				}else{//If it finds a full after the first empty grid must be sorted. 
					if(firstEmpty){
						needsTest=true;
					}
				}
				
			}
			
			if(needsTest){//Only sort array when needed
				Arrays.sort(horizontalRowSorter);
				
				Cell[][] tmpBoard = new Cell[getWidth()][];
				for(int x = 0; x < getWidth(); x++){
					movementNeededCount++;
					if(horizontalRowSorter[x] > 0){
						tmpBoard[x] = board[horizontalRowSorter[x]-1];
					}else{
						tmpBoard[x] = board[(-horizontalRowSorter[x])-1];
					}
				}
				board = tmpBoard;
			}
		}
		
		return movementNeededCount==0;
	}
	
	public void sortColumn(Cell[] data){
		Arrays.sort(data, this.sort);
		
	}
	
	public synchronized void update(){
		switch(state){
			case finshed:{
				break;
			}
			case waiting:{
				break;
			}
			case calculating:{
				if(computeRelayoutStep()==true){
					if(hasValidMovesRemain()){
						state = CellEngineState.waiting;
					}else{
						state = CellEngineState.finshed;
					}
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


	public Cell[][] getBoard() {
		return board;
	}


	public long getScore() {
		return score;
	}


	public void setScore(long score) {
		this.score = score;
	}


	public int getLives() {
		return lives;
	}


	public void setLives(int lives) {
		this.lives = lives;
	}


	public boolean isFinished() {
		// TODO Auto-generated method stub
		return state == CellEngineState.finshed;
	}
}

