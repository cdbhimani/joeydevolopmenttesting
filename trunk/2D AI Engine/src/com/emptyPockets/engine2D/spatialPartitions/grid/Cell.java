package com.emptyPockets.engine2D.spatialPartitions.grid;
//package com.joey.aitesting.game.cellSpace;
//
//import java.util.ArrayList;
//
//import com.joey.aitesting.game.entities.BaseGameEntity;
//import com.joey.aitesting.game.shapes.Rectangle2D;
//
//public class Cell<T extends BaseGameEntity> {
//	public int level;
//	public boolean isLeaf;
//	public Rectangle2D region;
//
//	public Cell<T> topLeft;
//	public Cell<T> topRight;
//	public Cell<T> bottomLeft;
//	public Cell<T> bottomRight;
//	
//	public Cell(Rectangle2D region, int level, int maxLevel){
//		this.region = region;	
//		this.level = level;
//		
//		if(level == maxLevel){
//			isLeaf = true;
//		}
//		else{
//			isLeaf = false;
//			addChild(maxLevel);
//		}
//	}
//	
//	public void addChild(int maxLevel){
//		topLeft = new Cell<T>(new Rectangle2D(region.x, region.y, region.sizeX/2, region.sizeY/2), level+1, maxLevel);
//		topRight = new Cell<T>(new Rectangle2D(region.x+region.sizeX/2, region.y, region.sizeX/2, region.sizeY/2), level+1, maxLevel);
//		bottomLeft = new Cell<T>(new Rectangle2D(region.x, region.y+region.sizeY/2, region.sizeX/2, region.sizeY/2), level+1, maxLevel);
//		bottomRight= new Cell<T>(new Rectangle2D(region.x+region.sizeX/2, region.y+region.sizeY/2, region.sizeX/2, region.sizeY/2), level+1, maxLevel);
//	}
//	
//	
//
//}
