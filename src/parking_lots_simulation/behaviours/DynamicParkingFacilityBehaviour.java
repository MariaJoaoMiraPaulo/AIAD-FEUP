package parking_lots_simulation.behaviours;

import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.engine.schedule.ISchedule;

public class DynamicParkingFacilityBehaviour extends ParkRevenueBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8556683381412545535L;
	private static final double DELTA = 0.3;
	private static final int FIRST_WEEK = 1;

	private boolean updateWasPositive;
	private double lastWeekRevenue;
	private double gamma;

	public DynamicParkingFacilityBehaviour(ParkingFacilityAgent parkingFacility,
			ISchedule currentSchedule) {
		super(parkingFacility, currentSchedule);

		this.updateWasPositive = true;
		this.gamma = 1;
	}

	@Override
	public void updateValues(double currentWeek, double currentRevenue) {
		
		if(currentWeek == FIRST_WEEK) {
			lastWeekRevenue = currentRevenue;
		}
		if(currentRevenue == 0) {
			gamma = 0;
		}
		else gamma = currentRevenue / lastWeekRevenue;
		
		System.out.println("Old price: " + parkingFacility.getPricePerHour());
		updateWasPositive = parkingFacility.updatePricePerHour(updateParameter(parkingFacility.getPricePerHour()));
		System.out.println("New price: " + parkingFacility.getPricePerHour());
		
		lastWeekRevenue = currentRevenue;
	}
	
	public double updateParameter(double currentParameter) {
		return (updateWasPositive) ? currentParameter + DELTA * currentParameter * (gamma - 1) :
			currentParameter - DELTA * currentParameter * (gamma - 1);
	}

}
