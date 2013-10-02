package com.emptyPockets.network;

import java.util.ArrayList;

public class Lobby extends UserHub {
	ArrayList<Room> rooms;
	
	public Lobby(){
		super();
		rooms = new ArrayList<Room>();
	}
	
	public void addRoom(Room room){
		synchronized (room) {
			rooms.add(room);
		}
	}
}
