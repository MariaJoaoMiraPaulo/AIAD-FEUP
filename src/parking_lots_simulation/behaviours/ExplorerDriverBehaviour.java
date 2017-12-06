package parking_lots_simulation.behaviours;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import parking_lots_simulation.comparators.DistanceComparator;
import parking_lots_simulation.exceptions.NoValidDestinationException;
import repast.simphony.space.grid.Grid;

public class ExplorerDriverBehaviour extends ParkSearchingBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExplorerDriverBehaviour(DriverAgent driverAgent, int period, Grid<Object> mainGrid,
			Set<ParkingFacilityAgent> parkingFacilities) {
		super(driverAgent, period, mainGrid, parkingFacilities);
	}

	@Override
	public Entry<ParkingFacilityAgent, Double> getMostUsefulDestination(
			Set<ParkingFacilityAgent> fullParkingFacilities) throws NoValidDestinationException {

		Set<ParkingFacilityAgent> validFacilities = new HashSet<>();

		// Adds all parking facilities not present in parkingFacilitiesToAvoid to the
		// Set
		for (ParkingFacilityAgent parkingFacility : parkingFacilities) {
			if (!fullParkingFacilities.contains(parkingFacility)) {
				validFacilities.add(parkingFacility);
			}
		}

		if (validFacilities.size() == 0) {
			throw new NoValidDestinationException();
		}

		ParkingFacilityAgent destination = Collections.min(validFacilities, new DistanceComparator(grid, driver));

		return new SimpleEntry<ParkingFacilityAgent, Double>(destination, getUtility(destination));
	}

}
