package chapter2.states.minerWife;

import chapter2.entities.EntityManager;
import chapter2.entities.Miner;
import chapter2.entities.MinerWife;
import chapter2.locations.Location;
import chapter2.messageSystem.MessageDispatcher;
import chapter2.messageSystem.MessageType;
import chapter2.messageSystem.Telegram;
import chapter2.states.State;

public class CookStew extends State<MinerWife> {

    @Override
    public void Enter(MinerWife wife) {
	if (!wife.isCooking()) {
	    System.out.printf("%s : I'm putting the stew in the oven.\n",
		    wife.getName());
	    MessageDispatcher.getInstance().dispatchMessageSeconds(3f,
		    wife.getId(), wife.getId(), MessageType.StewReady, null);
	    wife.setCooking(true);
	}
    }

    @Override
    public void Execute(MinerWife wife) {
	System.out.printf(
		"%s : Fussing over the food.\n",
		wife.getName());
    }

    @Override
    public void Exit(MinerWife wife) {

    }
    
    @Override
    public boolean handleMessage(MinerWife wife, Telegram t) {
	switch(t.message){
	 case MessageType.StewReady:
	     System.out.printf("%s : Hunny the Dinner is ready.\n", wife.getName());
	     MessageDispatcher.getInstance().dispatchMessage(0L, wife.getId(),
			EntityManager.MINER_BOB_ID, MessageType.StewReady, null);
	     System.out.printf("%s : Putting the food on the table.\n", wife.getName());
	     wife.setCooking(false);
	     wife.getStateMachine().revertToPreviousState();
		return true;
	}
	return false;
    }

}
