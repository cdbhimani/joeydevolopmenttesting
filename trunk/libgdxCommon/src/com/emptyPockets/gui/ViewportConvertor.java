package com.emptyPockets.gui;

import com.badlogic.gdx.math.Vector2;

public interface ViewportConvertor {
//	public float screenToViewport(float size);
//	public float viewportToScreen(float size);
//	
//
//	public void screenToViewport(Vector2 in, Vector2 out);
//	public void viewportToScreen(Vector2 in, Vector2 out);
	
	public void camToPanel(float x, float y, Vector2 vec);
	public void panelToCam(float x, float y, Vector2 vec);
	public float panelToCam(float val);
}
