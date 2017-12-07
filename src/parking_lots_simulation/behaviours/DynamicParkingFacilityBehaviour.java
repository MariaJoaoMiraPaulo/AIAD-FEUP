package parking_lots_simulation.behaviours;

import parking_lots_simulation.Launcher;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.space.grid.Grid;
import sajas.core.behaviours.Behaviour;

public class DynamicParkingFacilityBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final double DELTA = 0.3;
	private static final int FIRST_WEEK = 1;
	private Grid<Object> mainGrid;
	private ParkingFacilityAgent parkingFacility;
	private ISchedule currentSchedule;
	private boolean updateWasPositive;
	private double lastWeekRevenue;

	public DynamicParkingFacilityBehaviour(ParkingFacilityAgent parkingFacility, Grid<Object> mainGrid, ISchedule currentSchedule) {
		super();
		this.parkingFacility = parkingFacility;
		this.mainGrid = mainGrid;
		this.currentSchedule = currentSchedule;
		this.updateWasPositive = true;
	}

	@Override
	public void action() {
		double totalTicks = currentSchedule.getTickCount();
		
		if(totalTicks % Launcher.TICKS_IN_WEEK != 0.0) {
			return;
		}
		
		double currentWeek = totalTicks/Launcher.TICKS_IN_WEEK;
		double atualRevenue = parkingFacility.getWeeklyRevenue();
		if(currentWeek == FIRST_WEEK) {
			lastWeekRevenue = atualRevenue;
		}
		
		lastWeekRevenue = atualRevenue;
		parkingFacility.setWeeklyRevenue(0);
		
	}

	@Override
	public boolean done() {
		return false;
	}
	
	public double updateParameter(double atualParameter) {
		
		if(updateWasPositive) {
			return atualParameter + DELTA * atualParameter;
		}
		
		return atualParameter - DELTA * atualParameter;
	}
}
