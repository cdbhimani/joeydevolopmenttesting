package com.emptyPockets.network.lobbysystem.messges;

import com.emptyPockets.network.framework.User;
import com.emptyPockets.network.framework.exceptions.TooManyUsersException;
import com.emptyPockets.network.lobbysystem.Lobby;
import com.emptyPockets.network.lobbysystem.LobbyManager;
import com.emptyPockets.network.lobbysystem.messges.RoomMessage.RoomState;

public class LobbyMessage implements LobbySystemMessage {
	public static class LobbyJoinRequest extends LobbyMessage{
		public int lobbyId;
		public void handleMessage(LobbyManager manager, User user){
			Lobby lobby =  manager.getLobby(lobbyId);
			if(lobby == null){
				manager.sendUnknownError(user);
			}
			try {
				lobby.addUser(user);
				manager.sendLobbyJoinSuccess(user);
			} catch (TooManyUsersException e) {
				manager.sendLobbyFull(user);
			}
		}
	}
	
	public static class LobbyJoinResponse extends LobbyMessage{
		public boolean joined;
		public String message;
	}
	
	public static class LobbyState extends LobbyMessage{
		public RoomState[] rooms;
	}
	
	public void handleMessage(LobbyManager manager, User user){
	}
}
