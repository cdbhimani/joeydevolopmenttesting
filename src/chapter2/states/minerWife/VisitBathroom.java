package chapter2.states.minerWife;

import chapter2.entities.MinerWife;
import chapter2.locations.Location;
import chapter2.states.State;

public class VisitBathroom extends State<MinerWife>{

    @Override
    public void Enter(MinerWife wife) {
	if(wife.getLocation() != Location.John){
	    System.out.printf("%s : I've got to go powder my nose.\n", wife.getName());
	    wife.setLocation(Location.John);
	}
    }

    @Override
    public void Execute(MinerWife wife) {
	System.out.printf("%s : Pee Pee Pee.\n", wife.getName());
	wife.getStateMachine().revertToPreviousState();
    }

    @Override
    public void Exit(MinerWife wife) {
	System.out.printf("%s : Awww Sweet Relief. \n", wife.getName());
    }

}
