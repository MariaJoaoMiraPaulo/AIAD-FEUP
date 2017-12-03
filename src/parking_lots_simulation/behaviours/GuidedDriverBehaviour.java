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

public class GuidedDriverBehaviour extends DriverBehaviour {
	private static final long serialVersionUID = -766675944062469676L;
	
	public GuidedDriverBehaviour(DriverAgent guidedDriver, Grid<Object> mainGrid, Set<ParkingFacilityAgent> parkingFacilities) {
		super(guidedDriver, mainGrid, parkingFacilities);
	}

	@Override
	public GridPoint getMostUsefulDestination(Set<GridPoint> parkingFacilitiesToAvoid)
			throws NoPositiveUtilityParkingFoundException {
		
		// TODO: Change to GuidedDriverBehaviour

		Set<GridPoint> validFacilities = new HashSet<>();

		// Adds all parking facilities not present in parkingFacilitiesToAvoid to the Set
		for(ParkingFacilityAgent parkingFacility : parkingFacilities) {
			if(!parkingFacilitiesToAvoid.contains(parkingFacility)) {
				validFacilities.add(mainGrid.getLocation(parkingFacility));
			}
		}

		return Collections.min(validFacilities, new DistanceComparator(mainGrid, driverAgent));
	}
}
