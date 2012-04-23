package com.me.mygdxgame.spatialPartitioning;

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

public class QuadTree{
	public QuadTreeNode root;
	public ArrayList<Entity2D> points = new ArrayList<Entity2D>();
	
	public QuadTree(Rectangle2D r, int maxCount){
		root = new QuadTreeNode(r, 2);
	}

	public void addPoint(Entity2D p){
		synchronized (points) {
			points.add(p);
			root.addPoint(p);	
		}
	}
	
	public void rebuild(){
		root.reset();
		for(Entity2D p : points){
			root.addPoint(p);
		}
	}

	public ArrayList<Entity2D> getPointsInRegion(Rectangle2D region){
		ArrayList<Entity2D> ents = new ArrayList<Entity2D>();	
		root.getPointsInRegion(region, ents);
		return ents;
	}
}

