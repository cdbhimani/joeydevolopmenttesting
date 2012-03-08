package chapter2;

import chapter2.entities.EntityManager;
import chapter2.entities.Miner;
import chapter2.entities.MinerWife;
import chapter2.messageSystem.MessageDispatcher;

public class MainClass {
    public static void main(String input[]) throws InterruptedException{
	EntityManager entityManager = EntityManager.getInstance();
	MessageDispatcher messageDispatcher = MessageDispatcher.getInstance();
	
	Miner miner = new Miner(EntityManager.MINER_BOB_ID);
	MinerWife minerWife = new MinerWife(EntityManager.MINER_WIFE_ID);
	miner.setName("Miner Bob");
	minerWife.setName("Miner Minni");
	
	//Add Entities
	entityManager.addEntity(miner);
	entityManager.addEntity(minerWife);
	
	
	for(int i = 0; i < 200;i++){
	    miner.update();
	    minerWife.update();
	    
	    messageDispatcher.dispatchDelayedMessages();
	    Thread.sleep(600);
	}
    }
}
