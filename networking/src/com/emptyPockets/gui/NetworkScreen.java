package com.emptyPockets.gui;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.emptyPockets.logging.Console;
import com.emptyPockets.logging.ConsoleScreen;
import com.emptyPockets.network.Network;
import com.emptyPockets.network.ServerDiscovery;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class NetworkScreen extends StageScreen{
	Server server;
	Client client;
	ConsoleScreen consoleScreen;
	TextButton consoleButton;
	
	
	TextButton serverButton;
	TextButton clientButton;
	
	public NetworkScreen(InputMultiplexer inputMultiplexer) {
		super(inputMultiplexer);
		
		createConsole();
		
		createClient();
		createServer();
	}
	
	public void createConsole(){
		consoleScreen = new ConsoleScreen("Console", getSkin());
		consoleButton = new TextButton("Con", getSkin());
		consoleScreen.setVisible(false);
		
		consoleButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				consoleScreen.setVisible(!consoleScreen.isVisible());
			}
		});
	}
	
	public void createClient(){
		clientButton = new TextButton("C", getSkin());
		clientButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Thread t = new Thread(){
					public void run() {
						startClient();		
					};
				};
				t.start();
			}});
		client = new Client();
		client.addListener(new NetworkListener("CLIENT : "));
	}
	
	public void createServer(){
		
		serverButton = new TextButton("S", getSkin());
		serverButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				startServer();
			}});
		server = new Server();
		server.addListener(new NetworkListener("SERVER : "));
	}
	
	public void startServer(){
		Console.println("Starting Server");
		
		server.start();
		try {
			server.bind(Network.getNetwork().tcpPort, Network.getNetwork().udpPort);
			Console.println("Server Started");
		} catch (IOException e) {
			e.printStackTrace();
			Console.print("Server Failed");
		}
	}
	
	public void startClient(){
		Console.println("Starting Client");
		ServerDiscovery.findServer(stage, client, Network.getNetwork().tcpPort,Network.getNetwork().udpPort, Network.getNetwork().discoveryTimeout, Network.getNetwork().connectionTimeout);
		Console.println("Finished Client");
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		float size = 1f;
		clientButton.setSize(ScreenSizeHelper.getcmtoPxlX(size), ScreenSizeHelper.getcmtoPxlY(size));
		serverButton.setSize(ScreenSizeHelper.getcmtoPxlX(size), ScreenSizeHelper.getcmtoPxlY(size));
		consoleButton.setSize(ScreenSizeHelper.getcmtoPxlX(size), ScreenSizeHelper.getcmtoPxlY(size));
		
		clientButton.setPosition(0, Gdx.graphics.getHeight()-clientButton.getHeight());
		serverButton.setPosition(Gdx.graphics.getWidth()-clientButton.getWidth(), Gdx.graphics.getHeight()-serverButton.getHeight());
		consoleButton.setPosition(Gdx.graphics.getWidth()-consoleButton.getWidth(), 0);
	}
	
	@Override
	public void createStage(Stage stage) {
		stage.addActor(clientButton);
		stage.addActor(serverButton);
		stage.addActor(consoleButton);
		stage.addActor(consoleScreen);
	}

	@Override
	public void drawScreen(float delta) {
		
	}

	@Override
	public void drawOverlay(float delta) {
		
	}

	@Override
	public void updateLogic(float delta) {
		
	}

}

class NetworkListener extends Listener{
	String tag;
	
	public NetworkListener(String tag) {
		this.tag = tag;
	}
	
	@Override
	public void disconnected(Connection connection) {
		// TODO Auto-generated method stub
		super.disconnected(connection);
		Console.println(tag+"Disconnect from : "+connection.getRemoteAddressTCP());
	}
	
	@Override
	public void connected(Connection connection) {
		// TODO Auto-generated method stub
		super.connected(connection);
		Console.println(tag+"Connection from : "+connection.getRemoteAddressTCP());
	}
	
	@Override
	public void received(Connection connection, Object object) {
		// TODO Auto-generated method stub
		super.received(connection, object);
		Console.println(tag+"Recieved from : "+connection.getRemoteAddressTCP()+" : "+object);
	}
	
}