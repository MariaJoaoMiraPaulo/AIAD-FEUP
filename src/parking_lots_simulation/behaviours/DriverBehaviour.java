package parking_lots_simulation.behaviours;

import java.util.Set;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.Grid;
import sajas.core.behaviours.SequentialBehaviour;

public class DriverBehaviour extends SequentialBehaviour {

	private static final long serialVersionUID = 4252257290496119984L;
	/**
	 * All parking facilities open
	 */
	protected Set<ParkingFacilityAgent> parkingFacilities;

	/**
	 * Grid that represents the simulation space
	 */
	protected Grid<Object> grid;

	/**
	 * Driver agent where this behaviour is acting
	 */
	protected DriverAgent driver;

	public DriverBehaviour(DriverAgent driver, int period, Grid<Object> mainGrid, Set<ParkingFacilityAgent> parkingFacilities) {
		super(driver);
		this.driver = driver;
		this.grid = mainGrid;
		this.parkingFacilities = parkingFacilities;
	}
}
