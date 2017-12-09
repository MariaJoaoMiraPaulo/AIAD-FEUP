package parking_lots_simulation.behaviours;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import parking_lots_simulation.exceptions.NoValidDestinationException;
import repast.simphony.space.grid.Grid;

public class GuidedDriverBehaviour extends ParkSearchingBehaviour {
	private static final long serialVersionUID = -766675944062469676L;

	public GuidedDriverBehaviour(DriverAgent guidedDriver, Grid<Object> mainGrid,
			Set<ParkingFacilityAgent> parkingFacilities) {
		super(guidedDriver, mainGrid, parkingFacilities);
	}

	@Override
	public Entry<ParkingFacilityAgent, Double> getMostUsefulDestination(Set<ParkingFacilityAgent> fullParkingFacilities)
			throws NoValidDestinationException {

		Map<ParkingFacilityAgent, Double> validFacilities = new HashMap<ParkingFacilityAgent, Double>();

		for (ParkingFacilityAgent parkingFacility : parkingFacilities) {
			if (!fullParkingFacilities.contains(parkingFacility)) {
				validFacilities.put(parkingFacility, getUtility(parkingFacility));
			}
		}

		return getMaxUtilityPark(validFacilities);
	}

	public Entry<ParkingFacilityAgent, Double> getMaxUtilityPark(Map<ParkingFacilityAgent, Double> parkingFacilities)
			throws NoValidDestinationException {

		Entry<ParkingFacilityAgent, Double> maxEntry = null;

		for (Entry<ParkingFacilityAgent, Double> entry : parkingFacilities.entrySet()) {
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
				maxEntry = entry;
			}
		}

		if (maxEntry == null) {
			throw new NoValidDestinationException();
		}
		return maxEntry;
	}

}
