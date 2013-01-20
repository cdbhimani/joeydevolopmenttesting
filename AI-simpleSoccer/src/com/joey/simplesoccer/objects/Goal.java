package com.joey.simplesoccer.objects;

import com.joey.aitools.math.Vector2D;

public class Goal {
	Vector2D leftPost;
	Vector2D rightPost;
	Vector2D facing;
	Vector2D center;
	
	int numGoalsScored;
	
	public Goal(Vector2D left, Vector2D right){
		leftPost = new Vector2D(left);
		rightPost = new Vector2D(right);
		
		//center = (left+right)/2
		center = new Vector2D(left);
		center.add(right);
		center.mul(1/2f);
		
		//facing = |right-left|.perp
		facing=new Vector2D(right);
		facing.sub(left);
		facing.normalise();
		facing.setPerp();
	}
	
	public boolean Scored(SoccerBall ball){
		return false;
	}
}
