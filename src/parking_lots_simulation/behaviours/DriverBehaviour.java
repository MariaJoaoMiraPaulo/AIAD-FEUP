package parking_lots_simulation.behaviours;

import java.util.HashSet;
import java.util.Set;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.NoPositiveUtilityParkingFoundException;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.VNContains;
import sajas.core.behaviours.Behaviour;

public abstract class DriverBehaviour extends Behaviour {

	private static final long serialVersionUID = 4252257290496119984L;
	protected Set<ParkingFacilityAgent> parkingFacilities;
	protected Grid<Object> mainGrid;
	protected DriverAgent driverAgent;
	private GridPoint destination;
	private VNContains<Object> neighbourhood;
	private boolean done = false;
	private Set<GridPoint> parkingFacilitiesToAvoid;

	public DriverBehaviour(DriverAgent driver, Grid<Object> mainGrid, Set<ParkingFacilityAgent> parkingFacilities) {
		this.driverAgent = driver;
		this.mainGrid = mainGrid;
		this.parkingFacilities = parkingFacilities;
		this.neighbourhood = new VNContains<>(mainGrid);
		this.parkingFacilitiesToAvoid = new HashSet<>();
	}

	public abstract GridPoint getMostUsefulDestination(Set<GridPoint> parkingFacilitiesToAvoid) throws NoPositiveUtilityParkingFoundException;
	
	@Override
	public void action() {
		if(destination == null) {
			try {
				destination = getMostUsefulDestination(parkingFacilitiesToAvoid);
			} catch (NoPositiveUtilityParkingFoundException e) {
				// TODO: Exit system
			}
		}
		
		if(neighbourhood.isNeighbor(driverAgent, destination, 1, 1)) {
			ParkingFacilityAgent parkingFacility = new GridCell<>(destination, ParkingFacilityAgent.class).items().iterator().next();

			if(parkingFacility.isFull()) {
				parkingFacilities.add(parkingFacility);
				destination = null;
			} else {
				parkingFacility.parkCar(driverAgent);
			}
		} else {
			//TODO: MOVE 
		}
	}

	@Override
	public boolean done() {
		return done;
	}

}
