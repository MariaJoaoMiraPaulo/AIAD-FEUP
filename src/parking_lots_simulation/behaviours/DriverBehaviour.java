package parking_lots_simulation.behaviours;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.print.attribute.standard.Destination;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.NoPositiveUtilityParkingFoundException;
import parking_lots_simulation.ParkingFacilityAgent;
import parking_lots_simulation.RepastSServiceConsumerProviderLauncher;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
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
	private boolean done = false;
	private Set<GridPoint> parkingFacilitiesToAvoid;

	public DriverBehaviour(DriverAgent driver, Grid<Object> mainGrid, Set<ParkingFacilityAgent> parkingFacilities) {
		this.driverAgent = driver;
		this.mainGrid = mainGrid;
		this.parkingFacilities = parkingFacilities;
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

		GridPoint agentPosition = mainGrid.getLocation(driverAgent);
		
		if(isAgentOnParkEntrance(mainGrid.getDistance(agentPosition, parkingDestination))) {
			ParkingFacilityAgent parkingFacility = (ParkingFacilityAgent)mainGrid.getObjectAt(parkingDestination.getX(), parkingDestination.getY());
			
			if(parkingFacility.isFull()) {
				//parkingFacilities.add(parkingFacility);
				//destination = null;
			} else {
				mainGrid.moveTo(driverAgent, parkingDestination.getX(), parkingDestination.getY());
				parkingFacility.parkCar(driverAgent);
				// TODO random time

				driverAgent.addBehaviour(new SleepBehaviour(driverAgent, parkingFacility, 5));
				done = true;
			}
		} else { 
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
		
		double u = 0.9;
		double v = 0.9;
		
		double maxUtility = driverAgent.getPaymentEmphasis()*Math.pow(RepastSServiceConsumerProviderLauncher.MAX_PAYMENT, u) - driverAgent.getWalkDistanceEmphasis() * Math.pow(RepastSServiceConsumerProviderLauncher.MAX_EFFORT, v);
		
		double utility = 0 + (double)(Math.random() * maxUtility); 
		double beta = 0 + (double)(Math.random() * 1); 
		double alpha = 0 + (double)(Math.random() * 1); 
	
		double payment = alpha * price * driverAgent.getDuration_of_stay();
		double effort = beta * distance_to_destination;
		
		return utility - driverAgent.getPaymentEmphasis()*Math.pow(payment, u) - driverAgent.getWalkDistanceEmphasis() * Math.pow(effort, v);
	}

	/**
	 * If the driver agent is at sqrt(2) distance of a park facility
	 * @param distance distance between park and driver agent
	 * @return true if is at sqrt(2) or less, false otherwise
	 */
	private boolean isAgentOnParkEntrance(double distance) {
		
		if(distance <= Math.sqrt(2)) {
			return true;
		}
		
		return false;
	}
}
