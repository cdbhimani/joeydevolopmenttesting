package com.emptypockets.networking.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.emptyPockets.commandline.CommandLine;
import com.emptyPockets.logging.Console;
import com.emptypockets.engine.Engine;
import com.emptypockets.engine.MovingEntity;
import com.emptypockets.networking.controls.CommandService;
import com.emptypockets.networking.server.ServerManager;
import com.emptypockets.networking.transfer.ClientLoginRequest;
import com.emptypockets.networking.transfer.ClientLogoutRequest;
import com.emptypockets.networking.transfer.ClientStateTransferObject;
import com.emptypockets.networking.transfer.LoginFailedResponse;
import com.emptypockets.networking.transfer.LoginSucessfulResponse;
import com.emptypockets.networking.transfer.NetworkProtocall;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientManager extends Listener {

	Client client;
	Object engineLock = new Object();
	Engine engine;
	private CommandLine command;
	String username = "client";
	ServerManager serverManager;
	boolean loggedIn = false;

	public ClientManager() {
		setCommand(new CommandLine());
		setupClient();
		NetworkProtocall.register(client.getKryo());
		CommandService.registerClient(this);
	}

	public void setupClient() {
		client = new Client();
		client.start();
		client.addListener(this);
	}

	public void setupServer(int updateCount) {
		serverManager = new ServerManager(updateCount);
	}

	public ServerManager getServerManager() {
		if (serverManager == null) {
			serverManager = new ServerManager();
		}
		return serverManager;
	}

	public MovingEntity getEntity(String name) {
		MovingEntity ent = null;

		synchronized (engineLock) {
			if (engine != null) {
				ent = engine.getEntity(name);
			}
		}
		return ent;
	}

	public void connect(String address, int tcpPort, int udpPort) throws IOException {
		loggedIn = false;
		Console.printf("Connecting to server %s : %d,%d\n", address, tcpPort, udpPort);
		client.connect(20000, address, tcpPort, udpPort);
	}

	public void listStatus() {
		Console.println("Connected : " + client.isConnected() + " - LoggedIn : " + loggedIn);
	}

	public void listNetworkServers(final int udpPort, final int timeoutSec, final NetworkDiscoveryInterface callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Console.println("Searching for hosts Port [" + udpPort + "] holding for [" + timeoutSec + " s]");
				List<InetAddress> hosts = client.discoverHosts(udpPort, timeoutSec * 1000);
				Console.println("Found : " + hosts.size());
				for (InetAddress host : hosts) {
					Console.println("Host : " + host.getHostAddress() + " - " + host.getHostName());
				}
				if (callback != null) {
					callback.notifyDiscoveredHosts(hosts);
				}
			}
		}).start();
	}

	public void stop() {
		Console.println("Disconnecting from server");
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
		} else if (object instanceof LoginSucessfulResponse) {
			loginSucessfull((LoginSucessfulResponse) object);
		} else if (object instanceof LoginFailedResponse) {
			loginFailed((LoginFailedResponse) object);
		}
	}

	public CommandLine getCommand() {
		return command;
	}

	public void setCommand(CommandLine command) {
		this.command = command;
	}

	public void setUsername(String data) {
		this.username = data;
	}

	public void serverLogin() {
		Console.println("Sending Login Request to server");
		client.sendTCP(new ClientLoginRequest(username));
	}

	public void serverLogout() {
		Console.println("Sending Logout Request to server");
		client.sendTCP(new ClientLogoutRequest(username));
	}

	public void loginSucessfull(LoginSucessfulResponse sucessfull) {
		Console.println("Logout Response : Sucessfully Logged in.");
		loggedIn = true;
	}

	public void loginFailed(LoginFailedResponse failed) {
		Console.println("Logout Response : Login FAILED - " + failed.getRejectionReason());
		loggedIn = false;
	}

	public void send(ClientStateTransferObject state) {
		if (client.isConnected() && loggedIn) {
			state.username = username;
			client.sendUDP(state);
		}
	}

	public String getUsername() {
		return username;
	}
}
