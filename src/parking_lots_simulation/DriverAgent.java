package parking_lots_simulation;

import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;

public class DriverAgent extends Agent {

	private GridPoint start;
	private GridPoint destination;
	private String id;

	public DriverAgent(String id, GridPoint start, GridPoint destination) {
		this.id = id;
		this.start = start;
		this.destination = destination;
	}
	
	public DriverAgent(String id) {
		this.id = id;
	}

	@Override
	public void addBehaviour(Behaviour b) {
		super.addBehaviour(b);
	}

	@Override
	protected void setup() {
		super.setup();
	}

	public String getId() {
		return id;
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		
		System.out.println("Adeus!!");
	}
}
