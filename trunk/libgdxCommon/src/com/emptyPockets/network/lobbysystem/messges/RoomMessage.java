package com.emptyPockets.network.lobbysystem.messges;

import com.emptyPockets.network.framework.messages.UserDetails;

public class RoomMessage implements LobbySystemMessage {
	public static class RoomJoinRequest extends RoomMessage{
		public short lobbyId;
		public short roomId;
	}
	
	public static class RoomJoinResponse extends RoomMessage{
		public boolean joined;
	}
	
	public static class RoomState extends RoomMessage{
		public short usersCount;
		public short maxUserCount;
		public String roomName;
	}
	
	public static class RoomDetailedState extends RoomState{
		public UserDetails[] users;
	}
}
