package com.emptyPockets.network.framework.client;

import com.emptyPockets.network.framework.GameClient;
import com.emptyPockets.utils.stateMachine.State;
import com.emptyPockets.utils.stateMachine.StateMachine;

public class GameClientDisconnectedState implements State<GameClientStateMachine, GameClient>{

	@Override
	public void enterState(StateMachine<GameClient> state) {
		
	}

	@Override
	public void exitState(StateMachine<GameClient> state) {
		// TODO Auto-generated method stub
		state.getObject();
	}

	@Override
	public void update(StateMachine<GameClient> state) {
		// TODO Auto-generated method stub
		
	}

}
