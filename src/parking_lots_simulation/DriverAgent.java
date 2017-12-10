package parking_lots_simulation;

import parking_lots_simulation.behaviours.GodBehaviour;
import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;

public class DriverAgent extends Agent {
	public static final double alpha = 1;
	public static final double beta = 1;

	private GridPoint destination;
	private String id;
	
	/**
	 * Duration of stay in hours.
	 */
	private double durationOfStay;

	/**
	 * The parking facility the driver is currently parked in. Null if the agent is
	 * not parked.
	 */
	private ParkingFacilityAgent currentParkingFacility;

	/**
	 * Emphasis of agent on paying a certain amount of money
	 */
	private double paymentEmphasis;

	/**
	 * Emphasis of agent on walking a certain distance
	 */
	private double walkDistanceEmphasis;

	/**
	 * Utility the driver gets for arriving at its destination
	 */
	private double utilityForArrivingAtDestination;

	/**
	 * The utility of the current park. Only applicable if the car is parked.
	 * Otherwise, it will be the value of not finding a useful park.
	 */
	private double currentUtility = -10000;

	public DriverAgent(String id, GridPoint destination, double durationOfStay) {
		this.id = id;
		this.durationOfStay = durationOfStay;
		this.destination = destination;

		paymentEmphasis = 1 + GodBehaviour.driverRandomGenerator.nextDouble() * (1.5 - 1);
		walkDistanceEmphasis = 1 + GodBehaviour.driverRandomGenerator.nextDouble() * (1.5 - 1);
		utilityForArrivingAtDestination = 5000 + GodBehaviour.driverRandomGenerator.nextDouble() * (10000 - 5000);
	}

	@Override
	public void doDelete() {
		Launcher.god.deleteDriver(this, currentUtility);
		super.doDelete();
	}

	public DriverAgent(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public GridPoint getDestination() {
		return destination;
	}

	public double getDurationOfStay() {
		return durationOfStay;
	}

	public double getPaymentEmphasis() {
		return paymentEmphasis;
	}

	public double getWalkDistanceEmphasis() {
		return walkDistanceEmphasis;
	}

	public ParkingFacilityAgent getCurrentParkingFacility() {
		return currentParkingFacility;
	}

	public void setCurrentParkingFacility(ParkingFacilityAgent currentParkingFacility) {
		this.currentParkingFacility = currentParkingFacility;
	}

	public boolean isParked() {
		return currentParkingFacility != null;
	}

	/**
	 * Must only be used when the car is already parked.
	 * 
	 * @param destinationUtility
	 */
	public void setUtility(double destinationUtility) {
		currentUtility = destinationUtility;
	}

	public double getUtilityForArrivingAtDestination() {
		return utilityForArrivingAtDestination;
	}
}
