package com.emptyPockets.backgrounds.grid;

import com.badlogic.gdx.math.Vector2;

public class DualNodeLink extends NodeLink{
	Node nodeA;
	Node nodeB;
	float stiffness;
	float damping;
	float initialDist;

	Vector2 temp = new Vector2();
	float currentDist;

	public DualNodeLink(Node nodeA, Node nodeB, float stiffness, float damping){
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.initialDist = nodeB.pos.dst(nodeA.pos);
		this.stiffness = stiffness;
		this.damping = damping;
	}

	public void solve(){
		temp.x = nodeB.pos.x-nodeA.pos.x;
		temp.y = nodeB.pos.y-nodeA.pos.y;
		currentDist = temp.len();
		if(currentDist <= initialDist){
			return;
		}
		if(currentDist < 1e-4f){
			temp.x = 0;
			temp.y = 0;
		}else{
			temp.mul((initialDist-currentDist)/currentDist);
		}
		
		temp.x = temp.x*stiffness-(nodeA.vel.x-nodeB.vel.x)*damping;
		temp.y = temp.y*stiffness-(nodeA.vel.y-nodeB.vel.y)*damping;
		
		nodeB.applyForce(temp);
		temp.mul(-1);
		nodeA.applyForce(temp);
	}
}
