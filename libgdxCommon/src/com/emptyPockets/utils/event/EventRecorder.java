package com.emptyPockets.utils.event;

import java.util.HashMap;

import com.emptyPockets.utils.event.Event.EventTimerAccuracy;

public class EventRecorder {
	
	int historyCount = 0;
	
	HashMap<String, Event> currentEvents = new HashMap<String, Event>();
	HashMap<String, EventHistory> historicEvents = new HashMap<String, EventHistory>(); 
	
	EventTimerAccuracy accuracy = EventTimerAccuracy.MILLI_TIME;
	
	public EventRecorder(){
	}
	
	public EventRecorder(int historyCount){
		this.historyCount = historyCount;
	}
	
	public EventRecorder(EventTimerAccuracy accur){
		this.accuracy = accur;
	}
	
	private void ensureKey(String key){
		if(!currentEvents.containsKey(key)){
			currentEvents.put(key, new Event(accuracy));
		}
		
		if(!historicEvents.containsKey(key)){
			historicEvents.put(key, new EventHistory(historyCount));
		}
	}
	
	public float getAverageEventDuration(String key){
		if(!historicEvents.containsKey(key)){
			return 0;
		}
		EventHistory history = historicEvents.get(key);
		return history.getAverageDuration();
	}
	
	public synchronized void begin(String key){
		ensureKey(key);
		
		Event event= currentEvents.get(key);
		event.start();
	}
	
	public synchronized int getEventCount(String key){
		EventHistory hist = historicEvents.get(key);
		if(hist == null){
			return 0;
		}
		return hist.getCount();
	}
	public synchronized void end(String key){
		Event event = currentEvents.remove(key);
		event.end();
		historicEvents.get(key).add(event);
	}

	public EventTimerAccuracy getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(EventTimerAccuracy accuracy) {
		this.accuracy = accuracy;
	}
	
	
}
