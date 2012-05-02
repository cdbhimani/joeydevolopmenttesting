package com.joey.aitesting.game.entities;

import com.joey.aitesting.game.shapes.Vector2D;

public class Obstacle extends BaseGameEntity{

	
	public Obstacle(float x, float y, float radius){
		super(EntityTypes.OBSTACLE_TYPE,new Vector2D(x,y), radius);
	}
	
	public Obstacle(Vector2D pos, float radius){
		super(EntityTypes.OBSTACLE_TYPE,pos,radius);
	}
	@Override
	public void update(float time) {
		// TODO Auto-generated method stub
		
	}

}
