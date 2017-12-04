package parking_lots_simulation.comparators;

import java.util.Comparator;

import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class SortByDistance implements Comparator<ParkingFacilityAgent>{
	
	private Grid<Object> mainGrid;
	private GridPoint currentPosition;
	
	public SortByDistance(Grid<Object> mainGrid, GridPoint currentPosition) {
		this.mainGrid = mainGrid;
		this.currentPosition = currentPosition;
	}

	/**
	 * @param left 
	 * @param right
	 * @return if left < right: -1  | left = right: 0 | left > right: 1
	 */
	@Override
	public int compare(ParkingFacilityAgent left, ParkingFacilityAgent right) {
		
		System.out.println("boas");

		GridPoint leftPoint = mainGrid.getLocation(left);
		GridPoint rightPoint = mainGrid.getLocation(right);
		
		double distanceLeft = mainGrid.getDistance(currentPosition, leftPoint);
		double distanceRight = mainGrid.getDistance(currentPosition, rightPoint);
		
		if(distanceLeft < distanceRight) {
			return -1;
		}
		else if(distanceLeft == distanceRight) {
			return 0;
		}
		
		return 1;
	}
	
}
