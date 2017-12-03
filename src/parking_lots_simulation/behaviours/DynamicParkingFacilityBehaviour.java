package parking_lots_simulation.behaviours;

import java.util.ArrayList;
import java.util.Iterator;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.Grid;
import sajas.core.behaviours.Behaviour;

public class DynamicParkingFacilityBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid<Object> mainGrid;
	private ParkingFacilityAgent parkingFacility;
	boolean done = false;
	private ArrayList<DriverAgent> parkedDrivers;
	
	public DynamicParkingFacilityBehaviour(ParkingFacilityAgent parkingFacility, Grid<Object> mainGrid) {
		super();
		this.parkingFacility = parkingFacility;
		this.mainGrid = mainGrid;
		this.parkedDrivers = new ArrayList<DriverAgent>();
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		//Get parked drivers 
		Iterable<Object> driversAt = mainGrid.getObjectsAt(mainGrid.getLocation(parkingFacility).getX(),mainGrid.getLocation(parkingFacility).getY());
		Iterator<Object> it = driversAt.iterator();
		
		while(it.hasNext()) {
			Object object = it.next();
			if(object instanceof DriverAgent) {
				DriverAgent driver = (DriverAgent) object;
				if(!parkedDrivers.contains(driver))
						parkedDrivers.add(driver);
			}
		}
	
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return done;
	}
}
