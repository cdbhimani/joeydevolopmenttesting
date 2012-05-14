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
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.joey.aitesting.game.entities.BaseGameEntity;
import com.joey.aitesting.game.shapes.Rectangle2D;

public class QuadTree<T extends BaseGameEntity> {
	QuadTreeNode<T> root;
	ArrayList<T> entities = new ArrayList<T>();
	Rectangle2D worldBounds;

	float minDivision = 0.1f;
	
	public QuadTree(Rectangle2D r, int maxCount) {
		setWorldSize(r, maxCount);
	}

	public void setWorldSize(Rectangle2D r, int maxCount) {
		if (root != null) {
			root.dispose();
			root = null;
		}
		this.worldBounds = r;
		root = new QuadTreeNode<T>(this, r, maxCount);
	}

	public void addEntity(T p) {
		synchronized (entities) {
			entities.add(p);
			root.addEntity(p);
		}
	}

	public void removeEntity(T p) {
		synchronized (entities) {
			entities.remove(p);
		}
	}

	public void rebuild() {
		synchronized (entities) {
			root.reset();
			for (T p : entities) {
				root.addEntity(p);
			}
		}
	}
	
	public QuadTreeNode<T> getRootNode(){
		return root;
	}

	public void getPointsInRegion(Rectangle2D region, HashSet<T> ents){
		synchronized (entities) {
			root.getPointsInRegion(region, ents);
		}
	}
	public HashSet<T> getPointsInRegion(Rectangle2D region) {
		HashSet<T> ents = new HashSet<T>();
		getPointsInRegion(region, ents);
		return ents;
	}

	public Rectangle2D getWorldBoundary() {
		// TODO Auto-generated method stub
		return worldBounds;
	}
}
