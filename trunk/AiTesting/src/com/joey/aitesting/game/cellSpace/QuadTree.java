package com.joey.aitesting.game.cellSpace;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.shapes.Rectangle2D;

public class QuadTree<T extends BaseGameEntity>{
	public QuadTreeNode<T> root;
	public ArrayList<T> points = new ArrayList<T>();
	public Rectangle2D worldBounds;
	
	public QuadTree(Rectangle2D r, int maxCount){
		setWorldSize(r,maxCount);
	}

	public void setWorldSize(Rectangle2D r, int maxCount){
		if(root != null){
			root.dispose();
			root = null;
		}
		this.worldBounds = r;
		root = new QuadTreeNode<T>(r, maxCount);
	}
	
	public void addEntity(T p){
		synchronized (points) {
			points.add(p);
			root.addEntity(p);	
		}
	}
	
	public void removeEntity(T p){
		synchronized (points) {
			points.add(p);
			root.addEntity(p);	
		}
	}
	
	public void rebuild(){
		root.reset();
		for(T p : points){
			root.addEntity(p);
		}
	}

	public ArrayList<T> getPointsInRegion(Rectangle2D region){
		ArrayList<T> ents = new ArrayList<T>();	
		root.getPointsInRegion(region, ents);
		return ents;
	}
}

