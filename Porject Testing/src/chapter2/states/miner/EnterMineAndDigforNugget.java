package chapter2.states.miner;

import chapter2.entities.BaseGameEntity;
import chapter2.entities.Miner;
import chapter2.locations.Location;
import chapter2.states.State;

public class EnterMineAndDigforNugget extends State<Miner>{

    public EnterMineAndDigforNugget(){
    }
    
    @Override
    public void Enter(Miner miner) {
	if(miner.getLocation() != Location.Goldmine){
	    System.out.printf("%s: Im heading to the goldmine to get some gold\n", miner.getName());
	    miner.setLocation(Location.Goldmine);
	}
	
    }

    @Override
    public void Execute(Miner miner) {	
	System.out.printf("%s : I'm mineing some gold :)\n", miner.getName());

	miner.addGold(1);
	miner.increaseFatigue();
	
	if(miner.isPocketsFull()){
	    miner.getStateMachine().changeState(MinerStateManager.getInstance().VisitBankAnddepositGold);
	}
	
	if(miner.isThirsty()){
	    miner.getStateMachine().changeState(MinerStateManager.getInstance().QuenchThirst);
	}
	
	if(miner.isTired()){
	    miner.getStateMachine().changeState(MinerStateManager.getInstance().GoHomeAndSleepTilRested);
	}
	
    }

    @Override
    public void Exit(Miner miner) {
	System.out.printf("%s : I'm leaving the mine with my %d gold\n", miner.getName(), miner.getGoldCarried());
    }
}
