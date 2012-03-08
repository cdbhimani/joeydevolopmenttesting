package chapter2.entities;

import chapter2.messageSystem.Telegram;
import chapter2.states.State;
import chapter2.states.StateMachine;

public abstract class BaseGameEntity {
    static int nextId = 0;
    
    int id;
    String name;
    StateMachine stateMachine;
    
    
    public BaseGameEntity(){
	setId(nextId++);
	init();
    }
    
    public BaseGameEntity(int id){
	setId(id);
	init();
    }
    
    public void setId(int id){
	this.id = id;
    }
    
    public int getId(){
	return id;
    }
    
    public void update(){
	if(stateMachine != null){
	    stateMachine.update();
	}
    }

    public boolean handleMessage(Telegram t){
	if(stateMachine!= null){
	    return stateMachine.handleMessage(t);
	}
	return false;
    }
    
    public StateMachine getStateMachine() {
        return stateMachine;
    }

    public void setStateMachine(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void init() {
	// TODO Auto-generated method stub
	
    }
}
