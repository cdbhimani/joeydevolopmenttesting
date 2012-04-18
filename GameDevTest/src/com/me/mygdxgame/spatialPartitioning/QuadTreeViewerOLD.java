package com.me.mygdxgame.spatialPartitioning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class QuadTreeViewerOLD extends JPanel implements MouseListener,
		MouseMotionListener, MouseWheelListener {

	QuadTree tree;
	Point2D worldPos = new Point2D();
	Point2D mousePos = new Point2D();
	Point2D dragOffset = new Point2D();

	Point2D p = new Point2D();
	boolean drag = false;

	float zoom = 1;
	float zoomChange = 1;
	float zoomIncrement = 0.1f;
	boolean zoomChanged = false;

	boolean drawGird = false;
	boolean drawPoints = true;
	int pointSize = 200;
	
	public QuadTreeViewerOLD() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	public synchronized void setQuadTree(QuadTree tree) {
		this.tree = tree;
	}

	@Override
	protected synchronized void paintComponent(Graphics g1) {
		// TODO Auto-generated method stub
		super.paintComponent(g1);

		Graphics2D g = (Graphics2D) g1;

		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		if (this.tree == null) {
			return;
		}
		synchronized (tree) {

			AffineTransform old = g.getTransform();

			if (zoomChanged) {
				Point2D imageP = new Point2D(
						(float) ((mousePos.x - worldPos.x) / zoom),
						(float) ((mousePos.y - worldPos.y) / zoom));

				zoom *= zoomChange;
				zoomChange = 1;

				Point2D panelP = new Point2D(
						(float) ((imageP.x * zoom + worldPos.x)),
						(float) ((imageP.y * zoom + worldPos.y)));

				worldPos.x += (mousePos.x - (float) panelP.x);
				worldPos.y += (mousePos.y - (float) panelP.y);

				zoomChanged = false;
			}

			AffineTransform trans = AffineTransform.getTranslateInstance(
					worldPos.x + dragOffset.x, worldPos.y + dragOffset.y);
			AffineTransform scaleTra = AffineTransform.getScaleInstance(zoom,
					zoom);
			trans.concatenate(scaleTra);

			double[] input = new double[] { 0, 0, getWidth(), getHeight() };
			double[] result = new double[] { 0, 0, getWidth(), getHeight() };

			try {
				trans.inverseTransform(input, 0, result, 0, 2);
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Rectangle2D r = new Rectangle2D((float) result[0], (float) result[1],
					(float) result[2], (float) result[3]);
			g.setTransform(trans);
			drawQuadTreeNode(tree.root, (Graphics2D) g, r);
			g.setTransform(old);
		}
	}

	public void drawQuadTreeNode(QuadTreeNode tree, Graphics2D g,
			Rectangle2D r) {
		if (tree == null) {
			return;
		}

		if (!r.intersects(tree.region)) {
			return;
		}
		if (tree.isLeaf) {
			if(drawGird){
				g.setColor(Color.RED);
				g.drawRect((int)(tree.region.x1), (int)(tree.region.y1), (int)(tree.region.x2
					- tree.region.x1), (int)(tree.region.y2 - tree.region.y1));
			}
			if(drawPoints){
			synchronized (tree.points) {
					for (Point2D p : tree.points) {
						g.setColor(Color.blue);
						g.fillOval((int)(p.x-pointSize), (int)(p.y-pointSize), 2*pointSize, 2*pointSize);
					}
				}
			}
		} else {
			drawQuadTreeNode(tree.NW, g, r);
			drawQuadTreeNode(tree.NE, g, r);
			drawQuadTreeNode(tree.SW, g, r);
			drawQuadTreeNode(tree.SE, g, r);
		}
	}

	public void mousePressed(MouseEvent e) {
		drag = true;
		p.x = e.getPoint().x;
		p.y = e.getPoint().y;
	};

	public void mouseDragged(MouseEvent e) {
		if (drag) {
			dragOffset.x = e.getPoint().x - p.x;
			dragOffset.y = e.getPoint().y - p.y;
		}
	};

	public void mouseReleased(MouseEvent e) {
		drag = false;
		worldPos.x += dragOffset.x;
		worldPos.y += dragOffset.y;
		dragOffset.x = 0;
		dragOffset.y = 0;
	};

	public void mouseWheelMoved(MouseWheelEvent e) {
		mousePos.x = e.getPoint().x;
		mousePos.y = e.getPoint().y;

		if (e.getWheelRotation() > 0) {
			zoomChange = 1 + zoomIncrement;
		} else {
			zoomChange = 1 - zoomIncrement;
		}
		zoomChanged = true;
	};

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String input[]) throws InterruptedException {
		int number = 10000;

		int sizeX = 5000;
		int sizeY = 5000;

		int maxEntity = 2;
		
		Rectangle2D r = new Rectangle2D(-sizeX, -sizeY, sizeX, sizeY);

		QuadTree tree = new QuadTree(r, maxEntity);

		QuadTreeViewerOLD viewer = new QuadTreeViewerOLD();
		viewer.pointSize = 3;
		viewer.drawGird = true;
		viewer.setQuadTree(tree);

		ArrayList<Point2D> points = new ArrayList<Point2D>(number);
		for (float i = 0; i < number; i++) {
			float maxVel = 100;
			Point2D p = new Point2D();
			p.x = (float) (Math.random() * sizeX);
			p.y = (float) (Math.random() * sizeY);
			p.vx = (float) (maxVel-Math.random() *2* maxVel);
			p.vy = (float) (maxVel-Math.random() * 2*maxVel);
			
			points.add(p);
			tree.addPoint(p);
		}

		JFrame f = new JFrame();
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(viewer, BorderLayout.CENTER);
		f.getContentPane().setPreferredSize(new Dimension(sizeX, sizeY));
		f.setSize(600, 480);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		long begin;
		long end;
		float time = 0;
		boolean value = true;
		float border = 30;
		long lastUpdate = System.currentTimeMillis();
		while (value == true) {
			synchronized (tree) {
				float diff = (System.currentTimeMillis()-lastUpdate)/1000f;
				lastUpdate = System.currentTimeMillis();
				
				begin = System.nanoTime();
				for(int i = 0; i < points.size(); i++){
					Point2D p = points.get(i);
					p.x += p.vx*diff;
					p.y += p.vy*diff;
					
					//Setup data
					ArrayList<Point2D> rst =tree.getPointsInRegion(
							new Rectangle2D(p.x-border, p.y-border, p.x+border, p.y+border));
					float vx = 0;
					float vy = 0;
					int count = 0;
					for(Point2D data : rst){
						vx+=data.vx;
						vy+=data.vy;
						count++;
					}
//					if(count > 0){
//						p.vx = (p.vx+vx/count)/2;
//						p.vy = (p.vy+vy/count)/2;
//					}
					
					//Validate position
					if(p.x <= r.x1)p.x = r.x2;
					if(p.x >= r.x2)p.x = r.x1;
					
					if(p.y <= r.y1)p.y = r.y2;
					if(p.y >= r.y2)p.y = r.y1;
				}
				end = System.nanoTime();
				time = (end - begin) / 1e6f;
				System.out.println("Move All : " + time);

				begin = System.nanoTime();
				tree.rebuild();
				end = System.nanoTime();
				time = (end - begin) / 1e6f;
				System.out.println("Rebuild : " + time);

				System.out.println("\n\n");
				
			}
			viewer.repaint();
		}
	}
}
