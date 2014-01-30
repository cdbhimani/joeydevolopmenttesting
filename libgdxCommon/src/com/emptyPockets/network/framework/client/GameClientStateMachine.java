package com.emptyPockets.network.framework.client;

import com.emptyPockets.network.framework.GameClient;
import com.emptyPockets.utils.stateMachine.StateMachine;

public class GameClientStateMachine extends StateMachine<GameClient> {

	public GameClientStateMachine(GameClient object) {
		super(object);
	}
	

}
