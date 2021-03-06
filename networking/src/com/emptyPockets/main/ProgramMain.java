package com.emptyPockets.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.emptyPockets.gui.Scene2DToolkit;
import com.emptyPockets.test.kryoNetwork.gui.ClientScreen;
import com.emptyPockets.utils.file.FileSelector;

public class ProgramMain extends Game{
	FileSelector fileSelector;
	
	InputMultiplexer input;
	@Override
	public void create() {
		input = new InputMultiplexer();
		Gdx.input.setInputProcessor(input);
//		BasicCarTestScreen screen = new BasicCarTestScreen(input);
//		NetworkScreen screen = new NetworkScreen(input);
//		Network screen = new Network(input);
//		EntityEditorScreen screen = new EntityEditorScreen(input);
//		BodyEditorScreen screen = new BodyEditorScreen(input);
//		SpaceShipTesting screen = new SpaceShipTesting(input); //Test texture atlas
//		BeamTesting screen = new BeamTesting(input); //Testing Mesh Grids to display laser beams
//		ShaderLesson1 screen = new ShaderLesson1(input); //Testing shaders
//		WorldRenderScreen screen = new WorldRenderScreen(input); //Fist touch tests for menus on entities
//		TouchableEntitiesScreen screen = new TouchableEntitiesScreen(input);
		ClientScreen screen = new ClientScreen(input);
//		HolePuncher screen = new HolePuncher(input);
//		MultitouchScreen screen = new MultitouchScreen(input);
		setScreen(screen);
	}
	
	@Override
	public void pause() {
		super.pause();
		Scene2DToolkit.getToolkit().disposeSkin();
	}
	
	@Override
	public void resume() {
		super.resume();
		Scene2DToolkit.getToolkit().reloadSkin();
	}
	
	
}