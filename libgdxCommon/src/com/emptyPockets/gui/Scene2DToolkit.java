package com.emptyPockets.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Scene2DToolkit {
	private static Scene2DToolkit toolkit;
	
	Skin skin;
	
	private Scene2DToolkit(){
		
	}
	
	public static Scene2DToolkit getToolkit(){
		if(toolkit == null){
			toolkit = new Scene2DToolkit();
		}
		return toolkit;
	}
	public Skin getSkin(){
		if( skin == null ) {
			FileHandle jsonFile = Gdx.files.internal( "ui/uiskin.json" );
			FileHandle atlasFile = Gdx.files.internal( "ui/uiskin.atlas" );
			TextureAtlas atlas = new TextureAtlas(atlasFile);
            skin = new Skin(jsonFile, atlas);
        }
        return skin;
	}
}
