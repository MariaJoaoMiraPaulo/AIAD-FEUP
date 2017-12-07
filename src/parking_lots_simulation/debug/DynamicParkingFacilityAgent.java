package parking_lots_simulation.debug;

import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.GridPoint;

public class DynamicParkingFacilityAgent extends ParkingFacilityAgent {

	public DynamicParkingFacilityAgent(String name, GridPoint location, int capacity, double pricePerHour, double maxPricePerDay) {
		super(name, location, capacity, pricePerHour, maxPricePerDay);
	}
}
