package parking_lots_simulation;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;

public abstract class ParkingFacilityAgent extends Agent {
	
	private static final double MIN_INFLATION_RATE = 0.25;

	protected Map<String, DriverAgent> parkedCars = new HashMap<>();
	protected int capacity;
	protected double pricePerHour;
	protected double minPricePerHour;
	protected double maxPricePerDay;
	protected double minMaxPricePerDay;
	protected GridPoint location;
	protected double weeklyRevenue;
	protected String name;
	protected double inflationParameter;

	public ParkingFacilityAgent(String name, GridPoint location, int capacity, double pricePerHour, double maxPricePerDay) {
		this.location = location;
		this.capacity = capacity;
		this.pricePerHour = pricePerHour;
		this.minPricePerHour = this.pricePerHour/2;
		this.maxPricePerDay = maxPricePerDay;
		this.minMaxPricePerDay = this.maxPricePerDay/2;
		this.weeklyRevenue = 0;
		this.name = name;
		this.inflationParameter = 1;
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
		
		// Compute the price
		weeklyRevenue += computePrice(driverAgent);
	}
	
	/**
	 * @return the occupancy percentage of the park
	 */
	public double getOccupancyPercentage() {
		return (double)parkedCars.size() / (double)capacity;
	}
	
	/**
	 * Computes the price
	 * @param driverAgent
	 * @return price to pay
	 */
	public abstract double computePrice(DriverAgent driverAgent);
	
	/**
	 * Gets the price to pay in function of inflation. For example for a inflation rate of 1.10 and a price
	 * per hour of 1, you get the following prices:
	 * Hour | Price Per Hour  | Total
	 *   1  |       1		  |   1
	 *   2	|		1.1		  |   2.1
	 *   3	|		1.21      |   3.31
	 *   4	|		1.331     |   4.641
	 * @param durationOfStay
	 * @param currentPricePerHour
	 * @return total to pay
	 */
	public double calculatePrice(double durationOfStay, double currentPricePerHour) {
		return (durationOfStay <= 1) ? currentPricePerHour * durationOfStay 
				: calculatePrice(--durationOfStay, currentPricePerHour + (inflationParameter - 1)*currentPricePerHour) + currentPricePerHour;
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
	 * Changes price per hour if isn't below minPricePerHour
	 * @param pricePerHour
	 * @return new value
	 */
	public double setPricePerHour(double pricePerHour) {
		this.pricePerHour = (pricePerHour > minPricePerHour) ? pricePerHour : minPricePerHour; 
		return this.pricePerHour;
	}

	/**
	 * @return the max price per day
	 */
	public double getMaxPricePerDay() {
		return maxPricePerDay;
	}
	
	/**
	 * Changes max price per day if isn't below minMaxPricePerDay
	 * @param maxPricePerDay
	 * @return new value
	 */
	public double setMaxPricePerDay(double maxPricePerDay) {
		this.maxPricePerDay = (maxPricePerDay > minMaxPricePerDay) ? maxPricePerDay : minMaxPricePerDay;
		return this.maxPricePerDay;
	}

	/**
	 * @return the weekly revenue
	 */
	public double getWeeklyRevenue() {
		return weeklyRevenue;
	}

	/**
	 * Used to reset weekly revenue, on the beginning of the week
	 * @param weeklyRevenue new weekly revenue
	 */
	public void resetWeeklyRevenue() {
		this.weeklyRevenue = 0;
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

	/**
	 * @return inflation rate
	 */
	public double getInflationParameter() {
		return inflationParameter;
	}

	/**
	 * Changes inflation rate if isn't below min inflation rate
	 * @param inflationParameter
	 * @return new inflation rate
	 */
	public double setInflationParameter(double inflationParameter) {
		this.inflationParameter = (inflationParameter > MIN_INFLATION_RATE) ? inflationParameter : MIN_INFLATION_RATE;
		return this.inflationParameter;
	}

}
