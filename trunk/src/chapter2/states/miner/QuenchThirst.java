package chapter2.states.miner;

import java.beans.Statement;

import chapter2.entities.BaseGameEntity;
import chapter2.entities.Miner;
import chapter2.locations.Location;
import chapter2.states.State;

public class QuenchThirst extends State<Miner> {

    @Override
    public void Enter(Miner miner) {
	if(miner.getLocation() != Location.Saloon){
	    System.out.printf("%s : I feeling a bit thirsty, better head to the saloon\n",miner.getName());
	    miner.setLocation(Location.Saloon);
	}
    }

    @Override
    public void Execute(Miner miner) {
	System.out.printf("%s : Thats a nice drink :). \n", miner.getName());
	miner.setThirst(0);
	miner.getStateMachine().changeState(MinerStateManager.getInstance().EnterMineAndDigforNugget);

    }

    @Override
    public void Exit(Miner miner) {
	System.out.printf("%s : I feel better after my drinks, back to it\n",
		miner.getName());
    }
}
