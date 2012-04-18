package chapter2.states.miner;

import chapter2.entities.Miner;
import chapter2.messageSystem.Telegram;
import chapter2.states.State;

public class MinerGlobalState extends State<Miner> {

    @Override
    public void Enter(Miner miner) {
	System.out.println("GLOBAL");
    }

    @Override
    public void Execute(Miner miner) {
	miner.increaseThirst();
    }

    @Override
    public void Exit(Miner miner) {
	// TODO Auto-generated method stub
	
    }
    
    @Override
    public boolean handleMessage(Miner e, Telegram t) {
	return false;
    }

}
