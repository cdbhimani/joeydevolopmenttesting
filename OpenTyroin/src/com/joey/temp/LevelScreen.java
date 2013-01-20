package com.joey.temp;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.streetj.starfighter.Touchpad.TouchpadStyle;

public class LevelScreen implements Screen {
   
   public static final float VIEWPORT_WIDTH = 400;
   public static final float VIEWPORT_HEIGHT = 240;
   
   private Stage levelStage;
   private Touchpad touchpad;
   
   private boolean firstShowing;
   
   public LevelScreen() {
      
      this.firstShowing = true;
   }

   @Override
   public void render(float delta) {
      // (1) Update stage
      levelStage.act(delta);
      // (2) Render stage
      Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
      levelStage.draw();
   }

   @Override
   public void resize(int width, int height) {
      // TODO Auto-generated method stub

   }

   @Override
   public void show() {
      if (firstShowing) {
         init();
         firstShowing = false;
      }
      Gdx.input.setInputProcessor(levelStage);
   }

   @Override
   public void hide() {
      // TODO Auto-generated method stub

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
   
   protected void init() {
      TouchpadStyle touchpadStyle =  new TouchpadStyle(
            new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/imgs/controls/joypad.png")), 64, 64)),
            new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("data/imgs/controls/knob.png")), 24, 24)));
      touchpad = new Touchpad(26f, 3f, touchpadStyle);
      touchpad.setX(10f);
      touchpad.setY(10f);
      
      touchpad.addListener(new ChangeListener() {
         
         @Override
         public void changed(ChangeEvent event, Actor actor) {
            Gdx.app.log("Touchpad", "Value: " + touchpad.getPadValueX() + ", " + touchpad.getPadValueY());
         }
      });
      
      levelStage = new Stage(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, true);
      levelStage.addActor(touchpad);
   }
}