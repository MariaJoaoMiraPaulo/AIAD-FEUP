package parking_lots_simulation.behaviours;

import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.engine.schedule.ISchedule;

public class DynamicParkingFacilityBehaviour extends ParkRevenueBehaviour {

	public enum Parameter {
		PRICE_PER_HOUR, MAX_PRICE_PER_DAY, INFLATION_RATE
	}

	private static final long serialVersionUID = -8556683381412545535L;
	private static final double DELTA = 0.3;
	private static final int FIRST_WEEK = 0;
	private static final int NUMBER_OF_CONSECUTIVE_UPDATES = 5;
	private static final Parameter[] PARAMETERS = { Parameter.PRICE_PER_HOUR, Parameter.MAX_PRICE_PER_DAY,
			Parameter.INFLATION_RATE };

	/**
	 * This modifier takes either the value -1 or 1. It is 1 if the last update
	 * increased (or maintained) the parameter value. Otherwise, it is -1.
	 */
	private int calculationModifier;
	private double lastWeekRevenue;
	private double gamma;
	private int consecutiveUpdates;
	private int currentParameterIndex;

	public DynamicParkingFacilityBehaviour(ParkingFacilityAgent parkingFacility, ISchedule currentSchedule) {
		super(parkingFacility, currentSchedule);

		this.calculationModifier = 1;
		this.gamma = 1;
		this.consecutiveUpdates = 0;
		this.currentParameterIndex = 0;
	}

	@Override
	public void updateValues(int currentWeek, double currentRevenue) {

		if (currentWeek == FIRST_WEEK) {
			lastWeekRevenue = currentRevenue;
		}

		gamma = (currentRevenue == 0) ? 0 : currentRevenue / lastWeekRevenue;

		// If the parameter has been updated enough times, we need to pass to the next
		// one and reset all variables
		if (consecutiveUpdates == NUMBER_OF_CONSECUTIVE_UPDATES) {
			consecutiveUpdates = 0;
			calculationModifier = 1;
			currentParameterIndex++;
		}

		double newValue = 0, oldValue = 0;
		switch (PARAMETERS[currentParameterIndex % PARAMETERS.length]) {
		case PRICE_PER_HOUR:
			oldValue = parkingFacility.getPricePerHour();
			newValue = parkingFacility.setPricePerHour(calculateParamater(oldValue));
			break;
		case MAX_PRICE_PER_DAY:
			oldValue = parkingFacility.getMaxPricePerDay();
			newValue = parkingFacility.setMaxPricePerDay(calculateParamater(oldValue));
			break;
		case INFLATION_RATE:
			oldValue = parkingFacility.getInflationParameter();
			newValue = parkingFacility.setInflationParameter(calculateParamater(oldValue));
			break;
		default:
			break;
		}
		// Update number of consecutive updates of a certain parameter
		consecutiveUpdates++;

		// Verifying if the update has increased the parameter value or not
		calculationModifier = (newValue >= oldValue) ? 1 : -1;

		lastWeekRevenue = currentRevenue;
	}

	public double calculateParamater(double currentParameter) {
		return currentParameter + calculationModifier * DELTA * currentParameter * (gamma - 1);
	}
}
