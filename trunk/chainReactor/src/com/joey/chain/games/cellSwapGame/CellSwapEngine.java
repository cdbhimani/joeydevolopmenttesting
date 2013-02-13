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
	private int difficulty = 0;
	
	
	private boolean contineous = true;
	
	public CellSwapEngine(int sizeX, int sizeY){
		createGrid(sizeX, sizeY);
		reset();
	}
	
	public void reset(){
		difficulty = 5;
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
		System.out.println("Attempt Swap");
		if(isScoreableCell(x, y, !isTest) || isScoreableCell(nX, nY, !isTest)){
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
	
	private boolean isScoreableCell(int x, int y, boolean flagIfValid){
		return getScoreCellX(x, y, flagIfValid) >= cellMatchCountMin
				|| getScoreCellY(x, y, flagIfValid) >= cellMatchCountMin;
	}
		
	private int getScoreCellX(int x, int y, boolean flagIfValid){
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
		if(flagIfValid && count >= cellMatchCountMin){
			for(xP = x-xLeft; xP<=x+xRight; xP++){
				System.out.println("Flagged"+xP+","+yP);
				board[xP][yP].flag();
			}
		}
		return count;
	}
	
	private int getScoreCellY(int x, int y, boolean flagIfValid){
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
		if(flagIfValid && count >= cellMatchCountMin){
			for(yP = y-yDown; yP<=y+yUp; yP++){
				System.out.println("Flagged"+xP+","+yP);
				board[xP][yP].flag();
			}
		}
		return count;
	}
	
	public int validateBoard(){
		System.out.println("Validate Board");
		boolean scoreFound = false;
		for(int x = 0; x < getWidth(); x++){
			for(int y = 0; y < getHeight(); y++){
				System.out.println("Testing : "+x+","+y);
				if(!board[x][y].isFlaged()){
					
					if(isScoreableCell(x, y, true)){
						System.out.println("Not Flagged");
						scoreFound = true;
					}
				}else{
					System.out.println("Flagged");
				}
			}
			
		}
		System.out.println("Checking Score : "+scoreFound);
		return killTagged();
	}
	
	public int killTagged(){
		int count = 0;
		for(int x=0; x < getWidth(); x++){
			for(int y = 0; y < getHeight(); y++){
				if(board[x][y].isFlaged()){
					board[x][y].kill();
					board[x][y].unFlag();
					count++;
				}
			}
		}
		return count;
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
				//Ensure first screen has no matches
				while(isScoreableCell(x, y, false)){
					board[x][y].random(difficulty);
				}
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
					int clearCount = validateBoard();
					if(clearCount == 0){
						if(hasValidMovesRemain()){
							state = CellSwapEngineState.waiting;
						}else{
							state = CellSwapEngineState.finshed;
						}
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

		CellSwapEngine engine = new CellSwapEngine(5, 5);
		for(int x= 0; x < engine.getWidth(); x++){
			for(int y = 0;y < engine.getHeight(); y++){
				System.out.print(engine.getBoard()[x][y].getTypeName()+"\t,\t");
			}
			System.out.println();
		}
		while(true){
			System.out.println("Updating");
			engine.update();
		}
	}
}

