package com.emptyPockets.test.building.menus;

public interface BuildingNodeMenuAction {
	public void click(BuildingNodeMenuItem item);
	public void touchUp(BuildingNodeMenuItem item);
	public void touchDown(BuildingNodeMenuItem item);
	public void touchDragged(BuildingNodeMenuItem item, float x, float y);
}
