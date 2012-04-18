package com.joey.testing.game;

import com.joey.testing.game.shapes.Vector2D;

public class Wall2D {
    //Do not make public as normal needs to be updated
    Vector2D p1;
    Vector2D p2;
    Vector2D vN;

    public Wall2D() {
	p1 = new Vector2D();
	p2 = new Vector2D();
	vN = new Vector2D();
    }
    
    public Wall2D(Vector2D p1, Vector2D p2){
	this();
	setP1(p1);
	setP2(p2);
    }

    public void calculateNormal() {
	vN.x = p2.x - p1.x;
	vN.y = p2.y - p2.y;
	vN.setPerp();
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
}
