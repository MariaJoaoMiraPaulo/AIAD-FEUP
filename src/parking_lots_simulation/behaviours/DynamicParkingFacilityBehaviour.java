package parking_lots_simulation.behaviours;

import java.util.Iterator;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.space.grid.Grid;
import sajas.core.behaviours.Behaviour;

public class DynamicParkingFacilityBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid<Object> mainGrid;
	private ParkingFacilityAgent parkingFacility;

	public DynamicParkingFacilityBehaviour(ParkingFacilityAgent parkingFacility, Grid<Object> mainGrid) {
		super();
		this.parkingFacility = parkingFacility;
		this.mainGrid = mainGrid;
	}

	@Override
	public void action() {
		GridCell<DriverAgent> gridCell = new GridCell<>(mainGrid.getLocation(parkingFacility), DriverAgent.class);

		Iterator<DriverAgent> it = gridCell.items().iterator();

		while (it.hasNext()) {
			DriverAgent driver = it.next();
			parkingFacility.parkCar(driver);
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}
