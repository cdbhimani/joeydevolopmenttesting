package com.emptypockets.grid;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.emptyPockets.backgrounds.grid2D.Grid2DPanel;
import com.emptyPockets.backgrounds.grid2D.Grid2DSettings;
import com.emptyPockets.backgrounds.grid2D.GridData2D;
import com.emptyPockets.graphics.GraphicsToolkit;
import com.emptyPockets.gui.OrthographicsCameraConvertor;
import com.emptyPockets.gui.ScreenSizeHelper;
import com.emptyPockets.gui.StageScreen;
import com.emptyPockets.utils.OrthoCamController;

public class GridScreen extends StageScreen {
	final int insetSize = ScreenSizeHelper.getcmtoPxlX(1f);
	int touchPadSize = ScreenSizeHelper.getcmtoPxlX(3);

	GridData2D background;

	Touchpad movePad;
	Touchpad shootPad;
	public Slider strength;

	ShapeRenderer shape;
	OrthoCamController control;
	OrthographicsCameraConvertor conv;

	Ship ship = new Ship(this, new Vector2());
	ArrayList<PointMass> points = new ArrayList<PointMass>();
	Vector2 screenStart = new Vector2(0, 0);
	Vector2 screenEnd = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	Grid2DPanel gridSettings;
	
	public static float stiff = 15f;
	public static float damp = .9f;

	public static float sizeX = 2000;
	public static float sizeY = 2000;
	public static int numX = 50;
	public static int numY = 50;

	public GridScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		setClearColor(Color.BLACK);
		control = new OrthoCamController(getScreenCamera());
		conv = new OrthographicsCameraConvertor(getScreenCamera());
	}

	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		input.addProcessor(control);
	}

	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(control);
	}

	@Override
	public void show() {
		super.show();

		Grid2DSettings set = new Grid2DSettings();

		set.bounds.x = -sizeX / 2;
		set.bounds.y = -sizeY / 2;
		set.bounds.width = sizeX;
		set.bounds.height = sizeY;

		set.numX = numX;
		set.numY = numY;

		set.inverseMass = 1f;
		set.norm.damping = damp;
		set.norm.stiffness = stiff;

		set.edge.damping = damp;
		set.edge.stiffness = stiff;

		background = new GridData2D();
		background.createGrid(set);
		shape = new ShapeRenderer();
		
		gridSettings = new Grid2DPanel(background, getSkin());
		gridSettings.pack();
		gridSettings.setVisible(true);
		
		getStage().addActor(gridSettings);
	}

	@Override
	public void hide() {
		super.hide();

		background.dispose();
		shape.dispose();

		background = null;
		shape = null;
	}

	@Override
	public void createStage(Stage stage) {

		movePad = new Touchpad(0, getSkin());
		shootPad = new Touchpad(0, getSkin());
		strength = new Slider(10, 8000, 10, false, getSkin());

		Pixmap pix = new Pixmap(4, 4, Format.RGBA8888);
		Color c = new Color(Color.LIGHT_GRAY);
		c.a = 0.2f;
		pix.setColor(c);
		pix.fill();
		pix.setColor(Color.LIGHT_GRAY);
		pix.drawRectangle(0, 0, 4, 4);

		NinePatch ninePatch = new NinePatch(new Texture(pix), 1, 1, 1, 1);
		Drawable drawA = new NinePatchDrawable(ninePatch);
		Drawable drawB = new NinePatchDrawable(ninePatch);
		movePad.getStyle().background = drawA;
		shootPad.getStyle().background = drawB;
		Table layout = new Table();
		// layout.debug();
		// top
		layout.row();
		layout.add(strength).colspan(3).height(insetSize).fillX().expandX();
		// middle
		layout.row();
		layout.add().width(touchPadSize).fillY().expandY();
		layout.add().fill().expand();
		layout.add().width(touchPadSize).fillY().expandY();
		// bottom
		layout.row();
		layout.add(movePad).size(touchPadSize);
		layout.add().height(touchPadSize).fillX().expandX();
		layout.add(shootPad).size(touchPadSize);
		layout.setFillParent(true);

		stage.addActor(layout);
	}

	long time = System.currentTimeMillis();

	public void processInputs() {
		float moveX = 0;
		float moveY = 0;
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			moveY = 1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			moveY = -1;
		} else {
			moveY = movePad.getKnobPercentY();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			moveX = 1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			moveX = -1;
		} else {
			moveX = movePad.getKnobPercentX();
		}
		ship.move(moveX, moveY);

		float shootX = 0;
		float shootY = 0;
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			shootY = 1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			shootY = -1;
		} else {
			shootY = shootPad.getKnobPercentY();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			shootX = 1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			shootX = -1;
		} else {
			shootX = shootPad.getKnobPercentX();
		}
		ship.shoot(shootX, shootY);
	}

	@Override
	public void updateLogic(float delta) {
		super.updateLogic(delta);

		processInputs();

		ship.update(delta);
		eventLogger.begin("LOGIC-Explosions");
		Iterator<PointMass> point = points.iterator();
		while (point.hasNext()) {
			PointMass p = point.next();
			p.update(delta);
			float force = 1 / p.inverseMass;
			// if (force > 0) {
			background.applyExplosion(1f, p.pos, force, 50);
			// } else {
			// background.applyImplosiveForce(-force*peroid, p.pos, expRange);
			// }
			p.time -= (delta);
			if (p.time < 0 || p.pos.x < background.set.bounds.x || p.pos.x > background.set.bounds.x + background.set.bounds.width || p.pos.y < background.set.bounds.y || p.pos.y > background.set.bounds.y + background.set.bounds.height) {
				point.remove();
			}

		}
		eventLogger.end("LOGIC-Explosions");

		eventLogger.begin("LOGIC-solve");
		background.solve();
		eventLogger.end("LOGIC-solve");
		eventLogger.begin("LOGIC-Update");
		background.update(delta);
		eventLogger.end("LOGIC-Update");
	}

	@Override
	public void initializeRender() {
		getScreenCamera().position.x = ship.pos.x;
		getScreenCamera().position.y = ship.pos.y;
		super.initializeRender();
		shape.setProjectionMatrix(getScreenCamera().combined);
	}

	@Override
	public void drawBackground(float delta) {
		GraphicsToolkit.draw2DAxis(shape, getScreenCamera(), 100, Color.WHITE);
	}

	@Override
	public void drawScreen(float delta) {

		conv.camToPanel(0, Gdx.graphics.getHeight(), screenStart);
		conv.camToPanel(Gdx.graphics.getWidth(), 0, screenEnd);

		background.render(shape, screenStart, screenEnd, 1 / getScreenCamera().zoom);

		shape.begin(ShapeType.FilledCircle);
		shape.setColor(0, 10, 0, 0.5f);
		shape.filledCircle(ship.pos.x, ship.pos.y, 5);
		shape.end();

		shape.begin(ShapeType.FilledCircle);
		shape.setColor(1, 0, 0, 0.5f);
		for (PointMass p : points) {
			shape.filledCircle(p.pos.x, p.pos.y, 3);
		}
		shape.end();
	}

	@Override
	public void drawOverlay(float delta) {
	}

}

class Ship extends PointMass {
	GridScreen grid;
	float maxVel = 500;

	long lastShoot = 0;
	long shootInterval = 200;

	public Ship(GridScreen grid, Vector2 position) {
		super(position, 0);
		this.grid = grid;
	}

	public void shoot(float x, float y) {
		if (Math.abs(x) > 0.1 || Math.abs(y) > 0.1) {
			if (lastShoot + shootInterval < System.currentTimeMillis()) {
				float massVal = grid.strength.getValue();
				PointMass mass = new PointMass(pos.cpy(), 1 / massVal);
				mass.vel.x = x;
				mass.vel.y = y;
				mass.vel.nor().mul(maxVel * 1.8f);
				mass.time = 10;
				grid.points.add(mass);
				lastShoot = System.currentTimeMillis();
			}
		}
	}

	public void move(float x, float y) {
		vel.x = x;
		vel.y = y;
		vel.mul(maxVel);
	}

	@Override
	public void update(float time) {
		super.update(time);
		vel.x = 0;
		vel.y = 0;
	}

}