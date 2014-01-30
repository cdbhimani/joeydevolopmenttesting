package com.emptyPockets.utils.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EventHistoryTest {
		
	@Test
	public void testHistoryRemovesOldestHistory() throws InterruptedException{
		EventHistory history = new EventHistory(3);
		
		Event event;
		
		//High Time
		event = new Event();
		event.start();
		Thread.sleep(500);
		event.end();
		history.add(event);
		
		event = new Event();
		event.start();
		Thread.sleep(500);
		event.end();
		history.add(event);
		
		event = new Event();
		event.start();
		Thread.sleep(500);
		event.end();
		history.add(event);
		
		assertEquals(0.5, history.getAverageDuration(),0.05);
		
		//High Time
		event = new Event();
		event.start();
		Thread.sleep(100);
		event.end();
		history.add(event);
		
		event = new Event();
		event.start();
		Thread.sleep(100);
		event.end();
		history.add(event);
		
		event = new Event();
		event.start();
		Thread.sleep(100);
		event.end();
		history.add(event);
		
		assertEquals(0.1, history.getAverageDuration(),0.05);

	}
}
