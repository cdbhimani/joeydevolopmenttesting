package chapter2.entities;

import chapter2.locations.Location;
import chapter2.states.StateMachine;
import chapter2.states.minerWife.MinerWifeStateManager;


public class MinerWife extends BaseGameEntity {

    int location = Location.Home;
    boolean cooking = false;
    public MinerWife(){
	super();
    }
 
    public MinerWife(int id){
 	super(id);
     }
    @Override
    public void init(){
	setStateMachine(new StateMachine<MinerWife>(this));
	getStateMachine().setGlobalState(MinerWifeStateManager.getInstance().MinerWifeGlobalState);
	getStateMachine().changeState(MinerWifeStateManager.getInstance().DoHouseWork);
    }
    @Override
    public void update() {
	super.update();
    }
    public int getLocation() {
        return location;
    }
    public void setLocation(int location) {
        this.location = location;
    }

    public boolean isCooking() {
        return cooking;
    }

    public void setCooking(boolean cooking) {
        this.cooking = cooking;
    }

}
