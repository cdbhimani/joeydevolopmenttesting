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
	
	private Cell[][] board;
	private CellEngineState state = CellEngineState.waiting;
	private long animationStart = 0;
	private float animationTime = 200f;
	
	private long score = 0;
	private long lives = 10;
	private long minDestroy = 3;
	private long difficulty = 4;
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
			int killCount = kill(x,y);
			if(killCount < 3){
				
				if(getLives() > 0){
					setLives(getLives() - 1);
				}else{
					undoKill(x, y);
				}
			}
			score+=Math.pow(3,killCount);
			activate();
		}
	}
	
	public void undoKill(int x, int y){
		if(board[x][y].isAlive() == true){
			return;
		}
		
		board[x][y].activate();
		
		if(x > 0){
			if(board[x][y].equals(board[x-1][y])){
				undoKill(x-1,y);
			}
		}
		if(x < getWidth()-1){
			if(board[x][y].equals(board[x+1][y])){
				undoKill(x+1,y);
			}
		}
		if(y > 0){
			if(board[x][y].equals(board[x][y-1])){
				undoKill(x,y-1);
			}
		}
		if(y < getHeight()-1){
			if(board[x][y].equals(board[x][y+1])){
				undoKill(x,y+1);
			}
		}
	}
	public int kill(int x, int y){
		if(board[x][y].isAlive() == false){
			return 0;
		}
		
		int killCount = 1;
		board[x][y].kill();
		
		if(x > 0){
			if(board[x][y].equals(board[x-1][y])){
				killCount+=kill(x-1,y);
			}
		}
		if(x < getWidth()-1){
			if(board[x][y].equals(board[x+1][y])){
				killCount+=kill(x+1,y);
			}
		}
		if(y > 0){
			if(board[x][y].equals(board[x][y-1])){
				killCount+=kill(x,y-1);
			}
		}
		if(y < getHeight()-1){
			if(board[x][y].equals(board[x][y+1])){
				killCount+=kill(x,y+1);
			}
		}
		return killCount;
	}
	public void createGrid(int sizeX, int sizeY){
		board = new Cell[sizeX][sizeY];
		for(int x = 0; x < sizeX; x++){
			for(int y = 0;y < sizeY; y++){
				board[x][y] = new Cell();
				board[x][y].setPos(x,y);
				board[x][y].random();
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
				int count = kill(x, y);
				undoKill(x, y);
				if(count > minDestroy){
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
						board[x][y].random();
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


	public long getLives() {
		return lives;
	}


	public void setLives(long lives) {
		this.lives = lives;
	}


	public boolean isFinished() {
		// TODO Auto-generated method stub
		return state == CellEngineState.finshed;
	}
}

