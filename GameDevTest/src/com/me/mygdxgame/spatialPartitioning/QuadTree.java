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
	ArrayList<Point2D> points = new ArrayList<Point2D>();
	
	public QuadTree(Rectangle2D r, int maxCount){
		root = new QuadTreeNode(r, 2);
	}

	public void addPoint(Point2D p){
		synchronized (points) {
			points.add(p);
			root.addPoint(p);	
		}
	}
	
	public void rebuild(){
		root.reset();
		for(Point2D p : points){
			root.addPoint(p);
		}
	}

	public ArrayList<Point2D> getPointsInRegion(Rectangle2D region){
		ArrayList<Point2D> ents = new ArrayList<Point2D>();	
		root.getPointsInRegion(region, ents);
		return ents;
	}
}

