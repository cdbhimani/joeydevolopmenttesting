package com.joey.aitesting.game.entities;

import com.joey.aitesting.game.GameWorld;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.joey.aitesting.game.shapes.Vector2D;

public class Wall2D {
	// Do not make public as normal needs to be updated
	public Vector2D p1;
	public Vector2D p2;
	public Vector2D vN;
	public boolean useFlipNormal = false;
	
	public Wall2D() {
		p1 = new Vector2D();
		p2 = new Vector2D();
		vN = new Vector2D();
	}

	public Wall2D(Vector2D p1, Vector2D p2) {
		this();
		setP1(p1);
		setP2(p2);
	}

	public void calculateNormal() {
		vN.x = p2.x - p1.x;
		vN.y = p2.y - p1.y;
		
		vN.setPerp();
		vN.normalise();
		if(useFlipNormal){
			vN.flip();
		}
	}

	public Vector2D getP1() {
		return p1;
	}

	public void setP1(Vector2D vA) {
		this.p1.setLocation(vA);
		calculateNormal();
	}

	public Vector2D getP2() {
		return p2;
	}

	public void setP2(Vector2D vB) {
		this.p2.setLocation(vB);
		calculateNormal();
	}

	public Vector2D getvN() {
		return vN;
	}
	
	
	public static void addRectangle(GameWorld world, Rectangle2D rec, float inset, boolean inside){
		Wall2D top = new Wall2D();	
		Wall2D bottom = new Wall2D();
		Wall2D left = new Wall2D();
		Wall2D right = new Wall2D();
		
		Rectangle2D r = rec.clone();
		r.inset(inset);
		
		bottom.p1.x = r.x1;
		bottom.p1.y = r.y1;
		bottom.p2.x = r.x2;
		bottom.p2.y = r.y1;
		
		top.p1.x = r.x1;
		top.p1.y = r.y2;
		top.p2.x = r.x2;
		top.p2.y = r.y2;
		top.useFlipNormal = true;
		
		left.p1.x = r.x1;
		left.p1.y = r.y1;
		left.p2.x = r.x1;
		left.p2.y = r.y2;
		left.useFlipNormal = true;
		
		right.p1.x = r.x2;
		right.p1.y = r.y1;
		right.p2.x = r.x2;
		right.p2.y = r.y2;
		
		if(!inside){
			left.useFlipNormal=!left.useFlipNormal;
			right.useFlipNormal=!right.useFlipNormal;
			top.useFlipNormal=!top.useFlipNormal;
			bottom.useFlipNormal=!bottom.useFlipNormal;
		}
		
		bottom.calculateNormal();
		top.calculateNormal();
		left.calculateNormal();
		right.calculateNormal();
		
		world.addWall(bottom);
		world.addWall(top);
		world.addWall(left);
		world.addWall(right);
	}
}
