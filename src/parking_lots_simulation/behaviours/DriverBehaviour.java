package parking_lots_simulation.behaviours;

import java.util.HashSet;
import java.util.Set;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.NoPositiveUtilityParkingFoundException;
import parking_lots_simulation.ParkingFacilityAgent;
import parking_lots_simulation.RepastSServiceConsumerProviderLauncher;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.TickerBehaviour;

public abstract class DriverBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 4252257290496119984L;
	/**
	 * All parking facilities open
	 */
	protected Set<ParkingFacilityAgent> parkingFacilities;
	
	/**
	 * Grid that represents the simulation space
	 */
	protected Grid<Object> mainGrid;
	
	/**
	 * Driver agent where this behaviour is acting
	 */
	protected DriverAgent driverAgent;
	
	/**
	 * Location of the favorite park
	 */
	private GridPoint parkingDestination;
	
	/**
	 * Parking facilities discarded
	 */
	private Set<GridPoint> parkingFacilitiesToAvoid;

	public DriverBehaviour(DriverAgent driver, int period, Grid<Object> mainGrid, Set<ParkingFacilityAgent> parkingFacilities) {
		super(driver, period);
		this.driverAgent = driver;
		this.mainGrid = mainGrid;
		this.parkingFacilities = parkingFacilities;
		this.parkingFacilitiesToAvoid = new HashSet<>();
	}

	public abstract GridPoint getMostUsefulDestination(Set<GridPoint> parkingFacilitiesToAvoid) throws NoPositiveUtilityParkingFoundException;
	
	@Override
	public void onTick() {
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
				this.stop();
			}
		} else { 
			int x = directions(parkingDestination.getX(), agentPosition.getX());
			int y = directions(parkingDestination.getY(), agentPosition.getY());
			
			mainGrid.moveTo(driverAgent, agentPosition.getX() + x , agentPosition.getY() + y);
		}
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
