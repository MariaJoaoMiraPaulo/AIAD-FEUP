package parking_lots_simulation.debug;

import parking_lots_simulation.DriverAgent;
import repast.simphony.space.grid.GridPoint;

/*
 * Debug only
 */
public class GuidedDriverAgent extends DriverAgent {

	public GuidedDriverAgent(String id, GridPoint destination, int durationOfStay) {
		super(id, destination, durationOfStay );
	}
}
