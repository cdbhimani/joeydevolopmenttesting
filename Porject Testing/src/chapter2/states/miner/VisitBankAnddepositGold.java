package chapter2.states.miner;

import chapter2.entities.BaseGameEntity;
import chapter2.entities.Miner;
import chapter2.locations.Location;
import chapter2.states.State;

public class VisitBankAnddepositGold extends State<Miner>{

    @Override
    public void Enter(Miner miner) {
	if(miner.getLocation() != Location.Bank){
	    System.out.printf("%s : I'm off to the bank to drop off my gold\n", miner.getName());
	    miner.setLocation(Location.Bank);
	}
    }

    @Override
    public void Execute(Miner miner) {
	System.out.printf("%s : Hello mr banker I would like to deposit %d in the bank\n", miner.getName(), miner.getGoldCarried());
	miner.depositGold();
	
	if(miner.getGoldInBank() > 100){
	    miner.getStateMachine().changeState(MinerStateManager.getInstance().GoHomeAndSleepTilRested);
	} else if(miner.isThirsty()){
	    miner.getStateMachine().changeState(MinerStateManager.getInstance().QuenchThirst);
	} else{
	    miner.getStateMachine().changeState(MinerStateManager.getInstance().EnterMineAndDigforNugget);
	}
	
	
    }

    @Override
    public void Exit(Miner miner) {
	System.out.printf("%s : I now have %d in the bank\n", miner.getName(), miner.getGoldInBank());
    }

}
