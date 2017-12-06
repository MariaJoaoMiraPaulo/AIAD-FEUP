package parking_lots_simulation.behaviours;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.NoPositiveUtilityParkingFoundException;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class GuidedDriverBehaviour extends DriverBehaviour {
	private static final long serialVersionUID = -766675944062469676L;
	
	public GuidedDriverBehaviour(DriverAgent guidedDriver, int period, Grid<Object> mainGrid, Set<ParkingFacilityAgent> parkingFacilities) {
		super(guidedDriver, period, mainGrid, parkingFacilities);
	}

	@Override
	public GridPoint getMostUsefulDestination(Set<GridPoint> parkingFacilitiesToAvoid)
			throws NoPositiveUtilityParkingFoundException {
		
		Map<ParkingFacilityAgent, Double> validFacilities = new HashMap<ParkingFacilityAgent, Double>();
		
		for(ParkingFacilityAgent parkingFacility : parkingFacilities) {
			GridPoint parkPosition = mainGrid.getLocation(parkingFacility);
			double distance_to_destination = mainGrid.getDistanceSq(parkPosition, driverAgent.getDestination());
			double driverUtility = getUtility(distance_to_destination, parkingFacility.getPrice());
			
			validFacilities.put(parkingFacility, driverUtility);
		}
		
		return mainGrid.getLocation(getMaxUtilityPark(validFacilities));
	
	}
	
	public ParkingFacilityAgent getMaxUtilityPark(Map<ParkingFacilityAgent, Double> parkingFacilities) {
		
		Entry<ParkingFacilityAgent, Double> maxEntry = null;
		
		for(Entry<ParkingFacilityAgent, Double> entry : parkingFacilities.entrySet()) {
				if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
			    {
			        maxEntry = entry;
			    }	
		}
		
		return maxEntry.getKey();
	}

}
