package com.emptyPockets.backgrounds.grid2D;

import com.badlogic.gdx.math.Vector2;

public class DualNodeLink extends NodeLink{
	Node nodeA;
	Node nodeB;
	float stiffness;
	float damping;
	float initialDist;
	NodeLinkSettings cfg;
	Vector2 temp = new Vector2();
	float currentDist;

	public DualNodeLink(Node nodeA, Node nodeB, NodeLinkSettings cfg){
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.initialDist = nodeB.pos.dst(nodeA.pos);
		this.cfg = cfg;
	}

	public void solve(){
		temp.x = nodeB.pos.x-nodeA.pos.x;
		temp.y = nodeB.pos.y-nodeA.pos.y;
		currentDist = temp.len();
		
		if(currentDist < 0.01f){
			return;
		}
		temp.mul((initialDist-currentDist)/currentDist);
		temp.x = temp.x*cfg.stiffness-(nodeB.vel.x-nodeA.vel.x)*cfg.damping;
		temp.y = temp.y*cfg.stiffness-(nodeB.vel.y-nodeA.vel.y)*cfg.damping;
		
		nodeB.applyForce(temp);
		temp.mul(-1);
		nodeA.applyForce(temp);
	}
}
