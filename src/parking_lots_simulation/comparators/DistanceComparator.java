package parking_lots_simulation.comparators;

import java.util.Comparator;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class DistanceComparator implements Comparator<ParkingFacilityAgent>{
	
	private Grid<Object> mainGrid;
	private GridPoint currentPosition;
	
	public DistanceComparator(Grid<Object> mainGrid, DriverAgent driver) {
		this.mainGrid = mainGrid;
		this.currentPosition = mainGrid.getLocation(driver);
	}

	/**
	 * @param left 
	 * @param right
	 * @return if left < right: -1  | left = right: 0 | left > right: 1
	 */
	@Override
	public int compare(ParkingFacilityAgent p1, ParkingFacilityAgent p2) {
		double distance1 = mainGrid.getDistance(currentPosition, mainGrid.getLocation(p1));
		double distance2 = mainGrid.getDistance(currentPosition, mainGrid.getLocation(p2));
		
		return Double.compare(distance1, distance2);
	}
	
}
