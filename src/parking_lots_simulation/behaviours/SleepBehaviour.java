package parking_lots_simulation.behaviours;

import sajas.core.behaviours.Behaviour;

public class SleepBehaviour extends Behaviour {
	
	private boolean done = false;
	private int numberOfTicksRemaining;
	
	public SleepBehaviour(int numberOfTicksRemaining) {
		this.numberOfTicksRemaining = numberOfTicksRemaining;
	}

	@Override
	public void action() {
		
		if(numberOfTicksRemaining == 0) {
			done = true;
		}
		else {
			numberOfTicksRemaining--;
		}
		
	}

	@Override
	public boolean done() {
		return done;
	}

}
