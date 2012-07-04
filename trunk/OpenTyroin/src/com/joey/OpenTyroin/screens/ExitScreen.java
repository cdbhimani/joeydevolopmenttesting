package com.joey.OpenTyroin.screens;

import org.w3c.dom.views.AbstractView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.joey.OpenTyroin.Tyrian;

public class ExitScreen extends AbstractScreen{

	private Texture exitTexture;
	private TextureRegion exitTextureRegion;
	
	public ExitScreen(Tyrian owner) {
		super(owner);
	}
	
	public void show(){
		super.show();
		exitTexture = new Texture("data/splash.png");
		exitTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	

	@Override
	public void resize(int width, int height) {
		super.resize( width, height );

        // let's make sure the stage is clear
        stage.clear();

        // in the image atlas, our splash image begins at (0,0) of the
        // upper-left corner and has a dimension of 512x301
        TextureRegion splashRegion = new TextureRegion( exitTexture, 0, 0, 512, 301 );

        // here we create the splash image actor and set its size
        Image splashImage = new Image( splashRegion, Scaling.stretch, Align.BOTTOM | Align.LEFT );
        splashImage.width = width;
        splashImage.height = height;

        // this is needed for the fade-in effect to work correctly; we're just
        // making the image completely transparent
        splashImage.color.a = 1f;

        // configure the fade-in/out effect on the splash image
        Action actions = Sequence.$( 
        		ScaleTo.$(1, 1, 2)
        );
        
        
        actions.setCompletionListener( new OnActionCompleted() {
            @Override
            public void completed(Action action ){
                game.setScreen( game.getMenuScreen() );
            }
        });
        splashImage.action( actions );

        stage.addActor( splashImage );
	}
	
	public void hide(){

	}
	public void dispose(){
		super.dispose();
		exitTexture.dispose();
	}
}
