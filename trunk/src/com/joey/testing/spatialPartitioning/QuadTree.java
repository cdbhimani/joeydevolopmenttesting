package com.joey.testing.spatialPartitioning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class QuadTree{
	public QuadTreeNode root;

	public QuadTree(int sizeX, int sizeY, int maxCount){
		root = new QuadTreeNode(new Rectangle2D(0, 0, sizeX, sizeY), 2);
	}

	public void addPoint(Point2D p){
		root.addPoint(p);
	}
	public static void drawQuadTreeNode(QuadTreeNode tree, Graphics2D g){
		if(tree == null){return;}
		if(tree.isLeaf){
			g.setColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1f));
			//g.fillRect(tree.region.x1, tree.region.y1, tree.region.x2-tree.region.x1, tree.region.y2-tree.region.y1);

			for(Point2D p : tree.points){
				g.setColor(new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1f));
				g.fillRect(p.x, p.y, 1, 1);
			}
		}else{
			drawQuadTreeNode(tree.NW, g);
			drawQuadTreeNode(tree.NE, g);
			drawQuadTreeNode(tree.SW, g);
			drawQuadTreeNode(tree.SE, g);
		}
	}

	public static void main(String input[]){
		int sizeX = 1024;
		int sizeY = 1024;

		final QuadTree tree = new QuadTree(sizeX, sizeY, 3);
		final JPanel panel = new JPanel(){@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				tree.drawQuadTreeNode(tree.root, (Graphics2D)g);
			}
		};

		panel.addMouseListener(new MouseAdapter() {@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					tree.addPoint(new Point2D(e.getPoint().x, e.getPoint().y));
					panel.repaint();
				}
			}
		);


		JFrame f= new JFrame();
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(panel, BorderLayout.CENTER);
		f.getContentPane().setPreferredSize(new Dimension(sizeX, sizeY));
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}

