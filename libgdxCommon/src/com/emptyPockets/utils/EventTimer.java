package com.emptyPockets.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.emptyPockets.utils.EventTimer.EventTimerAccuracy;

public class EventTimer {
	public enum EventTimerAccuracy{
		NANO_TIME,
		MILLI_TIME
	};
	
	EventTimerAccuracy accuracy = EventTimerAccuracy.MILLI_TIME;
	int historyCount = 0;
	
	HashMap<String, Event> currentEvents = new HashMap<String, Event>();
	HashMap<String, ArrayList<Event>> historicEvents = new HashMap<String, ArrayList<Event>>(); 
	
	public EventTimer(){
		
	}
	
	public EventTimer(EventTimerAccuracy accur){
		this.accuracy = accur;
	}
	
	private void ensureKey(String key){
		if(!currentEvents.containsKey(key)){
			currentEvents.put(key, new Event());
		}
		
		if(!historicEvents.containsKey(key)){
			historicEvents.put(key, new ArrayList<Event>(historyCount));
		}
	}
	
	public float getAverageEventLength(String key){
		if(!historicEvents.containsKey(key)){
			return 0;
		}
		
		ArrayList<Event> event = historicEvents.get(key);
		float avg = 0;
		if(event.size()==0){
			return 0;
		}
		
		for(Event e : event){
			avg+=e.seconds;
		}
		avg/=event.size();
		return avg;
	}
	
	public synchronized void begin(String key){
		ensureKey(key);
		
		Event event= currentEvents.get(key);
		event.accuracy = accuracy;
		event.start = getTime(event.accuracy);
	}
	
	public synchronized void end(String key){
		Event event = currentEvents.remove(key);
		event.end = getTime(event.accuracy);
		event.updateTime();
		ArrayList<Event> history = historicEvents.get(key);
		history.add(event);
		history.trimToSize();
	}
	
	private long getTime(EventTimerAccuracy accuracy){
		switch (accuracy) {
			case NANO_TIME: return System.nanoTime();
			case MILLI_TIME: return System.nanoTime();	
			default:return 0;
		}
	}
	
	public void getEventsBetween(String key, long begin, long end){
		
	}
	
}

class EventHolder{
	ArrayList<Event> sortedByStart;
	ArrayList<Event> sortedByEnd;
	
}
