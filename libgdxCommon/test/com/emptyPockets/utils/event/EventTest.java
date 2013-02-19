package com.emptyPockets.utils.event;

import static org.junit.Assert.*;

import org.junit.Test;

import com.emptyPockets.utils.event.Event.EventTimerAccuracy;

public class EventTest{

	@Test
	public void testEventTimeMeasureMilliCorrectly() throws InterruptedException{
		Event event = new Event(EventTimerAccuracy.MILLI_TIME);
		event.start();
		Thread.sleep(500);
		event.end();
		
		assertEquals(0.5, event.getSeconds(), 0.02);
	}
	
	@Test
	public void testEventTimeMeasureNanoCorrectly() throws InterruptedException{
		Event event = new Event(EventTimerAccuracy.NANO_TIME);
		event.start();
		Thread.sleep(500);
		event.end();
		
		assertEquals(0.5, event.getSeconds(), 0.02);
	}
}

