package parking_lots_simulation.behaviours;

import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.engine.schedule.ISchedule;

public class StaticParkingFacilityBehaviour extends ParkRevenueBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4140441100631042778L;

	public StaticParkingFacilityBehaviour(ParkingFacilityAgent parkingFacility, ISchedule currentSchedule) {
		super(parkingFacility, currentSchedule);
	}

	@Override
	public void updateValues(int currentWeek, double currentRevenue) {
		return;
	}

}