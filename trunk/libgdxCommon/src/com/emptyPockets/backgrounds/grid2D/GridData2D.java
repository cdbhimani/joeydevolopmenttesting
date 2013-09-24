package com.emptyPockets.backgrounds.grid2D;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class GridData2D {
	Node[][] nodes;
	ArrayList<NodeLink> links = new ArrayList<NodeLink>();
	public Grid2DSettings set;

	boolean renderGrid = true;
	boolean subSample = false;
	int sampleCount = 1;

	Vector2 tempSubSampleRec = new Vector2();
	Object lock = new Object();

	public void createGrid(Grid2DSettings set) {
		dispose();
		synchronized (lock) {
			this.set = set;

			// Create Nodes
			nodes = new Node[set.numX][set.numY];
			Vector2[][] posData = new Vector2[set.numX][set.numY];
			links = new ArrayList<NodeLink>(set.numX * set.numY * 2);
			for (int x = 0; x < set.numX; x++) {
				for (int y = 0; y < set.numY; y++) {
					float xP = set.bounds.x + set.bounds.width * (x / (set.numX - 1f));
					float yP = set.bounds.y + set.bounds.height * (y / (set.numY - 1f));
					posData[x][y] = new Vector2(xP, yP);

					nodes[x][y] = new Node();
					nodes[x][y].pos.set(posData[x][y]);

					if (x == 0 || y == 0 || x == set.numX - 1 || y == set.numY - 1) {
						nodes[x][y].inverseMass = 0;
					} else {
						nodes[x][y].inverseMass = set.inverseMass;
					}
				}
			}

			for (int x = 0; x < set.numX; x++) {
				for (int y = 0; y < set.numY; y++) {
					NodeLinkSettings cfg;
					if (x == 0 || y == 0 || x == set.numX - 1 || y == set.numY - 1) {
						cfg = set.edge;
					} else {
						cfg = set.norm;
					}

					// Back to origin
					links.add(new FixedNodeLink(nodes[x][y], posData[x][y], cfg));

					// Left
					if (x > 0) {
						links.add(new DualNodeLink(nodes[x][y], nodes[x - 1][y], cfg));
					}
					// Up
					if (y > 0) {
						links.add(new DualNodeLink(nodes[x][y], nodes[x][y - 1], cfg));
					}
				}
			}
		}
	}

	public void dispose(){
		synchronized (lock) {
			nodes = null;
			set = null;
			links.clear();
			links.trimToSize();
		}
	}
	public void resetNodes() {
		synchronized (lock) {
			for (NodeLink link : links) {
				if (link instanceof FixedNodeLink) {
					FixedNodeLink lk = (FixedNodeLink) link;
					lk.reset();
				}
			}
		}
	}

	public void solve() {
		synchronized (lock) {
			for (NodeLink link : links) {
				link.solve();
			}
		}
	}

	public void applyExplosion(float delta, Vector2 pos, float force, float radius) {
		synchronized (lock) {
			int xMin = (int) (set.numX * ((pos.x - radius - set.bounds.x) / set.bounds.width));
			int xMax = (int) (set.numX * ((pos.x + radius - set.bounds.x) / set.bounds.width)) + 1;
			int yMin = (int) (set.numY * ((pos.y - radius - set.bounds.y) / set.bounds.height));
			int yMax = (int) (set.numY * ((pos.y + radius - set.bounds.y) / set.bounds.height)) + 1;

			if (xMin < 0) {
				xMin = 0;
			}
			if (yMin < 0) {
				yMin = 0;
			}
			if (xMax > set.numX) {
				xMax = set.numX;
			}
			if (yMax > set.numY) {
				yMax = set.numY;
			}
			Vector2 dir = new Vector2();
			float lenght = 0;
			for (int x = xMin; x < xMax; x++) {
				for (int y = yMin; y < yMax; y++) {
					dir.x = nodes[x][y].pos.x - pos.x;
					dir.y = nodes[x][y].pos.y - pos.y;
					lenght = dir.len2();
					if (lenght < radius * radius) {
						dir.mul(force * delta / (.1f + lenght));
						nodes[x][y].applyImpulse(dir);
					}
				}
			}
		}
	}

	public void update(float delta) {
		synchronized (lock) {
			for (Node[] nodeData : nodes) {
				for (Node node : nodeData) {
					node.update(delta);
				}
			}
		}
	}

	public void render(ShapeRenderer shape, Vector2 screenStart, Vector2 screenEnd, float zoom) {

		if (!renderGrid) {
			return;
		}
		synchronized (lock) {
			int xMin = (int) (set.numX * ((screenStart.x - set.bounds.x) / set.bounds.width));
			int xMax = (int) (set.numX * ((screenEnd.x - set.bounds.x) / set.bounds.width)) + 1;
			int yMin = (int) (set.numY * ((screenStart.y - set.bounds.y) / set.bounds.height));
			int yMax = (int) (set.numY * ((screenEnd.y - set.bounds.y) / set.bounds.height)) + 1;
			if (xMin < 0) {
				xMin = 0;
			}
			if (yMin < 0) {
				yMin = 0;
			}
			if (xMax > set.numX) {
				xMax = set.numX;
			}
			if (yMax > set.numY) {
				yMax = set.numY;
			}

			Color majorColor = new Color(.1f, .1f, 1f, .3f);
			Color gridColor = new Color(.3f, .3f, 1f, .3f);

			float majorSize = 3f;
			float gridSize = 1;

			Vector2 pA = null;
			Vector2 pB = null;
			Vector2 pC = null;
			Vector2 pD = null;

			int minX = 5;
			int minY = 5;

			/**
			 * Draw Major Grid lines
			 */
			Gdx.gl.glLineWidth(majorSize * zoom);
			shape.setColor(majorColor);
			shape.begin(ShapeType.Line);
			for (int x = xMin; x < xMax; x++) {
				for (int y = yMin; y < yMax; y++) {
					// if ( x == 0 || y == 0 || x == nodes.length - 1 || y ==
					// nodes[x].length - 1) {
					pD = (nodes[x][y].pos);
					if (x > 0 && (y == 0 || y % minY == 0)) {
						pC = nodes[x - 1][y].pos;
						shape.line(pD.x, pD.y, pC.x, pC.y);
					}
					if (y > 0 && (x == 0 || x % minX == 0)) {
						pB = nodes[x][y - 1].pos;
						shape.line(pD.x, pD.y, pB.x, pB.y);
					}
				}
			}
			shape.end();

			/**
			 * Draw Sub Samples lines
			 */
			if (subSample) {
				Gdx.gl.glLineWidth(gridSize * zoom);
				shape.begin(ShapeType.Line);
				shape.setColor(1f, 0f, 0f, .1f);
				for (int x = xMin; x < xMax; x++) {
					for (int y = yMin; y < yMax; y++) {
						pD = (nodes[x][y].pos);
						// Fills in the grid
						if (x > 0 && y > 0) {
							pA = nodes[x - 1][y - 1].pos;
							pC = nodes[x - 1][y].pos;
							pB = nodes[x][y - 1].pos;
							drawRec(shape, pA, pB, pC, pD, 0, this.sampleCount);
						}
					}
				}
			}
			shape.end();

			/**
			 * Draw Grid lines
			 */
			Gdx.gl.glLineWidth(gridSize * zoom);
			shape.begin(ShapeType.Line);
			for (int x = xMin; x < xMax; x++) {
				for (int y = yMin; y < yMax; y++) {

					shape.setColor(gridColor);
					pD = (nodes[x][y].pos);
					if (x > 0) {
						pC = nodes[x - 1][y].pos;
						shape.line(pD.x, pD.y, pC.x, pC.y);
					}
					if (y > 0) {
						pB = nodes[x][y - 1].pos;
						shape.line(pD.x, pD.y, pB.x, pB.y);
					}
				}
			}
			shape.end();
		}
	}

	public void drawRec(ShapeRenderer shape, Vector2 pA, Vector2 pB, Vector2 pC, Vector2 pD, int depth, int maxDepth) {
		if (depth == maxDepth) {
			tempSubSampleRec.x = (pA.x + pB.x + pC.x + pD.x) / 4;
			tempSubSampleRec.y = (pA.y + pB.y + pC.y + pD.y) / 4;
			// Left
			shape.line(tempSubSampleRec.x, tempSubSampleRec.y, (pA.x + pC.x) / 2, (pA.y + pC.y) / 2);
			shape.line(tempSubSampleRec.x, tempSubSampleRec.y, (pA.x + pB.x) / 2, (pA.y + pB.y) / 2);
			shape.line(tempSubSampleRec.x, tempSubSampleRec.y, (pB.x + pD.x) / 2, (pB.y + pD.y) / 2);
			shape.line(tempSubSampleRec.x, tempSubSampleRec.y, (pD.x + pC.x) / 2, (pD.y + pC.y) / 2);
		} else {
			drawRec(shape, pA.x, pA.y, pB.x, pB.y, pC.x, pC.y, pD.x, pD.y, depth, maxDepth);
		}
	}

	public void drawRec(ShapeRenderer shape, float xA, float yA, float xB, float yB, float xC, float yC, float xD, float yD, int depth, int maxDepth) {
		if (depth == maxDepth) {
			shape.line(xA, yA, xB, yB);
			// shape.line(xB, yB, xD, yD);
			// shape.line(xC, yC, xD, yD);
			shape.line(xC, yC, xA, yA);
		} else {
			float xM = (xA + xB + xC + xD) / 4;
			float yM = (yA + yB + yC + yD) / 4;
			drawRec(shape, xA, yA, (xA + xB) / 2, (yA + yB) / 2, (xA + xC) / 2, (yA + yC) / 2, xM, yM, depth + 1, maxDepth);
			drawRec(shape, (xA + xB) / 2, (yA + yB) / 2, xB, yB, xM, yM, (xB + xD) / 2, (yB + yD) / 2, depth + 1, maxDepth);
			drawRec(shape, (xA + xC) / 2, (yA + yC) / 2, xM, yM, xC, yC, (xC + xD) / 2, (yC + yD) / 2, depth + 1, maxDepth);
			drawRec(shape, xM, yM, (xB + xD) / 2, (yB + yD) / 2, (xC + xD) / 2, (yC + yD) / 2, xD, yD, depth + 1, maxDepth);
		}
	}

}
