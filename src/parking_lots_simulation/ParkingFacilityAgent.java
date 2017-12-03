package parking_lots_simulation;

import java.util.HashMap;
import java.util.Map;
import sajas.core.Agent;

public class ParkingFacilityAgent extends Agent {

	private Map<String, DriverAgent> parkedCars = new HashMap<>();
	private int capacity;
	
	public ParkingFacilityAgent(int capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * @return the number of available parking spaces.
	 */
	public int getAvailableParkingSpaces() {
		return capacity - parkedCars.size();
	}
	
	/**
	 * Parks car in parking facility. Does not check if the park is full. See {@link #isFull()}
	 * @param driverAgent Driver to park
	 */
	public void parkCar(DriverAgent driverAgent) {
		parkedCars.put(driverAgent.getId(), driverAgent);
	}
	
	/**
	 * Removes a car from the parking facility. 
	 */
	public void removeCar(DriverAgent driverAgent) {
		parkedCars.remove(driverAgent.getId());
	}
	
	/**
	 * @return whether the parking facility is full or not.
	 */
	public boolean isFull() {
		return parkedCars.size() >= capacity;
	}
}
