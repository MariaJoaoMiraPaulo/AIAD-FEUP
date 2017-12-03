package parking_lots_simulation.behaviours;

import java.util.ArrayList;
import java.util.Collections;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import parking_lots_simulation.comparators.SortByDistance;
import repast.simphony.space.grid.Grid;
import sajas.core.behaviours.Behaviour;

public class GuidedDriverBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -766675944062469676L;
	private Grid<Object> mainGrid;
	private DriverAgent driverAgent;
	private ArrayList<ParkingFacilityAgent> parkingFacilities;
	private boolean done = false;
	
	public GuidedDriverBehaviour(DriverAgent guidedDriver, Grid<Object> mainGrid, ArrayList<ParkingFacilityAgent> parkingFacilities) {
		this.driverAgent = guidedDriver;
		this.mainGrid = mainGrid;
		this.parkingFacilities = parkingFacilities;
		
		Collections.sort(this.parkingFacilities, new SortByDistance(this.mainGrid, this.mainGrid.getLocation(this.driverAgent)));
		
		for(int i = 0; i < this.parkingFacilities.size(); i++) {
			double distance = mainGrid.getDistance(this.mainGrid.getLocation(this.driverAgent), this.mainGrid.getLocation(this.parkingFacilities.get(i)));
		}
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean done() {
		return done;
	}

}
