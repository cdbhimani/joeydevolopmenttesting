package com.emptyPockets.network.framework;

import java.util.ArrayList;

import com.emptyPockets.network.framework.exceptions.TooManyUsersException;

public class UserHub {
	int maxUsers = 0;
	ArrayList<User> users;
	ArrayList<UserHubListener> listeners;
	
	public UserHub(){
		users = new ArrayList<User>();
		listeners = new ArrayList<UserHubListener>();
	}
	

	public void addUserHubListener(UserHubListener listener){
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public void addUser(User user) throws TooManyUsersException{
		synchronized (users) {
			if(users.size() > maxUsers){
				throw new TooManyUsersException();
			}
			users.add(user);
		}
		
		synchronized (listeners) {
			for(UserHubListener list : listeners){
				list.userConnected(user);
			}
		}
	}
	
	public void removeUser(User user){
		synchronized (users) {
			users.remove(user);
		}
		
		synchronized (listeners) {
			for(UserHubListener list : listeners){
				list.userDisconnected(user);
			}
		}
	}
	
	public int getUserCount(){
		synchronized (users) {
			return users.size();
		}
	}
	
}
