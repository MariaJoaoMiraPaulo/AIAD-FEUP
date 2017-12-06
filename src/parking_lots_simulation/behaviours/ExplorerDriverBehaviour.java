package parking_lots_simulation.behaviours;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.NoPositiveUtilityParkingFoundException;
import parking_lots_simulation.ParkingFacilityAgent;
import parking_lots_simulation.comparators.DistanceComparator;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class ExplorerDriverBehaviour extends DriverBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExplorerDriverBehaviour(DriverAgent driverAgent, int period, Grid<Object> mainGrid, Set<ParkingFacilityAgent> parkingFacilities) {
		super(driverAgent, period, mainGrid, parkingFacilities);
	}

	@Override
	public GridPoint getMostUsefulDestination(Set<GridPoint> parkingFacilitiesToAvoid)
			throws NoPositiveUtilityParkingFoundException {
		
		Set<GridPoint> validFacilities = new HashSet<>();

		// Adds all parking facilities not present in parkingFacilitiesToAvoid to the Set
		for(ParkingFacilityAgent parkingFacility : parkingFacilities) {
			GridPoint facilityLocation = mainGrid.getLocation(parkingFacility);
			if(!parkingFacilitiesToAvoid.contains(facilityLocation)) {
				validFacilities.add(facilityLocation);
			}
		}
		
		return Collections.min(validFacilities, new DistanceComparator(mainGrid, driverAgent));
	}

}
