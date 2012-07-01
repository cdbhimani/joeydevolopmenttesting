package com.joey.OpenTyroin.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.esotericsoftware.tablelayout.Cell;
import com.joey.OpenTyroin.Tyrian;

public class MenuScreen extends AbstractScreen {
	private static final float BUTTON_WIDTH = 300f;
	private static final float BUTTON_HEIGHT = 60f;
	private static final float BUTTON_SPACING = 10f;
	
	public MenuScreen(Tyrian owner) {
		super(owner);
	}
	
	public void resize(int width, int height){
		super.resize(width, height);
		
		//Creating Entities
		Label welcomeLabel = new Label( "Welcome to Tyrian for android", getSkin());	
        TextButton startGameButton = new TextButton( "Start game", getSkin() );
        TextButton optionsButton = new TextButton( "Options", getSkin() );
        TextButton hallOfFameButton = new TextButton( "Hall of Fame", getSkin() );
        TextButton exitButton = new TextButton( "Exit", getSkin() );
        
        Table table = new Table(getSkin());
        table.width = width;
        table.height = height;
        table.register("welcomeLabel", welcomeLabel);
        table.register("startGameButton", startGameButton);
        table.register("optionsButton", optionsButton);
        table.register("hallOfFameButton",hallOfFameButton);
        table.register("exitButton", exitButton);
        
        table.getTableLayout().parse( Gdx.files.internal( "ui/mainMenu/layout.txt" ).readString() );
        stage.addActor(table);
        
        
        exitButton.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				Gdx.app.exit();
			}
		});
        
	}
	

}
