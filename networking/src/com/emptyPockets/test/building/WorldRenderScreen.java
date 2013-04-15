package com.emptyPockets.test.building;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.emptyPockets.graphics.GraphicsToolkit;
import com.emptyPockets.gui.GameScreen;
import com.emptyPockets.gui.OrthographicsCameraConvertor;
import com.emptyPockets.test.building.controls.BuildingNodeMenu;
import com.emptyPockets.test.building.controls.BuildingNodeMenuAction;
import com.emptyPockets.test.building.controls.BuildingNodeMenuAdapter;
import com.emptyPockets.test.building.controls.BuildingNodeMenuItem;
import com.emptyPockets.test.building.model.BuildingNode;
import com.emptyPockets.test.building.model.World;
import com.emptyPockets.utils.OrthoCamController;

public class WorldRenderScreen extends GameScreen {
	World world;
	ShapeRenderer shape;
	
	OrthoCamController cameraController;
	OrthographicsCameraConvertor camConvert;
	Vector2 lastMouse;
	
	boolean touchDownOnMenu;
	boolean touchDownOnBuilding;
	boolean dragged;
	
	Vector2 selectedBuildingOffset;
	BuildingNode selectedBuilding;
	BuildingNodeMenuItem selectedMenuItem;
	
	public WorldRenderScreen(InputMultiplexer inputProcessor) {
		super(inputProcessor);
		
		world = new World();
		cameraController = new OrthoCamController(getScreenCamera());
		cameraController.setZoomEnabled(false);
		
		camConvert = new OrthographicsCameraConvertor(getScreenCamera());
		
		lastMouse = new Vector2();
		selectedBuildingOffset = new Vector2();
		
		setClearColor(Color.BLACK);
		setupRandom(10, 1000, 1000,100,60,10);
	}

	public void addBuilding(final BuildingNode node, final float border, final float buttonSize){
		float rad = node.getRadius();
		BuildingNodeMenu menu = new BuildingNodeMenu("Menu");
		
		BuildingNodeMenuItem left = new BuildingNodeMenuItem(menu, "",   new Rectangle(-rad-buttonSize-border,-buttonSize/2         ,buttonSize,buttonSize));
		BuildingNodeMenuItem right = new BuildingNodeMenuItem(menu, "",  new Rectangle(rad+border            ,-buttonSize/2         ,buttonSize,buttonSize));
		BuildingNodeMenuItem down = new BuildingNodeMenuItem(menu, "",   new Rectangle(-buttonSize/2         ,-rad-buttonSize-border,buttonSize,buttonSize));
		BuildingNodeMenuItem up = new BuildingNodeMenuItem(menu, "",     new Rectangle(-buttonSize/2         , rad+border           ,buttonSize,buttonSize));
		
		menu.addMenuItem(left);
		menu.addMenuItem(right);
		menu.addMenuItem(up);
		menu.addMenuItem(down);
		final float offset = 3;
		up.addAction(new BuildingNodeMenuAdapter(){
			@Override
			public void click(BuildingNodeMenuItem item) {
				BuildingNode newNode = new BuildingNode();
				newNode.setPos(node.getPosX(), node.getPosY()+offset*node.getRadius());
				newNode.setRadius(node.getRadius());
				addBuilding(newNode, border, buttonSize);
			}
		});
		
		down.addAction(new BuildingNodeMenuAdapter(){
			@Override
			public void click(BuildingNodeMenuItem item) {
				BuildingNode newNode = new BuildingNode();
				newNode.setPos(node.getPosX(), node.getPosY()-offset*node.getRadius());
				newNode.setRadius(node.getRadius());
				addBuilding(newNode, border, buttonSize);
			}
		});
		
		left.addAction(new BuildingNodeMenuAdapter(){
			@Override
			public void click(BuildingNodeMenuItem item) {
				BuildingNode newNode = new BuildingNode();
				newNode.setPos(node.getPosX()-offset*node.getRadius(), node.getPosY());
				newNode.setRadius(node.getRadius());
				addBuilding(newNode, border, buttonSize);
			}
		});
		
		right.addAction(new BuildingNodeMenuAdapter(){
			@Override
			public void click(BuildingNodeMenuItem item) {
				BuildingNode newNode = new BuildingNode();
				newNode.setPos(node.getPosX()+offset*node.getRadius(), node.getPosY());
				newNode.setRadius(node.getRadius());
				addBuilding(newNode, border, buttonSize);
			}
		});
		node.setMenu(menu);
		world.addBuilding(node);
	}
	public void setupRandom(int count, float wide, float high, float radiusMax, float buttonSize,float border){
		
		for(int i = 0; i < count; i++){
			final BuildingNode node = new BuildingNode();
			float rad = MathUtils.random(radiusMax/2, radiusMax);
			node.setPos(MathUtils.random(-wide, wide),MathUtils.random(-high, high));
			node.setRadius(rad);
			addBuilding(node, border, buttonSize);
		}
	}
	
	@Override
	public void drawBackground(float delta) {
		GraphicsToolkit.draw2DAxis(shape, getScreenCamera(), 100, Color.WHITE);
	}

	@Override
	protected void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		input.addProcessor(cameraController);
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(cameraController);
	}
	
	@Override
	public void show() {
		super.show();
		shape = new ShapeRenderer();
	}
	
	@Override
	public void initializeRender() {
		super.initializeRender();
		shape.setProjectionMatrix(getScreenCamera().combined);
	}
	
	@Override
	public void hide() {
		super.hide();
		shape.dispose();
		shape = null;
	}
	
	@Override
	public void drawScreen(float delta) {
		world.draw(shape);
	}

	@Override
	public void drawOverlay(float delta) {
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		camConvert.camToPanel(x, y, lastMouse);
		selectedMenuItem = null;
		dragged = false;
		touchDownOnBuilding = false;
		touchDownOnMenu = false;
		
		/**
		 * Menu Logic
		 */
		if(selectedBuilding != null){
			if(selectedBuilding.getMenu() != null){
				BuildingNodeMenuItem menuItem =selectedBuilding.getMenu().getMenuItem(lastMouse.x, lastMouse.y); 
				if(menuItem != null){
					touchDownOnMenu = true;
					menuItem.touchDown();
					selectedMenuItem = menuItem;
				}
			}
		}
		
		/**
		 * Building Logic (only if menu not pressed)
		 */
		if(!touchDownOnMenu){
			//Select Building
			BuildingNode newBuilding = world.selectBuildingNode(lastMouse.x, lastMouse.y);
			
			//Select new building
			if(newBuilding != null){
				//Unselect last building
				if(selectedBuilding != null){
					selectedBuilding.setSelected(false);
				}
				selectedBuilding = newBuilding;
				selectedBuildingOffset.x = selectedBuilding.getPosX() - lastMouse.x;
				selectedBuildingOffset.y = selectedBuilding.getPosY() - lastMouse.y;
				selectedBuilding.setSelected(true);
				touchDownOnBuilding= true;
			}
		}
		
		if(touchDownOnBuilding || touchDownOnMenu){
			return true;
		}
		return super.touchDown(x, y, pointer, button);
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		camConvert.camToPanel(screenX, screenY, lastMouse);
		dragged = true;
		
		/**
		 * Touch dragged on menu Item
		 */
		if(touchDownOnMenu && selectedMenuItem != null){
			selectedMenuItem.touchDragged(lastMouse.x, lastMouse.y);
			return true;
		}
		/**
		 * Move selected building if dragged
		 */
		if(touchDownOnBuilding && selectedBuilding != null){
			selectedBuilding.setPos(lastMouse.x+selectedBuildingOffset.x,lastMouse.y+selectedBuildingOffset.y);
			return true;
		}
		return super.touchDragged(screenX, screenY, pointer);
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		camConvert.camToPanel(screenX, screenY, lastMouse);
		/**
		 * Click the menu
		 */
		if(touchDownOnMenu ){
			if(selectedBuilding != null){
				if(selectedBuilding.getMenu() != null){
					BuildingNodeMenuItem menuItem =selectedBuilding.getMenu().getMenuItem(lastMouse.x, lastMouse.y); 
					if(menuItem != null){
						
						if(!dragged){
							menuItem.click();
						}
						menuItem.touchUp();
					}
				}
			}
		}
		
		/**
		 * If touch never hit any building or menu unselect it
		 */
		if(!touchDownOnMenu&&!touchDownOnBuilding&&!dragged){
			if(selectedBuilding != null){
				selectedBuilding.setSelected(false);
				selectedBuilding = null;
			}
		}
		
		
		return super.touchUp(screenX, screenY, pointer, button);
	}
}
