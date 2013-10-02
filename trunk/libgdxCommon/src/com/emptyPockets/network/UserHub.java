package com.emptyPockets.network;

import java.util.ArrayList;

public class UserHub {
	int maxUsers = 0;
	ArrayList<User> users;
	
	public UserHub(){
		users = new ArrayList<User>();
	}
	
	public void addUser(User user) throws TooManyUsersException{
		synchronized (users) {
			if(users.size() > maxUsers){
				throw new TooManyUsersException();
			}
			users.add(user);
		}
	}
	
	public void removeUser(User user){
		synchronized (users) {
			users.remove(user);
		}
	}
	
	public int getUserCount(){
		synchronized (users) {
			return users.size();
		}
	}
	
}
