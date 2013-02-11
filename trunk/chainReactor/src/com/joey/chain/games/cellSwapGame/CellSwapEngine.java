package com.joey.chain.games.cellSwapGame;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.math.MathUtils;

public class CellSwapEngine {

	public enum CellSwapEngineState{
		finshed,
		waiting,
		animating,
		animatingFinished,
		calculating;
	}
	
	public enum SwapDirection{
		UP(0, 1),
		DOWN(0, -1),
		LEFT(-1, 0),
		RIGHT(1,0);
		
		int dx;
		int dy;
		
		private SwapDirection(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
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
	private CellSwapEngineState state = CellSwapEngineState.finshed;
	private long animationStart = 0;
	private float animationTime = 100f;
	
	private long score = 0;
	private int difficulty = 2;
	
	
	private boolean contineous = false;
	
	public CellSwapEngine(int sizeX, int sizeY){
		createGrid(sizeX, sizeY);
		reset();
	}
	
	public void reset(){
		difficulty = 3;
		score=0;
		randomize();
		doRecalculation();
	}

	public int getWidth(){
		return board.length;
	}
	
	public int getHeight(){
		return board[0].length;
	}
	
	public synchronized boolean touch(int x, int y, SwapDirection swap){
		if(state != CellSwapEngineState.waiting){
			return false;
		}
		if(isValidMove(x, y, swap)){
			swap(x, y, swap);
			doRecalculation();
			return true;
		}else{
			Cell dst = board[x+swap.dx][y+swap.dy];
			Cell src = board[x][y];
			
			dst.desiredPos.set(src.lastPos);
			src.desiredPos.set(dst.lastPos);
			doAnimation();
			return false;
		}
		
	}
	
	private void swap(int x, int y, SwapDirection swap){	
		Cell tmp = board[x+swap.dx][y+swap.dy];
		board[x+swap.dx][y+swap.dy] = board[x][y];
		board[x][y] = tmp;
	}
	
	private boolean isValidMove(int x, int y, SwapDirection swap){
		return true;
	}
	
	public void createGrid(int sizeX, int sizeY){
		board = new Cell[sizeX][sizeY];
		horizontalRowSorter = new int[sizeX];

		for(int x = 0; x < getWidth(); x++){
			for(int y = 0;y < getHeight(); y++){
				board[x][y] = new Cell();
			}
		}
	}
	
	public void randomize(){
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0;y < getHeight(); y++){
				board[x][y].setPos(x,y);
				board[x][y].random(difficulty);
			}
		}
	}
	
	private void doRecalculation(){
		if(state == CellSwapEngineState.finshed || state == CellSwapEngineState.waiting){
			animationStart = System.currentTimeMillis();
			state = CellSwapEngineState.calculating;
		}
	}

	private void doAnimation(){
		if(state == CellSwapEngineState.finshed || state == CellSwapEngineState.waiting){
			animationStart = System.currentTimeMillis();
			state = CellSwapEngineState.animating;
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
		return true;
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
		System.out.println("Movement : "+movementNeededCount);
		return movementNeededCount==0;
	}
	
	public void sortColumn(Cell[] data){
		Arrays.sort(data, this.sort);
		
	}
	
	public synchronized void update(){
		switch(state){
			case finshed:{
			
			}
			case waiting:{
				break;
			}
			case calculating:{
				if(computeRelayoutStep()==true){
					if(hasValidMovesRemain()){
						state = CellSwapEngineState.waiting;
					}else{
						state = CellSwapEngineState.finshed;
					}
				}else{
					state = CellSwapEngineState.animating;
				}
				break;
			}
			case animatingFinished:{
				if(computeUpdateStepComplete() == true){
					state = CellSwapEngineState.calculating;
				}else{
					state = CellSwapEngineState.animating;
				}
				break;
			}
			case animating:{
				float time = (System.currentTimeMillis()-animationStart)/animationTime;
				if(time >= 1){
					state = CellSwapEngineState.animatingFinished;
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

	public boolean isFinished() {
		// TODO Auto-generated method stub
		return state == CellSwapEngineState.finshed;
	}
	
	public static void main(String input[]){
		int x = 1;
		int y = 1;
		
		CellSwapEngine engine = new CellSwapEngine(3, 3);
		engine.touch(x, y, SwapDirection.RIGHT);
		engine.touch(x, y, SwapDirection.LEFT);
		engine.touch(x, y, SwapDirection.UP);
		engine.touch(x, y, SwapDirection.DOWN);
	}
}

