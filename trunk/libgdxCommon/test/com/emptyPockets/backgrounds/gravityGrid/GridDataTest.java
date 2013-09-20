package com.emptyPockets.backgrounds.gravityGrid;

import com.emptyPockets.backgrounds.grid.Node;
import com.emptyPockets.backgrounds.grid.DualNodeLink;
import com.emptyPockets.utils.event.EventRecorder;

public class GridDataTest {

	public static void main(String input[]) throws InterruptedException {
		Node n1 = new Node();
		Node n2 = new Node();
		float k = 1;
		float b = 0;

		n1.pos.x = 0;
		n1.inverseMass = 0;

		n2.pos.x = 10;
		n2.inverseMass = 1;

		DualNodeLink link = new DualNodeLink(n1, n2, k, b);

		n2.pos.x = 20;

		EventRecorder event = new EventRecorder(5000);

		int avgCount = 1000;
		int callCount = 1000000;

		float timeA = 0;
		float timeB = 0;
		for (int i = 0; i < avgCount; i++) {
			event.begin("A");
			for (int j = 0; j < callCount; j++) {
				link.solve();
			}
			event.end("A");
			
			event.begin("A");
			for (int j = 0; j < callCount; j++) {
				link.solve();
			}
			event.end("A");
			
			event.begin("A");
			for (int j = 0; j < callCount; j++) {
				link.solve();
			}
			event.end("A");
			
			event.begin("A");
			for (int j = 0; j < callCount; j++) {
				link.solve();
			}
			event.end("A");
			
			event.begin("A");
			for (int j = 0; j < callCount; j++) {
				link.solve();
			}
			event.end("A");
			System.out.printf("%e\n", event.getAverageEventDuration("A") / ( callCount));
		}

	}
}
