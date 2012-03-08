package chapter2.messageSystem;

import java.util.ArrayList;
import java.util.PriorityQueue;

import chapter2.entities.BaseGameEntity;
import chapter2.entities.EntityManager;

public class MessageDispatcher {
    static MessageDispatcher instance = new MessageDispatcher();

    PriorityQueue<Telegram> delayedMessages;
    EntityManager entityManager = EntityManager.getInstance();

    public MessageDispatcher() {
	delayedMessages = new PriorityQueue<Telegram>();
    }

    public static MessageDispatcher getInstance() {
	return instance;
    }

    public void discharge(Telegram t) {
	BaseGameEntity e = entityManager.getEntityById(t.getReciever());
	if (e != null) {
	    System.out
		    .printf("###############################\nSending Message From %s for %s recorded at time [%d]. Msg is : %s\n###############################\n",
			    entityManager.getEntityById(t.sender).getName(),
			    entityManager.getEntityById(t.reciever).getName(),
			    System.currentTimeMillis(),
			    MessageType.messageToString(t.message));

	    e.handleMessage(t);
	}
    }

    public void dispatchMessageSeconds(float delaySeconds, int sender,
	    int reciever, int msg, Object info) {
	dispatchMessage((long) (delaySeconds * 1000), sender, reciever, msg,
		info);
    }

    public void dispatchMessage(Long delay, int sender, int reciever, int msg,
	    Object info) {
	Telegram t = new Telegram();
	t.dispatchTime = System.currentTimeMillis() + delay;
	t.sender = sender;
	t.reciever = reciever;
	t.message = msg;
	t.ExtraInfo = info;

	if (delay == 0) {
	    discharge(t);
	} else {
	    System.out
		    .printf("###############################\nDelayed telegram from %s for %s recorded at time [%d] Delayed :[%d]ms. Msg is : %s\n###############################\n",
			    entityManager.getEntityById(t.sender).getName(),
			    entityManager.getEntityById(t.reciever).getName(),
			    System.currentTimeMillis(), delay,
			    MessageType.messageToString(t.message));

	    synchronized (delayedMessages) {
		delayedMessages.add(t);
	    }
	}
    }

    public void dispatchDelayedMessages() {
	synchronized (delayedMessages) {
	    while (delayedMessages.peek() != null
		    && delayedMessages.peek().dispatchTime < System
			    .currentTimeMillis()) {
		discharge(delayedMessages.poll());

	    }
	}
    }
}
