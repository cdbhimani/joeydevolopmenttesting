package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.me.mygdxgame.graphics.Panel;
import com.me.mygdxgame.spatialPartitioning.Point2D;
import com.me.mygdxgame.spatialPartitioning.Rectangle2D;

public class TestPanel implements ApplicationListener {
    Skin skin;
    Stage stage;
    SpriteBatch batch;
    Actor root;
	
	public void test(){
	
	}

	@Override
	public void create() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"), Gdx.files.internal("data/uiskin.png"));
		
        final Button button = new TextButton("Single", skin.getStyle(TextButtonStyle.class), "button-sl");
        
        Window window = new Window("Window", skin.getStyle(WindowStyle.class), "window");
        window.x = 100;
        window.y = 100;
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        window.add(button).fill(false, true);
        window.pack();
        stage.addActor(window);
	}

    @Override
    public void render () {
            Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
            Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);


            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            stage.draw();
    }

    @Override
    public void resize (int width, int height) {
            stage.setViewport(width, height, false);
    }

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
