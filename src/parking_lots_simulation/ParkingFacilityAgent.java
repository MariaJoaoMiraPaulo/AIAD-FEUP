package parking_lots_simulation;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;

public class ParkingFacilityAgent extends Agent {

	private Map<String, DriverAgent> parkedCars = new HashMap<>();
	private int capacity;
	private double price;
	private double maxPrice;
	private GridPoint location;
	private String name;
	
	public ParkingFacilityAgent(String name, GridPoint location, int capacity, double price, double MaxPrice) {
		this.location = location;
		this.capacity = capacity;
		this.price = price;
		this.name = name;
		this.maxPrice = MaxPrice;
	}

	/**
	 * @return the number of available parking spaces.
	 */
	public int getAvailableParkingSpaces() {
		return capacity - parkedCars.size();
	}

	/**
	 * Parks car in parking facility. Does not check if the park is full. See
	 * {@link #isFull()}
	 * 
	 * @param driverAgent
	 *            Driver to park
	 */
	public void parkCar(DriverAgent driverAgent) {
		parkedCars.put(driverAgent.getId(), driverAgent);
		driverAgent.setCurrentParkingFacility(this);
	}

	/**
	 * Removes a car from the parking facility.
	 */
	public void removeCar(DriverAgent driverAgent) {
		parkedCars.remove(driverAgent.getId());
		driverAgent.setCurrentParkingFacility(null);
	}

	/**
	 * @return whether the parking facility is full or not.
	 */
	public boolean isFull() {
		return parkedCars.size() >= capacity;
	}

	/**
	 * @return the price per hour
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * @return the maximum price per day
	 */
	public double getMaxPrice() {
		return maxPrice;
	}
	
	/**
	 * @return parking name
	 */
	public String getParkFacilityName() {
		return name;
	}
	
	/**
	 * @return location
	 */
	public GridPoint getLocation() {
		return location;
	}

}
