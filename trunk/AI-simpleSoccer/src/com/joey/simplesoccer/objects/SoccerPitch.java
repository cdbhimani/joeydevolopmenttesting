package com.joey.simplesoccer.objects;

import java.util.Vector;

import com.joey.aitools.entity.Wall2D;

public class SoccerPitch {
	public float friction = 0.1f;
	SoccerBall ball;
	
	SoccerTeam redTeam;
	SoccerTeam blueTeam;
	
	Goal redGoal;
	Goal blueGoal;
	
	Vector<Wall2D> walls;
	Region playingArea;
	
	Vector<Region> playRegions;
	
	boolean gameOn;
	boolean goalKeeperHasBall;
	
	public SoccerPitch(int xClient, int yClient){
	}
	
	public void update(){
	}
	
	public void render(){
	}
	
	
}
