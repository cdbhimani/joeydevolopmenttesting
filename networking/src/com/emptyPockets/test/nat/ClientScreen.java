package com.emptyPockets.test.nat;

import java.io.IOException;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.gui.StageScreen;
import com.emptyPockets.logging.ConsoleScreen;

public class ClientScreen extends StageScreen {
	ClientConnection connection;
	
	TextField host;
	TextField udpPort;
	TextField tcpPort;
	TextButton connect;
	TextButton sendSTUN;
	
	ConsoleScreen console;
	
	public ClientScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		console = new ConsoleScreen("Console", getSkin());
		connection = new ClientConnection();
		connection.setConsole(console);
	}
	@Override
	public void createStage(Stage stage) {
		host = new TextField("", getSkin());
		udpPort = new TextField("", getSkin());
		tcpPort = new TextField("", getSkin());
		connect = new TextButton("Connect", getSkin());
		sendSTUN = new TextButton("STUN", getSkin());
		
		Window controls = new Window("Server", getSkin());
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
		controls.add(sendSTUN).fillX().expand().colspan(2);
		
		controls.pack();
		stage.addActor(controls);
		stage.addActor(console);
		
		connect.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				connect();
			}
		});
		
		sendSTUN.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stun();
			}
		});
	}
	
	public void connect(){
		console.println("About to connect");
		try {
			connection.connectServerManager(host.getText(), Integer.parseInt(tcpPort.getText()), Integer.parseInt(udpPort.getText()));
		} catch (Exception e) {
			console.println("Error Connecting"+e.getLocalizedMessage());
		}
	}
	
	public void stun(){
		console.println("Sending Stun");
		connection.sendServerManagerSTUN();
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
