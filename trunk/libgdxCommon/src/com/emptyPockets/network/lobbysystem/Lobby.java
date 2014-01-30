package com.emptyPockets.network.lobbysystem;

import java.util.ArrayList;

import com.emptyPockets.network.framework.UserHub;

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
