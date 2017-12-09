package parking_lots_simulation.debug;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.GridPoint;

public class DynamicParkingFacilityAgent extends ParkingFacilityAgent {

	public DynamicParkingFacilityAgent(String name, GridPoint location, int capacity, double pricePerHour, double maxPricePerDay) {
		super(name, location, capacity, pricePerHour, maxPricePerDay);
	}
	
	/**
	 * Computes the price
	 * @param driverAgent
	 * @return price to pay
	 */
	public double computePrice(DriverAgent driverAgent) {
		// Gets the price to pay
		double priceToPay = (inflationParameter == 1) ? driverAgent.getDurationOfStay() * pricePerHour
				: calculatePrice(driverAgent.getDurationOfStay(), pricePerHour);
		
		if(priceToPay > maxPricePerDay) {
			priceToPay = maxPricePerDay;
		} 
		
		double parkOccupancy = getOccupancyPercentage(), inflation;
		
		// Verifying park occupancy to make inflation/deflation on price
		if(parkOccupancy >= 0.3 && parkOccupancy <= 0.7) {
			return priceToPay;
		}
		else if(parkOccupancy < 0.3) {
			inflation = 1 - (0.3 - parkOccupancy);
		}
		else { // park occupancy > 0.7
			inflation = 1 + (parkOccupancy - 0.7);
		}
		
		priceToPay = inflation * priceToPay;
		
		return priceToPay;
	}
}
