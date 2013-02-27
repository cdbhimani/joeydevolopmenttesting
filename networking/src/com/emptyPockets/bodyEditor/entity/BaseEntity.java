package com.emptyPockets.bodyEditor.entity;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;

public abstract class BaseEntity {
	ArrayList<Shape> shapes;
	
	public abstract Body createBody();
	
	public void getShape(){
		
	}
}
