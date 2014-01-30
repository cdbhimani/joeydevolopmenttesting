package com.emptyPockets.network.framework.routing;

import java.util.ArrayList;

public class MessageRouterByClass extends MessageRouter {

	ArrayList<Class> classes;
	
	public MessageRouterByClass(){
		classes = new ArrayList<Class>();
	}
	
	public void addValidClasses(Class cls){
		classes.add(cls);
	}
	
	public void removeValidClasses(Class cls){
		classes.remove(cls);
	}
	
	@Override
	public boolean accepts(Object message) {
		for(Class cls : classes){
			if(cls.isInstance(message)){
				return true;
			}
		}
		return false;
	}
}
