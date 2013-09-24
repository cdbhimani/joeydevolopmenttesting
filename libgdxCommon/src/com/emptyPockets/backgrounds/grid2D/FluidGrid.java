package com.emptyPockets.backgrounds.grid2D;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class FluidGrid {

	GridData2D[][] gridData;

	public void shiftLeft() {
		GridData2D tmp[];
		for (int y = 0; y < gridData[0].length; y++) {
			tmp = gridData[0];
			for (int x = 0; x < gridData.length - 1; x++) {
				gridData[x] = gridData[x + 1];
			}
			gridData[gridData.length - 1] = tmp;
		}
	}

}
