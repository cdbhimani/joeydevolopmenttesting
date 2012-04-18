package chapter2.states.miner;

import chapter2.entities.BaseGameEntity;
import chapter2.entities.EntityManager;
import chapter2.entities.Miner;
import chapter2.entities.MinerWife;
import chapter2.locations.Location;
import chapter2.messageSystem.MessageDispatcher;
import chapter2.messageSystem.MessageType;
import chapter2.messageSystem.Telegram;
import chapter2.states.State;
import chapter2.states.minerWife.MinerWifeStateManager;

public class GoHomeAndSleepTilRested extends State<Miner>{

    @Override
    public void Enter(Miner miner) {
	if(miner.getLocation() != Location.Home){
	    System.out.printf("%s : I'm feeling a bit tired, better head home to sleep\n", miner.getName());
	    miner.setLocation(Location.Home);
	   
	    MessageDispatcher.getInstance().dispatchMessage(
		    0L, miner.getId(), EntityManager.MINER_WIFE_ID, MessageType.HiHoneyImHome, null);
	}
    }

    @Override
    public void Execute(Miner miner) {
	System.out.printf("%s : Zzzzzz\n", miner.getName());
	if(!miner.isFullyRested()){
	    miner.decreaseFatigue();
	}else{
	    miner.getStateMachine().changeState(MinerStateManager.getInstance().EnterMineAndDigforNugget);
	}
    }

    @Override
    public void Exit(Miner miner) {
	System.out.printf("%s : I feel so rested now\n", miner.getName());
    }
    
    public boolean handleMessage(Miner miner, Telegram t) {
   	switch (t.message) {
   	    case MessageType.StewReady:
   		miner.getStateMachine().changeState(MinerStateManager.getInstance().EatDinner);
   		return true;
   	}
   	return false;
       }
}
