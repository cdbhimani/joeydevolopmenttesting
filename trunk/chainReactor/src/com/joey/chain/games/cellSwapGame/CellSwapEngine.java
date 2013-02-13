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
	
	private Cell[][] board;
	private CellSwapEngineState state = CellSwapEngineState.finshed;
	private long animationStart = 0;
	private float animationTime = 250f;
	private int cellMatchCountMin = 3;
	
	private long score = 0;
	private int difficulty = 2;
	
	
	private boolean contineous = true;
	
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
		return attemptSwap(x, y, swap, false);
	}
		
		
	private boolean attemptSwap(int x, int y, SwapDirection swap, boolean isTest){
		int nX = x+swap.dx;
		int nY = y+swap.dy;
		
		if(!isValid(x, y) || !isValid(nX, nY)){
			return false;
		}
		
		Cell cellA = board[nX][nY];
		Cell cellB = board[x][y];
		
		board[nX][nY] = cellB;
		board[x][y] =cellA;
		
		boolean validSwap = false;
		boolean undoSwap = false;
		
		if(isSetCell(x, y, !isTest) || isSetCell(nX, nY, !isTest)){
			validSwap = true;
			undoSwap = false;
		}else{
			validSwap = false;
			undoSwap = true;
		}
		
		if(undoSwap || isTest){
			board[nX][nY] = cellA;
			board[x][y] =cellB;
		}
		
		if(!isTest){
			cellA.desiredPos.set(cellB.lastPos);
			cellA.calculateMovementRequired();
		
			cellB.desiredPos.set(cellA.lastPos);
			cellB.calculateMovementRequired();
		
			doAnimation();
		}
		return validSwap;
		
	}
	
	private boolean isSetCell(int x, int y, boolean flagIfValid){
		return getSetCellX(x, y, flagIfValid) >= cellMatchCountMin
				|| getSetCellY(x, y, flagIfValid) >= cellMatchCountMin;
	}
	private int getSetCellX(int x, int y, boolean flagIfValid){
		int xP,yP;
		int count = 0;
		int xLeft = 0;
		int xRight = 0;
		Cell cell = board[x][y];
		xP = x+1;
		yP = y;

		while(isValid(xP, yP) && isSameType(xP, yP, cell)){
			xRight++;
			xP++;
		}

		xP = x-1;
		yP = y;
		while(isValid(xP, yP) && isSameType(xP, yP, cell)){
			xLeft++;
			xP--;
		}
		
		count = 1+xLeft+xRight;
		if(flagIfValid || count >= cellMatchCountMin){
			for(xP = x-xLeft; xP<=x+xRight; xP++){
				board[xP][yP].flag();
			}
		}
		return count;
	}
	
	private int getSetCellY(int x, int y, boolean flagIfValid){
		int xP,yP;
		int yUp = 0;
		int yDown = 0;
		int count = 0;
		Cell cell = board[x][y];
		

		xP = x;
		yP = y+1;
		//Up
		while(isValid(xP, yP) && isSameType(xP, yP, cell)){
			yUp++;
			yP++;
		}
		
		xP = x;
		yP = y-1;
		while(isValid(xP, yP) && isSameType(xP, yP, cell)){
			yDown++;
			yP--;
		}
		
		count = 1+yUp+yDown;
		if(flagIfValid || count >= cellMatchCountMin){
			for(yP = y-yDown; yP<=y+yUp; yP++){
				board[xP][yP].flag();
			}
		}
		return count;
	}
	
	public void validateBoard(){
		int x = 0;
		int y = 0;
		boolean invalid = false;
		
		while(!invalid){
			x = 0;
			y = 0;
			invalid = false;
			while(x < getWidth()){
				while(y < getHeight()){
					if(!board[x][y].isFlaged()){
						if(isSetCell(x, y, true)){
							invalid = true;
						}
					}
				}
			}
		}
		
		if(invalid){
			killTagged();
		}
	}
	
	public void killTagged(){
		
	}
	
	public boolean isValid(int x, int y){
		return x>=0&&y>=0&&x<getWidth()&&y<getHeight()&&board[x][y].isAlive();
	}
	
	private boolean isSameType(int x, int y, Cell type){
		return board[x][y].equals(type);
	}
	public void createGrid(int sizeX, int sizeY){
		board = new Cell[sizeX][sizeY];

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
		boolean offset = false;
		int xOff = 0;
		
			
		for(int y = 0; y < getHeight(); y++){
			if(offset){
				xOff = 1;
			}else{
				xOff = 0;
			}
			offset = !offset;
			
			for(int x = xOff; x<getWidth(); x+=2){
				if(attemptSwap(x, y, SwapDirection.UP,    true))
					return true;
				
				if(attemptSwap(x, y, SwapDirection.DOWN,  true))
					return true;
				
				if(attemptSwap(x, y, SwapDirection.LEFT,  true))
					return true;
				
				if(attemptSwap(x, y, SwapDirection.RIGHT, true))
					return true;
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
		return movementNeededCount==0;
	}
	
	private void sortColumn(Cell[] data){
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

