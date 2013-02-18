package com.emptyPockets.utils;

import com.emptyPockets.utils.EventTimer.EventTimerAccuracy;

public class Event{
	EventTimerAccuracy accuracy;
	public long start;
	public long end;
	public float seconds;
	
	public void updateTime(){
		seconds = end-start;
		switch (accuracy) {
			case MILLI_TIME:  seconds = seconds/1e3f;break;
			case NANO_TIME :  seconds = seconds/1e9f;break;
		}
		seconds = end-start;
	}
	public float getSeconds(){
		return seconds;
	}
}