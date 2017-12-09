package parking_lots_simulation.behaviours;

import parking_lots_simulation.Launcher;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.engine.schedule.ISchedule;
import sajas.core.behaviours.Behaviour;

public abstract class ParkRevenueBehaviour extends Behaviour {

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;
	protected ParkingFacilityAgent parkingFacility;
	protected ISchedule currentSchedule;

	public ParkRevenueBehaviour(ParkingFacilityAgent parkingFacility, ISchedule currentSchedule) {
		super();
		this.parkingFacility = parkingFacility;
		this.currentSchedule = currentSchedule;
	}

	@Override
	public void action() {
		int totalTicks = (int) currentSchedule.getTickCount();

		// Only update at the start of the week
		if (totalTicks % Launcher.TICKS_IN_WEEK != 0) {
			return;
		}

		updateValues(totalTicks / Launcher.TICKS_IN_WEEK, parkingFacility.getWeeklyRevenue());

		parkingFacility.resetWeeklyRevenue();
	}

	@Override
	public boolean done() {
		return false;
	}

	public abstract void updateValues(int currentWeek, double currentRevenue);
}
