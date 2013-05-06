package com.emptypockets.networking.client;

import java.io.IOException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.emptypockets.engine.Engine;
import com.emptypockets.engine.MovingEntity;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientManager extends Listener {
	Client client;
	Object engineLock = new Object();
	Engine engine;

	public void setupClient() {
		client = new Client();
		client.start();
		client.addListener(this);
	}

	public void connect(String address, int tcpPort, int udpPort) throws IOException {
		client.connect(5000, address, tcpPort, udpPort);
	}

	public void stop() {
		client.close();
	}

	public void render(ShapeRenderer shape) {

		shape.begin(ShapeType.Circle);
		shape.setColor(Color.WHITE);
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
}
