package parking_lots_simulation.behaviours;

import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.engine.schedule.ISchedule;

public class DynamicParkingFacilityBehaviour extends ParkRevenueBehaviour {

	public enum Parameter {
		PRICE_PER_HOUR, MAX_PRICE_PER_DAY
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8556683381412545535L;
	private static final double DELTA = 0.3;
	private static final int FIRST_WEEK = 1;
	private static final int NUMBER_OF_CONSECUTIVE_UPDATES = 5;
	private static final Parameter[] ARRAY_OF_PARAMETERS = {Parameter.PRICE_PER_HOUR, Parameter.MAX_PRICE_PER_DAY};
	private boolean updateWasPositive;
	private double lastWeekRevenue;
	private double gamma;
	private int consecutiveUpdates;
	private int currentParameterIndex;

	public DynamicParkingFacilityBehaviour(ParkingFacilityAgent parkingFacility,
			ISchedule currentSchedule) {
		super(parkingFacility, currentSchedule);

		this.updateWasPositive = true;
		this.gamma = 1;
		this.consecutiveUpdates = 0;
		this.currentParameterIndex = 0;
	}

	@Override
	public void updateValues(double currentWeek, double currentRevenue) {

		if(currentWeek == FIRST_WEEK) {
			lastWeekRevenue = currentRevenue;
		}
		
		System.out.println("Semana nº " + currentWeek);
		
		gamma = (currentRevenue == 0) ? 0 : currentRevenue / lastWeekRevenue;
		
		// If the parameter has been updated enough times, we need to pass to the next one and reset all variables
		if(consecutiveUpdates == NUMBER_OF_CONSECUTIVE_UPDATES) {
			consecutiveUpdates = 0;
			updateWasPositive = true;
			currentParameterIndex++;
		}

		double newValue = 0, oldValue = 0;
		switch(ARRAY_OF_PARAMETERS[currentParameterIndex % ARRAY_OF_PARAMETERS.length]) {
			case PRICE_PER_HOUR:
				System.out.println("Atualizando o preço por hora");
				oldValue = parkingFacility.getPricePerHour();
				newValue = parkingFacility.setPricePerHour(newParameterValue(oldValue));
				break;
			case MAX_PRICE_PER_DAY:
				System.out.println("Atualizando o max preço por dia");
				oldValue = parkingFacility.getMaxPricePerDay();
				newValue = parkingFacility.setMaxPricePerDay(newParameterValue(oldValue));
				break;
			default: 
				break;
		}
		System.out.println("Old price: " + oldValue);
		System.out.println("New price: " + newValue);
		// Update number of consecutive updates of a certain parameter
		consecutiveUpdates++;
		
		// Verifying if the update has increased the parameter value or not
		updateWasPositive = (newValue >= oldValue) ? true : false;

		lastWeekRevenue = currentRevenue;
	}

	public double newParameterValue(double currentParameter) {
		return (updateWasPositive) ? currentParameter + DELTA * currentParameter * (gamma - 1) :
			currentParameter - DELTA * currentParameter * (gamma - 1);
	}

}
