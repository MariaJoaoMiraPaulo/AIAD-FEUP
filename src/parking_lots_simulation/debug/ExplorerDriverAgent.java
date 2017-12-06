package parking_lots_simulation.debug;

import parking_lots_simulation.DriverAgent;
import repast.simphony.space.grid.GridPoint;

/*
 * Debug only
 */
public class ExplorerDriverAgent extends DriverAgent {

	public ExplorerDriverAgent(String id, GridPoint destination, int durationOfStay) {
		super(id, destination, durationOfStay);
	}

}
