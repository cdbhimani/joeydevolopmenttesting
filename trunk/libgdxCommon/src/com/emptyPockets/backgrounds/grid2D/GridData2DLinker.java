package com.emptyPockets.backgrounds.grid2D;

import java.util.ArrayList;

public class GridData2DLinker {
	static enum GridJoinDirection{
		UP,
		DOWN,
		LEFT,
		RIGHT
		
	}
	GridData2D gridA;
	GridData2D gridB;
	GridJoinDirection direction;
	ArrayList<DualNodeLink> nodes = new ArrayList<DualNodeLink>();
	
	public GridData2DLinker(GridData2D gridA, GridData2D gridB, GridJoinDirection direction){
		
	}
	
	public void solve(){
		
	}
}
