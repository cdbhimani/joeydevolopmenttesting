package com.joey.OpenTyroin.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.joey.OpenTyroin.Tyrian;

public abstract class AbstractScreen implements Screen {

	protected Tyrian game;
	protected Stage stage;
	
	private SpriteBatch batch;
	private BitmapFont font;
	private Skin skin;

	public AbstractScreen(Tyrian owner) {
		this.game = owner;	
		this.stage = new Stage( 0, 0, true );
	}
	
	public Skin getSkin(){
		if( skin == null ) {
            skin = new Skin( Gdx.files.internal( "ui/uiskin.json" ), Gdx.files.internal( "ui/uiskin.png" ) );
        }
        return skin;
	}
	
	public BitmapFont getFont() {
		if (font == null) {
			font = new BitmapFont();
		}
		return font;
	}

	public SpriteBatch getBatch() {
		if (batch == null) {
			batch = new SpriteBatch();
		}
		return batch;
	}
	protected String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void show() {
		Gdx.app.log(Tyrian.LOG, "Showing screen: " + getName());
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log(Tyrian.LOG, "Resizing screen: " + getName() + " to: "+ width + " x " + height);
		// resize the stage
		stage.clear();
		stage.setViewport(width, height, true);
	}

	@Override
	public void render(float delta) {
		// update and draw the stage actors
		stage.act(delta);
		
		// the following code clears the screen with the given RGB color (black)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();
	}

	@Override
	public void hide() {
		Gdx.app.log(Tyrian.LOG, "Hiding screen: " + getName());
	}

	@Override
	public void pause() {
		Gdx.app.log(Tyrian.LOG, "Pausing screen: " + getName());
	}

	@Override
	public void resume() {
		Gdx.app.log(Tyrian.LOG, "Resuming screen: " + getName());
	}

	@Override
	public void dispose() {
		Gdx.app.log(Tyrian.LOG, "Disposing screen: " + getName());

//		// dispose the collaborators
		stage.clear();
		stage.dispose();
		if(batch!=null)batch.dispose();
		if(font!=null)font.dispose();
		if(skin!=null)skin.dispose();
	}

}
