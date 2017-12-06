package parking_lots_simulation;

import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;

public class DriverAgent extends Agent {
	public static final double alpha = 1;
	public static final double beta = 1;

	private GridPoint destination;
	private String id;
	private int durationOfStay;

	/*
	 * Emphasis of agent on paying a certain amount of money
	 */
	private double paymentEmphasis;

	/*
	 * Emphasis of agent on walking a certain distance
	 */
	private double walkDistanceEmphasis;

	/*
	 * Utility the driver gets for arriving at its destination
	 */
	private double utilityForArrivingAtDestination;

	public DriverAgent(String id, GridPoint destination, int durationOfStay) {
		this.id = id;
		this.durationOfStay = durationOfStay;
		this.destination = destination;

		paymentEmphasis = 1 + Math.random() * (1.5 - 1);
		walkDistanceEmphasis = 1 + Math.random() * (1.5 - 1);
		utilityForArrivingAtDestination = Math.random() * 10000;
	}

	public DriverAgent(String id) {
		this.id = id;
	}

	@Override
	public void addBehaviour(Behaviour b) {
		super.addBehaviour(b);
	}

	@Override
	protected void setup() {
		super.setup();
	}

	public String getId() {
		return id;
	}

	public GridPoint getDestination() {
		return destination;
	}

	public int getDurationOfStay() {
		return durationOfStay;
	}

	public double getPaymentEmphasis() {
		return paymentEmphasis;
	}

	public double getWalkDistanceEmphasis() {
		return walkDistanceEmphasis;
	}

	@Override
	protected void takeDown() {
		super.takeDown();
	}

	public double getUtilityForArrivingAtDestination() {
		return utilityForArrivingAtDestination;
	}
}
