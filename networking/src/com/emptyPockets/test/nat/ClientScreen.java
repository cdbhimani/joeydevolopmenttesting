package com.emptyPockets.test.nat;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.graphics.GraphicsToolkit;
import com.emptyPockets.gui.OrthographicsCameraConvertor;
import com.emptyPockets.gui.StageScreen;
import com.emptyPockets.gui.scene2d.ExceptionHandeling;
import com.emptyPockets.logging.ConsoleScreen;
import com.emptyPockets.test.nat.client.ClientConnection;
import com.emptyPockets.test.nat.server.ServerState;
import com.emptyPockets.utils.OrthoCamController;

public class ClientScreen extends StageScreen {
	String defaulHost = MainTesting.host;
	String defalutTCP = ""+MainTesting.tcpPort;
	String defaultUDP = ""+MainTesting.udpPort;

	ClientConnection connection;

	TextField clientName;
	TextField host;
	TextField udpPort;
	TextField tcpPort;
	TextButton connect;
	TextButton showConsole;
	ConsoleScreen console;
	ServerState state;
	ShapeRenderer shape;
	Vector2 lastMouse;
	OrthoCamController cameraController;
	OrthographicsCameraConvertor camConvert;
	
	public ClientScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		setClearColor(Color.BLACK);
		console = new ConsoleScreen("Console", getSkin());
		connection = new ClientConnection();
		connection.setConsole(console);
		state = new ServerState();
		connection.setServerState(state);
		
		cameraController = new OrthoCamController(getScreenCamera());
		camConvert = new OrthographicsCameraConvertor(getScreenCamera());

		lastMouse = new Vector2();
	}

	@Override
	public void addInputMultiplexer(InputMultiplexer input) {
		super.addInputMultiplexer(input);
		input.addProcessor(cameraController);
	}
	
	@Override
	public void removeInputMultiplexer(InputMultiplexer input) {
		super.removeInputMultiplexer(input);
		input.removeProcessor(cameraController);
	}
	
	@Override
	public void show() {
		super.show();
		shape = new ShapeRenderer();
	}
	
	@Override
	public void hide() {
		super.hide();
		shape.dispose();
		shape = null;
	}
	
	@Override
	public void createStage(Stage stage) {
		host = new TextField(defaulHost, getSkin());
		clientName = new TextField("Guest", getSkin());
		udpPort = new TextField(defaultUDP, getSkin());
		tcpPort = new TextField(defalutTCP, getSkin());
		connect = new TextButton("Connect", getSkin());
		showConsole= new TextButton("console", getSkin());
		
		Window controls = new Window("Server", getSkin());
		controls.row();
		controls.add("Name");
		controls.add(clientName);
		controls.row();
		controls.add("host");
		controls.add(host);
		controls.row();
		controls.add("TCP");
		controls.add(tcpPort);
		controls.row();
		controls.add("UDP");
		controls.add(udpPort);
		controls.row();
		controls.add(connect).fillX().expand().colspan(2);
		controls.row();
		controls.add(showConsole).fillX().expand().colspan(2);
		
		controls.pack();
		
		stage.addActor(console);
		stage.addActor(controls);
		
		showConsole.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				console.setVisible(!console.isVisible());
			}
		});
		connect.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				connect();
			}
		});
	}

	public void connect() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					console.println("About to connect");
					connection.setName(clientName.getText());
					connection.connectServer(host.getText(), Integer.parseInt(tcpPort.getText()), Integer.parseInt(udpPort.getText()));
					console.println("Connected");
				} catch (Exception e) {
					console.println("Error Connecting" + e.getLocalizedMessage());
				}

			}
		}).start();
	}

	@Override
	public void initializeRender() {
		super.initializeRender();
		shape.setProjectionMatrix(getScreenCamera().combined);
	}
	
	@Override
	public void drawBackground(float delta) {
		GraphicsToolkit.draw2DAxis(shape, getScreenCamera(), 100, Color.WHITE);
	}

	@Override
	public void drawScreen(float delta) {
		state.drawServer(shape);
	}

	@Override
	public void drawOverlay(float delta) {

	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		camConvert.camToPanel(x, y, lastMouse);
		connection.setPosition(lastMouse.x, lastMouse.y);
		return super.touchDown(x, y, pointer, button);
	}

}
