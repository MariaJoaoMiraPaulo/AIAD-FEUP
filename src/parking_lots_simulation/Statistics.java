package parking_lots_simulation;

import repast.simphony.engine.environment.RunEnvironment;
import parking_lots_simulation.GodAgent;

public class Statistics {

	String[] weekDays = {"Monday", "Tuesday" , "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	private int explorerDrivers = 0;
	private int guidedDrivers = 0;
	double[] MorningOccupation = new double[13];
	double[] MorningPrices = new double[13];
	double[] AfternoonOccupation = new double[13];
	double[] AfternoonPrices = new double[13];
	double[] NightOccupation = new double[13];
	double[] NightPrices = new double[13];
	double TotalUtility = 0;

	public Statistics() {
		
	}
	
	public void printStatistics(double currentTick) {
		System.out.println("-------------------------------");
		System.out.println("Day of the week: " + getDay(currentTick));
		System.out.println("Number of Explorer Drives: " + explorerDrivers);
		System.out.println("Number of Guided Drives: " + guidedDrivers);
		System.out.println("Morning Occupation Percentage");
		
		for(int i= 0; i< MorningOccupation.length; i++) {
			System.out.println("Park " + i + " at 9:00 " + String.format("%.2f", MorningOccupation[i]) + "%" + " Price per hour : " + MorningPrices[i]);
		}
		System.out.println("Afternoon Occupation Percentage");
		for(int i= 0; i< AfternoonOccupation.length; i++) {
			System.out.println("Park " + i + " at 16:00 " + String.format("%.2f", AfternoonOccupation[i]) + "%" + " Price per hour : " + AfternoonPrices[i]);
		}
		System.out.println("Night Occupation Percentage");
		for(int i= 0; i< NightOccupation.length; i++) {
			System.out.println("Park " + i + " at 10:00 " + String.format("%.2f", NightOccupation[i]) + "%" + " Price per hour : " + NightPrices[i]);
		}
			
		System.out.println("Drivers Average Utility: " + TotalUtility/(guidedDrivers+explorerDrivers));
		
		System.out.println("-------------------------------");
		
		guidedDrivers = 0;
		explorerDrivers = 0;
		TotalUtility = 0;
		
		//Pauses Simulation 
		//RunEnvironment.getInstance().pauseRun();
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
	
	public void updateMorningData(int park, double occupation, double price) {
		MorningOccupation[park] = occupation * 100;
		MorningPrices[park] = price;
	}
	
	public void updateAfternoonData(int park, double occupation, double price) {
		AfternoonOccupation[park] = occupation * 100;
		AfternoonPrices[park] = price;
	}
	
	public void updateNightData(int park, double occupation, double price) {
		NightOccupation[park] = occupation * 100;
		NightPrices[park] = price;
	}
	
	public void sumUtility(double utility) {
		TotalUtility+=utility;
	}

}
