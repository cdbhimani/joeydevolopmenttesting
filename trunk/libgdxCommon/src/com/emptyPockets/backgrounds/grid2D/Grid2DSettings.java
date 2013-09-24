package com.emptyPockets.backgrounds.grid2D;

import com.badlogic.gdx.math.Rectangle;

public class Grid2DSettings {
	public Rectangle bounds = new Rectangle();
	public int numX;
	public int numY;
	public NodeLinkSettings edge = new NodeLinkSettings();
	public NodeLinkSettings norm = new NodeLinkSettings();
	public float inverseMass;
}

