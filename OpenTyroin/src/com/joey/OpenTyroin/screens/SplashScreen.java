package com.joey.OpenTyroin.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.OnActionCompleted;
import com.badlogic.gdx.scenes.scene2d.actions.Delay;
import com.badlogic.gdx.scenes.scene2d.actions.FadeIn;
import com.badlogic.gdx.scenes.scene2d.actions.FadeOut;
import com.badlogic.gdx.scenes.scene2d.actions.MoveBy;
import com.badlogic.gdx.scenes.scene2d.actions.MoveTo;
import com.badlogic.gdx.scenes.scene2d.actions.Parallel;
import com.badlogic.gdx.scenes.scene2d.actions.Repeat;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleTo;
import com.badlogic.gdx.scenes.scene2d.actions.Sequence;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.joey.OpenTyroin.Tyrian;

public class SplashScreen extends AbstractScreen {
	private Texture splashTexture;
	private TextureRegion splashTextureRegion;
	
	public SplashScreen(Tyrian owner) {
		super(owner);
	}
	
	public void show(){
		super.show();
		splashTexture = new Texture("data/splash.png");
		splashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize( width, height );

        // let's make sure the stage is clear
        stage.clear();

        // in the image atlas, our splash image begins at (0,0) of the
        // upper-left corner and has a dimension of 512x301
        TextureRegion splashRegion = new TextureRegion( splashTexture, 0, 0, 512, 301 );

        // here we create the splash image actor and set its size
        Image splashImage = new Image( splashRegion, Scaling.stretch, Align.BOTTOM | Align.LEFT );
        splashImage.width = width;
        splashImage.height = height;

        // this is needed for the fade-in effect to work correctly; we're just
        // making the image completely transparent
        splashImage.color.a = 0f;

        // configure the fade-in/out effect on the splash image
        Action actions = Sequence.$( 
        		FadeIn.$(1f),
        		Delay.$(2f),
        		FadeOut.$(1f)
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
		dispose();
	}
	public void dispose(){
		super.dispose();
		splashTexture.dispose();
	}

}
