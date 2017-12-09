package parking_lots_simulation;

import repast.simphony.engine.environment.RunEnvironment;
import parking_lots_simulation.GodAgent;

public class Statistics {

	String[] weekDays = {"Monday", "Tuesday" , "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	private int explorerDrivers = 0;
	private int guidedDrivers = 0;

	
	public Statistics() {
		
	}
	
	public void printStatistics(double currentTick) {
		System.out.println("-------------------------------");
		System.out.println("Day of the week: " + getDay(currentTick));
		System.out.println("Number of Explorer Drives: " + explorerDrivers);
		System.out.println("Number of Guided Drives: " + guidedDrivers);
		System.out.println("-------------------------------");
		RunEnvironment.getInstance().pauseRun();
	}
	
	public void addStatisticToGodAgent(GodAgent god) {
		god.addStatistics(this);
	}
	
	
	public void incrementExplorerDrivers() {
		explorerDrivers++;
	}
	
	public void incrementGuidedDrivers() {
		guidedDrivers++;
	}
	
	public String getDay(double tickCount) {	
		int day = (((int) tickCount / Launcher.TICKS_IN_HOUR) / Launcher.HOURS_PER_DAY ) % 7;
		int weekDay = day==0 ? 7 : day; 
		
		return weekDays[weekDay-1];
	}
	
}
