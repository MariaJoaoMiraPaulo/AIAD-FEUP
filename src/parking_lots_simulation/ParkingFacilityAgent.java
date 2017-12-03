package parking_lots_simulation;

import sajas.core.Agent;

public class ParkingFacilityAgent extends Agent {

	private int availableParkingSpaces;
	private int capacity;
	
	public ParkingFacilityAgent(int capacity) {
		this.capacity = capacity;
		this.availableParkingSpaces = capacity;
	}
	
	public int getAvailableParkingSpaces() {
		return availableParkingSpaces;
	}
	
	public void setAvailableParkingSpaces(int availableParkingSpaces) {	
		this.availableParkingSpaces = availableParkingSpaces;
	}

	public boolean hasAvailableSpace() {
		return this.availableParkingSpaces > 0;
	}
}
