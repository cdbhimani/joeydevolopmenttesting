package chapter2.states;

import chapter2.entities.BaseGameEntity;
import chapter2.messageSystem.Telegram;
import chapter2.states.miner.MinerStateManager;

public abstract class State<T extends BaseGameEntity> {
    public State(){
    }
    
    public abstract void Enter(T e);
    public abstract void Execute(T e);
    public abstract void Exit(T e);
    
    public boolean handleMessage(T e, Telegram t){
	return false;
    }
}
