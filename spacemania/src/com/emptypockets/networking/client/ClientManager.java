package com.emptypockets.networking.client;

import java.io.IOException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.emptypockets.engine.Engine;
import com.emptypockets.engine.MovingEntity;
import com.emptypockets.networking.controls.CommandHub;
import com.emptypockets.networking.controls.CommandService;
import com.emptypockets.networking.transfer.ClientLoginRequest;
import com.emptypockets.networking.transfer.ClientStateTransferObject;
import com.emptypockets.networking.transfer.NetworkProtocall;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientManager extends Listener {
	Client client;
	Object engineLock = new Object();
	Engine engine;
	private CommandHub command;
	String username = "client";
	
	public ClientManager(){
		setCommand(new CommandHub());
		setupClient();
		NetworkProtocall.register(client.getKryo());
		CommandService.registerClient(this);
	}
	public void setupClient() {
		client = new Client();
		client.start();
		client.addListener(this);
	}

	public void connect(String address, int tcpPort, int udpPort) throws IOException {
		client.connect(20000, address, tcpPort, udpPort);
	}

	public void stop() {
		client.close();
	}

	public void render(ShapeRenderer shape) {
		shape.begin(ShapeType.Circle);
		shape.setColor(Color.GREEN);
		synchronized (engineLock) {
			if (engine != null) {
				for (MovingEntity e : engine.getEntities()) {
					shape.circle(e.posX(), e.posY(), 10);
				}
			}
		}
		shape.end();
	}

	public void serverStateRecieved(Engine data) {
		synchronized (engineLock) {
			if (this.engine == null || this.engine.getTick() < data.getTick()) {
				this.engine = data;
			}
		}
	}

	public void update(float time) {
		synchronized (engineLock) {
			if (engine != null) {
				engine.update(time);
			}
		}
	}

	@Override
	public void received(Connection connection, Object object) {
		super.received(connection, object);
		if (object instanceof Engine) {
			serverStateRecieved((Engine) object);
		}
	}

	public CommandHub getCommand() {
		return command;
	}

	public void setCommand(CommandHub command) {
		this.command = command;
	}
	public void setUsername(String data) {
		this.username = data;
	}
	public void serverLogin() {
		client.sendTCP(new ClientLoginRequest(username));
	}
	public void send(ClientStateTransferObject state) {
		if(client.isConnected()){
			state.username = username;
			client.sendUDP(state);
		}
	}
}
