package parking_lots_simulation.behaviours;

import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.Grid;
import sajas.core.behaviours.Behaviour;

public class StaticParkingFacilityBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7768427673852544310L;
	private Grid<Object> mainGrid;
	private ParkingFacilityAgent parkingFacility;

	public StaticParkingFacilityBehaviour(ParkingFacilityAgent parkingFacility, Grid<Object> mainGrid) {
		super();
		this.parkingFacility = parkingFacility;
		this.mainGrid = mainGrid;
	}

	@Override
	public void action() {

	}

	@Override
	public boolean done() {
		return false;
	}
}
