package parking_lots_simulation.debug;

import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.GridPoint;

public class DynamicParkingFacilityAgent extends ParkingFacilityAgent {

	public DynamicParkingFacilityAgent(GridPoint location, int capacity, double price) {
		super(location, capacity, price);
	}
}
