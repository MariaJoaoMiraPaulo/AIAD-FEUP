package parking_lots_simulation;

import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;

public class GuidedDriverAgent extends Agent {

	private static final long serialVersionUID = 1L;

	public GuidedDriverAgent() {

	}

	@Override
	public void addBehaviour(Behaviour b) {
		// TODO Auto-generated method stub
		super.addBehaviour(b);
	}

	@Override
	protected void setup() {
		super.setup();
		System.out.println("Atãoooooooooooooo eu gosto de guiar para um sitio certos");
	}
}
