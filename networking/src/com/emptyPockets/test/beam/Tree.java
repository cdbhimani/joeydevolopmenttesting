package com.emptyPockets.test.beam;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Tree {
	int details;
	Node start;
	Node end;
	
	ArrayList<Node> pool;
	
	private void create(int poolSize){
		start = new Node(new Vector2());
		end = new Node(new Vector2());
		
		pool = new ArrayList<Node>(poolSize);
		for(int i = 0;i < poolSize; i++){
			pool.add(new Node(new Vector2()));
		}
	}
	
	public void build(Vector2 p1, Vector2 p2, float maxDisp){
		
	}
	
	public static void drawLightning(ShapeRenderer shape,float x1,float y1,float x2,float y2,float displace, float curDetail)
	{
	  if (displace < curDetail) {
		  shape.line(x1, y1, x2, y2);
	  }
	  else {
	    float mid_x = (x2+x1)/2;
	    float mid_y = (y2+y1)/2;
	    mid_x += (Math.random()-.5)*displace;
	    mid_y += (Math.random()-.5)*displace;
	    drawLightning(shape,x1,y1,mid_x,mid_y,displace/2,curDetail);
	    drawLightning(shape,x2,y2,mid_x,mid_y,displace/2,curDetail);
	  }
	}
	
	public void clear(){
		Node current= start;
		Node next= null;
		while(current != end){
			next = start.child;
			current.child = null;
			pool.add(current);
			current = next;
		}
		end.child = null;
	}
	
}

class Node{
	Vector2 pos;
	Node child = null;
	
	public Node(Vector2 pos){
		this.pos = pos;
	}
	public boolean isLeaf(){
		return child==null;
	}
}
