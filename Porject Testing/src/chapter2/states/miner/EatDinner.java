package chapter2.states.miner;

import java.beans.Statement;

import chapter2.entities.BaseGameEntity;
import chapter2.entities.Miner;
import chapter2.locations.Location;
import chapter2.states.State;

public class EatDinner extends State<Miner> {

    @Override
    public void Enter(Miner miner) {
	if(miner.getLocation() != Location.Home){
	    System.out.printf("%s : That food smeels good\n",miner.getName());
	    miner.setLocation(Location.Home);
	}
    }

    @Override
    public void Execute(Miner miner) {
	System.out.printf("%s : That tastes really good :). \n", miner.getName());
	miner.getStateMachine().changeState(MinerStateManager.getInstance().EnterMineAndDigforNugget);
    }

    @Override
    public void Exit(Miner miner) {
	System.out.printf("%s :That food was great thanks hun\n",
		miner.getName());
    }
}
