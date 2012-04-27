package com.joey.aitesting.game.shapes;

public class WorldWrapper {

	/**
	Flags for overlap
	*/
	public static int kMinusX = 1;
	public static int kMinusY = 2;
	public static int kPlusX = 4;
	public static int kPlusY = 8;
	
	public static  int testOverlapFlag( Rectangle2D reg, Rectangle2D world, Rectangle2D rst)
	{
		return 0;
	}
	 
	/**
	The overlap check itself
	*/
	public static  int testOverlapFlag( Rectangle2D reg, Rectangle2D world)
	{
		int flags = 0;
		boolean x1 = world.x1 < reg.x1 && world.x2 > reg.x1;
		boolean x2 = world.x1 < reg.x2 && world.x2 > reg.x2;
		boolean y1 = world.y1 < reg.y1 && world.y2 > reg.y1;
		boolean y2 = world.y1 < reg.y2 && world.y2 > reg.y2;
		if(!x1 || !x2){
			if(x1&&!x2){
				flags |= kPlusX;
			}else{
				flags |= kMinusX;
			}
		}
		if(!y1 || !y2){
			if(y1&&!y2){
				flags |= kPlusY;
			}else{
				flags |= kMinusY;
			}
		}
		return flags;
	}
}
