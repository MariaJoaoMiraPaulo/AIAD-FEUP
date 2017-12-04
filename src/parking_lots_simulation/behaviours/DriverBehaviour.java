package parking_lots_simulation.behaviours;

import java.util.HashSet;
import java.util.Iterator;
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
		
		ParkingFacilityAgent parkingFacility = (ParkingFacilityAgent)mainGrid.getObjectAt(destination.getX(), destination.getY());
		GridPoint agentPosition = mainGrid.getLocation(driverAgent);
		if(neighbourhood.isNeighbor(driverAgent, parkingFacility, 1, 1) || verifyDiagonals(agentPosition)) {
			if(parkingFacility.isFull()) {
				//parkingFacilities.add(parkingFacility);
				//destination = null;
			} else {
				mainGrid.moveTo(driverAgent, destination.getX(), destination.getY());
				parkingFacility.parkCar(driverAgent);
				// TODO random time
				driverAgent.addBehaviour(new SleepBehaviour(driverAgent, parkingFacility, 5));
				done = true;
			}
		} else { 
			int x = directions(destination.getX(), agentPosition.getX());
			int y = directions(destination.getY(), agentPosition.getY());
			
			mainGrid.moveTo(driverAgent, agentPosition.getX() + x , agentPosition.getY() + y);
		}
	}

	@Override
	public boolean done() {
		return done;
	}
	
	/**
	 * @param park
	 * @param agent
	 * @return 0 if are on the same line/column, 1 or -1 on a different 
	 */ 
	private int directions(int park, int agent) {

		int delta = park - agent;
		
		if(delta == 0) {
			return 0;
		}
		else if(delta > 0) {
			return 1;
		}
		
		return -1;
	}
	
	private boolean verifyDiagonals(GridPoint agentPosition) {
		
		if((agentPosition.getX() + 1) == destination.getX() && (agentPosition.getY() + 1) == destination.getY()
				|| (agentPosition.getX() + 1) == destination.getX() && (agentPosition.getY() - 1) == destination.getY()
				|| (agentPosition.getX() - 1) == destination.getX() && (agentPosition.getY() + 1) == destination.getY()
				|| (agentPosition.getX() - 1) == destination.getX() && (agentPosition.getY() - 1) == destination.getY()
				|| agentPosition.getX() == destination.getX() && agentPosition.getY() == destination.getY()) {
			return true;
		}
		
		return false;
	}

}
