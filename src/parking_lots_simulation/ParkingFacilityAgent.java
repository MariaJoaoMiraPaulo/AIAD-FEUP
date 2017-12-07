package parking_lots_simulation;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;

public class ParkingFacilityAgent extends Agent {

	private Map<String, DriverAgent> parkedCars = new HashMap<>();
	private int capacity;
	private double pricePerHour;
	private double maxPricePerDay;
	private GridPoint location;
	private double weeklyRevenue;

	public GridPoint getLocation() {
		return location;
	}

	public ParkingFacilityAgent(GridPoint location, int capacity, double price, double maxPricePerDay) {
		this.location = location;
		this.capacity = capacity;
		this.pricePerHour = price;
		this.maxPricePerDay = maxPricePerDay;
		this.weeklyRevenue = 0;
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
		weeklyRevenue += driverAgent.getDurationOfStay() * pricePerHour;
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
	public double getPricePerHour() {
		return pricePerHour;
	}
	
	/**
	 * @return the max price per day
	 */
	public double getMaxPricePerDay() {
		return maxPricePerDay;
	}

	/**
	 * @return the weekly revenue
	 */
	public double getWeeklyRevenue() {
		return weeklyRevenue;
	}

	/**
	 * Used to reset weekly revenue, on the begging of the week
	 * @param weeklyRevenue new weekly revenue
	 */
	public void setWeeklyRevenue(double weeklyRevenue) {
		this.weeklyRevenue = weeklyRevenue;
	}
}
