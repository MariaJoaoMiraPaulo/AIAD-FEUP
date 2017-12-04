package parking_lots_simulation;

import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;

public class DriverAgent extends Agent {

	private GridPoint destination;
	private String id;
	private int durationOfStay;
	private double paymentEmphasis;
	private double walkDistanceEmphasis;

	public DriverAgent(String id, GridPoint destination, int duration_of_stay ) {
		this.id = id;
		this.durationOfStay = duration_of_stay;
		this.paymentEmphasis = 1 + (double)(Math.random() * 1.5); //emphasis of agent i on paying a certain amount of money 
		this.walkDistanceEmphasis = 1 + (double)(Math.random() * 1.5); //emphasis of agent i on  walking a certain distance
		this.destination = destination;
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
	
	public int getDuration_of_stay() {
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
		
		System.out.println("Adeus!!");
	}
}
