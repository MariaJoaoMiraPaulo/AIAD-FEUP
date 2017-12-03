package parking_lots_simulation.behaviours;

import java.util.ArrayList;
import java.util.Iterator;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.Grid;
import sajas.core.behaviours.Behaviour;

public class ExplorerDriverBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid<Object> mainGrid;
	private DriverAgent driverAgent;
	private ArrayList<ParkingFacilityAgent> parkingFacilities;
	private boolean done = false;

	public ExplorerDriverBehaviour(DriverAgent driverAgent, Grid<Object> mainGrid, ArrayList<ParkingFacilityAgent> staticParkingFacilities) {
		super();
		this.driverAgent = driverAgent;
		this.mainGrid = mainGrid;
		this.parkingFacilities = staticParkingFacilities;
	}

	/**
	 * Generates a random coordinate with a distance of 1 from coord
	 * @param coord
	 * @param max
	 * @return
	 */
	private int generateRandomCoordinate(int coord, int max) {
		return Math.min(Math.max(coord + (int) (Math.random()*3)-1, 0), max-1);
	}
	
	@Override
	public void action() {
		Iterator<ParkingFacilityAgent> it = parkingFacilities.iterator();
		
		while(it.hasNext()) {
			ParkingFacilityAgent parking = it.next();
			if(mainGrid.getLocation(parking).equals(mainGrid.getLocation(driverAgent)) && parking.hasAvailableSpace()) {
				done = true;
				parking.setAvailableParkingSpaces(parking.getAvailableParkingSpaces()-1);
				return;
			}
		}
		
		int x = generateRandomCoordinate(mainGrid.getLocation(driverAgent).getX(), mainGrid.getDimensions().getWidth());
		int y = generateRandomCoordinate(mainGrid.getLocation(driverAgent).getY(), mainGrid.getDimensions().getHeight());
		mainGrid.moveTo(driverAgent, x, y);
	}

	@Override
	public boolean done() {
		return done;
	}
}
