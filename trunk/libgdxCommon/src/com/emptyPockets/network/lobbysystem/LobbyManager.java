package com.emptyPockets.network.lobbysystem;

import java.util.ArrayList;
import java.util.HashMap;

import com.emptyPockets.network.framework.GameServer;
import com.emptyPockets.network.framework.User;
import com.emptyPockets.network.framework.messages.ErrorMessage;
import com.emptyPockets.network.framework.routing.MessageRouterByClass;
import com.emptyPockets.network.lobbysystem.messges.LobbyMessage;
import com.emptyPockets.network.lobbysystem.messges.LobbyMessage.LobbyJoinResponse;
import com.emptyPockets.network.lobbysystem.messges.LobbySystemMessage;

public class LobbyManager extends MessageRouterByClass {
	ArrayList<Lobby> lobbies;
	HashMap<Integer, Lobby> lobbyMap;
	GameServer server;

	ErrorMessage unknownErrorMessage;
	LobbyJoinResponse lobbyJoinSuccess;
	LobbyJoinResponse lobbyJoinFailFull;

	public LobbyManager(GameServer server) {
		setup();
		server.registerRouter(this);
	}

	public Lobby getLobby(int lobbyId) {
		return lobbyMap.get(lobbyId);
	}

	public void setup() {
		addValidClasses(LobbySystemMessage.class);

		lobbyJoinSuccess = new LobbyJoinResponse();
		lobbyJoinSuccess.joined = true;
		lobbyJoinSuccess.message = "lobby.joined";

		lobbyJoinFailFull = new LobbyJoinResponse();
		lobbyJoinFailFull.joined = false;
		lobbyJoinFailFull.message = "lobby.full";

		unknownErrorMessage = new ErrorMessage();
		unknownErrorMessage.message = "Unknown Error Message";
	}

	@Override
	public boolean recieveMessage(Object message, Object... additionData) {
		User user = null;
		if (additionData.length == 1 && additionData[0] instanceof User) {
			user = (User) additionData[0];
		}

		if (user == null) {
			return false;
		}

		if (message instanceof LobbyMessage) {
			LobbyMessage lobbyMessage = (LobbyMessage) message;
			lobbyMessage.handleMessage(this, user);
		}

		return false;
	}

	public void sendUnknownError(User user) {
		server.sendTCP(user, unknownErrorMessage);
	}

	public void sendLobbyJoinSuccess(User user) {
		server.sendTCP(user, lobbyJoinSuccess);
	}

	public void sendLobbyFull(User user) {
		server.sendTCP(user, lobbyJoinFailFull);
	}
}
