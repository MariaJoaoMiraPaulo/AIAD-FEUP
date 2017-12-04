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
	private GridPoint parkingDestination;
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
		if(parkingDestination == null) {
			try {
				parkingDestination = getMostUsefulDestination(parkingFacilitiesToAvoid);	
			} catch (NoPositiveUtilityParkingFoundException e) {
				// TODO: Exit system
			}
		}
		
		if(neighbourhood.isNeighbor(driverAgent, parkingDestination, 1, 1)) {
			ParkingFacilityAgent parkingFacility = new GridCell<>(parkingDestination, ParkingFacilityAgent.class).items().iterator().next();

			if(parkingFacility.isFull()) {
				parkingFacilities.add(parkingFacility);
				parkingDestination = null;
			} else {
				parkingFacility.parkCar(driverAgent);
				// TODO random time
				driverAgent.addBehaviour(new SleepBehaviour(5));
				done = true;
			}
		} else { 
			GridPoint agentPosition = mainGrid.getLocation(driverAgent);
			
			int x = directions(parkingDestination.getX(), agentPosition.getX());
			int y = directions(parkingDestination.getY(), agentPosition.getY());
			
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
	
	public double getUtility(double distance_to_destination, double price) {
		
		double utility = 0 + (double)(Math.random() * 1); 
		double beta = 0 + (double)(Math.random() * 1); 
		double alpha = 0 + (double)(Math.random() * 1); 
		double u = 0.9;
		double v = 0.9;
		
		double payment = alpha * price * driverAgent.getDuration_of_stay();
		double effort = beta * distance_to_destination;
		
		return utility - driverAgent.getPaymentEmphasis()*Math.pow(payment, u) - driverAgent.getWalkDistanceEmphasis() * Math.pow(effort, v);
	}

}