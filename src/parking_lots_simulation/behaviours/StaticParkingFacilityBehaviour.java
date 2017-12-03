package parking_lots_simulation.behaviours;

import java.util.ArrayList;
import java.util.Iterator;

import org.antlr.works.utils.Console;

import parking_lots_simulation.DriverAgent;
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
	boolean done = false;
	private ArrayList<DriverAgent> parkedDrivers;
	
	
	public StaticParkingFacilityBehaviour(ParkingFacilityAgent parkingFacility, Grid<Object> mainGrid) {
		super();
		this.parkingFacility = parkingFacility;
		this.mainGrid = mainGrid;
		this.parkedDrivers = new ArrayList<DriverAgent>();
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		//Get drivers trying the park
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
