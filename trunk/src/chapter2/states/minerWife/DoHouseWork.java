package chapter2.states.minerWife;

import java.util.Random;

import chapter2.entities.MinerWife;
import chapter2.locations.Location;
import chapter2.states.State;

public class DoHouseWork extends State<MinerWife> {

    Random r = new Random();

    @Override
    public void Enter(MinerWife wife) {
	if (wife.getLocation() != Location.Home) {
	    System.out.printf("%s : I'm heading to the shack\n", wife.getName());
	}
    }

    @Override
    public void Execute(MinerWife wife) {

	switch (r.nextInt(15)) {
	    case 0:
		System.out.printf("%s : Sweeping the floor\n", wife.getName());
		break;
	    case 1:
		System.out.printf("%s : Makeing the bed\n", wife.getName());
		break;
	    case 2:
		System.out.printf("%s : Mopping the floor\n", wife.getName());
		break;
	}
    }

    @Override
    public void Exit(MinerWife wife) {

    }

}
