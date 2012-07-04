package com.joey.OpenTyroin;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.joey.OpenTyroin.screens.ExitScreen;
import com.joey.OpenTyroin.screens.HighScoreScreen;
import com.joey.OpenTyroin.screens.LevelScreen;
import com.joey.OpenTyroin.screens.LoadSavedGameScreen;
import com.joey.OpenTyroin.screens.MenuScreen;
import com.joey.OpenTyroin.screens.OptionsScreen;
import com.joey.OpenTyroin.screens.ProfileScreen;
import com.joey.OpenTyroin.screens.SplashScreen;
import com.joey.OpenTyroin.screens.StartGameScreen;

public class Tyrian extends Game {
    public static final String LOG = Tyrian.class.getSimpleName();
    private FPSLogger fpsLogger;
 
	SplashScreen splashScreen;
	MenuScreen menuScreen;
	
	StartGameScreen startGameScreen;
	OptionsScreen optionsScreen;
	HighScoreScreen highScoreScreen;
	ProfileScreen profileScreen;
	LoadSavedGameScreen loadSavedGameScreen;
	LevelScreen levelScreen;
	ExitScreen exitScreen;
	
	public Tyrian(){
	}
	
	
	@Override
	public void create() {
		Gdx.app.log(Tyrian.LOG, "Creating Game");
		fpsLogger = new FPSLogger();
		
		//setScreen( getSplashScreen());
		setScreen(getMenuScreen());
	}
	
	@Override
    public void resize(int width,int height ){
        Gdx.app.log( Tyrian.LOG, "Resizing game to: " + width + " x " + height );
    }
 
	
	public SplashScreen getSplashScreen(){
		if(splashScreen == null){
			splashScreen = new SplashScreen(this);
		}
		return splashScreen;
	}
	
	public MenuScreen getMenuScreen(){
		if(menuScreen == null){
			menuScreen = new MenuScreen(this);
		}
		return menuScreen;
	}
	
	public ExitScreen getExitScreen(){
		if(exitScreen == null){
			exitScreen = new ExitScreen(this);
		}
		return exitScreen;
	}

	public StartGameScreen getStartGameScreen() {
		if(startGameScreen != null){
			startGameScreen = new StartGameScreen(this);
		}
		return startGameScreen;
	}

	public OptionsScreen getOptionsScreen() {
		if(optionsScreen != null){
			optionsScreen = new OptionsScreen(this);
		}
		return optionsScreen;
	}

	public HighScoreScreen getHighScoreScreen() {
		if(highScoreScreen != null){
			highScoreScreen = new HighScoreScreen(this);
		}
		return highScoreScreen;
	}

	public ProfileScreen getProfileScreen() {
		if(profileScreen != null){
			profileScreen = new ProfileScreen(this);
		}
		return profileScreen;
	}

	public LoadSavedGameScreen getLoadSavedGameScreen() {
		if(loadSavedGameScreen != null){
			loadSavedGameScreen = new LoadSavedGameScreen(this);
		}
		return loadSavedGameScreen;
	}

	public LevelScreen getLevelScreen() {
		return levelScreen;
	}


    @Override
    public void render(){
    	super.render();
        fpsLogger.log();
    }
 
    @Override
    public void pause(){
        Gdx.app.log( Tyrian.LOG, "Pausing game" );
    }
 
    @Override
    public void resume(){
        Gdx.app.log( Tyrian.LOG, "Resuming game" );
    }
 
    @Override
    public void dispose(){
        Gdx.app.log( Tyrian.LOG, "Disposing game" );
    }
}

