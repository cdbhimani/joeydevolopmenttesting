package com.joey.aitesting;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.TableLayout;
import com.joey.aitesting.game.GameWorld;
import com.joey.aitesting.game.graphics.ConsoleLogger;
import com.joey.aitesting.game.graphics.ConsoleViewer;
import com.joey.aitesting.game.graphics.QuadTreeViewer;
import com.joey.aitesting.game.shapes.Rectangle2D;
import com.me.mygdxgame.Gestures.OrthoCamController;

public class MyGdxGame implements ApplicationListener{
	int sizeX = 600;
	int sizeY = 600;
	
	FPSLogger fps;
    Skin skin;
    Stage stage;
    SpriteBatch batch;
    Actor root;
    GameWorld world;
    
	
	QuadTreeViewer quadViewer;
	OrthoCamController quadViewCamController;
	OrthographicCamera quadViewCam;
	
	ConsoleViewer consoleViewer;
	ConsoleLogger console;
	OrthographicCamera consoleCamera;
	
	Label worldEntityCount;
	TextField toAddEntityCount;
	
	@Override
	public void create() {
		fps = new FPSLogger();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        batch = new SpriteBatch();
        
        quadViewCam = new OrthographicCamera();
        quadViewCamController = new OrthoCamController(quadViewCam);

        quadViewer = new QuadTreeViewer(new Rectangle2D(-sizeX, -sizeY, sizeX, sizeY));
		
		
		console = new ConsoleLogger();
		consoleViewer = new ConsoleViewer(console);
		consoleCamera = new OrthographicCamera();
		
        createStage();
        
        InputMultiplexer multi = new InputMultiplexer();
        multi.addProcessor(stage);
        multi.addProcessor(quadViewCamController);
//      multi.addProcessor(this);
        Gdx.input.setInputProcessor(multi);
        
	}
	
	public void createStage(){
        skin = new Skin(Gdx.files.internal("data/uiskin.json"), Gdx.files.internal("data/uiskin.png"));
		
        final Button addButton = new TextButton("Add", skin.getStyle(TextButtonStyle.class), "button-sl");
        final Button removeButton = new TextButton("Remove", skin.getStyle(TextButtonStyle.class), "button-s2");
        final Button button = new TextButton("Menu", skin.getStyle(TextButtonStyle.class), "button-s3");
        final CheckBox drawGrid = new CheckBox(skin);
        final CheckBox drawParticles = new CheckBox(skin);
        
        drawGrid.setChecked(quadViewer.drawQuadTree);
        drawParticles.setChecked(quadViewer.drawEntities);
        
        worldEntityCount= new Label("", skin.getStyle(LabelStyle.class));
        toAddEntityCount= new TextField("10", "", skin.getStyle(TextFieldStyle.class), "styles2");
        final Window window = new Window("Window", skin.getStyle(WindowStyle.class), "window");
        
        button.x = 0;
        button.y = 0;
        button.width =50;
        button.height =50;
        
        window.x = 55;
        window.y = 0;
       
        worldEntityCount.setAlignment(Align.CENTER);
        
        
        window.defaults().spaceBottom(10);
        window.row().fill().expandX();
        window.add(new Label("Grid: ", skin));
        window.add(drawGrid).fill(true, false);
        window.row().fill().expandX();
        window.add(new Label("Entity: ", skin));
        window.add(drawParticles).fill(true, false);
        window.row().fill().expandX();
        window.add(addButton).fill(true, false);
        window.add(removeButton).fill(true, false);        
        window.row().fill().expandX();
        window.add(new Label("Num: ", skin));
        window.add(toAddEntityCount).fill(true,false);
        window.row().fill().expandX();
        window.add(new Label("Total: ", skin));
        window.add(worldEntityCount).fill(true, false);
        
        window.pack();
        
        stage.addActor(window);
		stage.addActor(button);
		
		drawParticles.setClickListener(new ClickListener() {
				
				@Override
				public void click(Actor actor, float x, float y) {
					quadViewer.drawEntities = !quadViewer.drawEntities;
					drawParticles.setChecked(quadViewer.drawEntities);
				}
			});
		 
		drawGrid.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				quadViewer.drawQuadTree = !quadViewer.drawQuadTree;
				drawGrid.setChecked(quadViewer.drawQuadTree);
			}
		});
		
		
		 
        addButton.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				
				try{
					world.addVehicles(Integer.parseInt(toAddEntityCount.getText()));	
				}
				catch(Exception e){
					
				}
				
			}
		});
        
        removeButton.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				try{
					world.removeVehicles(Integer.parseInt(toAddEntityCount.getText()));	
				}
				catch(Exception e){
				}
			}
				
		});
        
        button.setClickListener(new ClickListener() {
			
			@Override
			public void click(Actor actor, float x, float y) {
				window.visible = !window.visible;
			}
		});
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		fps.log();
		updateWorld();
		updateFields();
		drawWorld();
	}
	
	
	public void updateWorld(){
		world.update(1);
	}
		
	public void updateFields(){
		worldEntityCount.setText(""+quadViewer.tree.points.size());
	}
	public void drawWorld(){
		GL10 gl = Gdx.gl10;
		
		quadViewCam.update();
		quadViewCam.apply(gl);	
		
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Vector3 p1 = new Vector3(0,0,0);
		Vector3 p2 = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),0);
		
		quadViewCam.unproject(p1);
		quadViewCam.unproject(p2);
		
		Rectangle2D drawRegion = new Rectangle2D(p1.x, p2.y, p2.x, p1.y);
		quadViewer.render(gl, quadViewCam, drawRegion);
		
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        consoleCamera.update();
		consoleCamera.apply(gl);
		consoleViewer.draw(consoleCamera);
	}

	@Override
	public void resize(int width, int height) {
		quadViewCam.viewportWidth = width;
		quadViewCam.viewportHeight = height;
		
		consoleCamera.viewportWidth = width;
		consoleCamera.viewportHeight = height;
		consoleCamera.position.x = width/2;
		consoleCamera.position.y = height/2;
		
        stage.setViewport(width, height, false);
	}
	
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
