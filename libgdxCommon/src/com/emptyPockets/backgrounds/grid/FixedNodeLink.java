package com.emptyPockets.backgrounds.grid;

import com.badlogic.gdx.math.Vector2;

public class FixedNodeLink extends NodeLink{
	Node node;
	Vector2 pos;
	float stiffness;
	float damping;
	float initialDist;

	Vector2 temp = new Vector2();
	float currentDist;

	public FixedNodeLink(Node node, Vector2 fixPoint, float stiffness, float damping){
		this.node = node;
		this.pos = fixPoint;
		this.initialDist = node.pos.dst(this.pos);
		this.stiffness = stiffness;
		this.damping = damping;
	}

	public void solve(){
		temp.x = node.pos.x-pos.x;
		temp.y = node.pos.y-pos.y;
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
		
		temp.x = temp.x*stiffness-(node.vel.x)*damping;
		temp.y = temp.y*stiffness-(node.vel.y)*damping;
		
		node.applyForce(temp);
	}
	
	public static void main(String input[]){
		Node node = new Node();
		node.pos.x = 10;
		node.inverseMass = 1;
		
		Vector2 pos = new Vector2(0,0);
		
		FixedNodeLink link = new FixedNodeLink(node, pos, 1, 1);
		
		node.pos.x = 20;
		while(true){
			System.out.printf("%3.3f\n",node.pos.x);
			link.solve();
			node.update(1);
		}
	}
}
