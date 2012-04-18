package chapter2.states.minerWife;

import chapter2.entities.MinerWife;
import chapter2.messageSystem.MessageType;
import chapter2.messageSystem.Telegram;
import chapter2.states.State;
import chapter2.states.miner.MinerStateManager;

public class MinerWifeGlobalState extends State<MinerWife>{

    @Override
    public void Enter(MinerWife wife) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void Execute(MinerWife wife) {
	if(Math.random() < 0.01){
	    wife.getStateMachine().changeState(MinerWifeStateManager.getInstance().VisitBathroom);
	}
    }

    @Override
    public void Exit(MinerWife wife) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public boolean handleMessage(MinerWife wife, Telegram t) {
	switch (t.message) {
	    case MessageType.HiHoneyImHome:
		wife.getStateMachine().changeState(MinerWifeStateManager.getInstance().CookStew);
		return true;
	}
	return false;
    }
}
