package com.emptyPockets.utils.event;

import static org.junit.Assert.*;

import org.junit.Test;

public class EventRecorderTest {

	@Test
	public void test() throws InterruptedException{
		EventRecorder recorder = new EventRecorder();
		
		recorder.begin("test");
		Thread.sleep(100);
		recorder.end("test");

		recorder.begin("test");
		Thread.sleep(100);
		recorder.end("test");
		
		recorder.begin("test");
		Thread.sleep(100);
		recorder.end("test");
		
		recorder.begin("test");
		Thread.sleep(100);
		recorder.end("test");
		
		float duration = recorder.getAverageEventDuration("test");
		assertEquals(4, recorder.getEventCount("test"));
		assertEquals(0.1, duration, 0.050);
	}
}
