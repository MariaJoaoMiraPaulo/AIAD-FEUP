package parking_lots_simulation.debug;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.GridPoint;

public class StaticParkingFacilityAgent extends ParkingFacilityAgent {

	public StaticParkingFacilityAgent(String name, GridPoint location, int capacity, double pricePerHour, double maxPricePerDay) {
		super(name, location, capacity, pricePerHour, maxPricePerDay);
	}
	
	/**
	 * Computes the price
	 * @param driverAgent
	 * @return price to pay
	 */
	public double computePrice(DriverAgent driverAgent) {
		// Gets the price to pay
		double priceToPay = driverAgent.getDurationOfStay() * pricePerHour;
		
		if(priceToPay > maxPricePerDay) {
			priceToPay = maxPricePerDay;
		}
		
		return priceToPay;
	}
	
}
