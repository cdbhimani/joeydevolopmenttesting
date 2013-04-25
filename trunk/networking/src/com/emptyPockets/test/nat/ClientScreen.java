package com.emptyPockets.test.nat;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.gui.StageScreen;
import com.emptyPockets.gui.scene2d.ExceptionHandeling;
import com.emptyPockets.logging.ConsoleScreen;

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
	TextButton sendSTUN;
	TextButton requestServerState;

	ConsoleScreen console;

	public ClientScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		
		console = new ConsoleScreen("Console", getSkin());
		connection = new ClientConnection();
		connection.setConsole(console);
	}

	@Override
	public void createStage(Stage stage) {
		host = new TextField(defaulHost, getSkin());
		clientName = new TextField("Guest", getSkin());
		udpPort = new TextField(defaultUDP, getSkin());
		tcpPort = new TextField(defalutTCP, getSkin());
		connect = new TextButton("Connect", getSkin());
		requestServerState = new TextButton("Get State", getSkin());

		ExceptionHandeling win = new ExceptionHandeling("Test", getSkin());
		win.pack();
		stage.addActor(win);
		
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
		controls.add(requestServerState).fillX().expand().colspan(2);

		controls.pack();
		stage.addActor(controls);
		stage.addActor(console);

		connect.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				connect();
			}
		});

		requestServerState.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				serverState();
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

	public void serverState() {
		console.println("Request State");
		connection.requestServerStatus();
	}

	@Override
	public void drawBackground(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawScreen(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawOverlay(float delta) {
		// TODO Auto-generated method stub

	}

}
