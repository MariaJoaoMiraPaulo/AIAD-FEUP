package parking_lots_simulation;

import repast.simphony.engine.environment.RunEnvironment;
import parking_lots_simulation.GodAgent;

public class Statistics {

	String[] weekDays = {"Monday", "Tuesday" , "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	private int explorerDrivers = 0;
	private int guidedDrivers = 0;
	double[] MorningOccupation = new double[13];
	double[] AfternoonOccupation = new double[13];
	double[] NightOccupation = new double[13];
	double[] inflationParameter = new double[13];
	double[] price = new double[13];
	double TotalUtility = 0;
	int week = 1;

	public Statistics() {}
	
	public void printStatistics(double currentTick) {
	
		System.out.println("--------------------------------");

		int day = (((int) currentTick / Launcher.TICKS_IN_HOUR) / Launcher.HOURS_PER_DAY ) % 7;
		if(day == 0) {
			week++;
			System.out.println("--------  New Prices  ----------");
			System.out.println("Park      |   1   |   2   |   3   |   4   |   5   |   6   |   7   |   8   |   9   |   10  |   11  |   12  |   13 |");
			System.out.print("Inflation     | ");
			for(int i= 0; i< inflationParameter.length; i++) {
				System.out.print( String.format("%.2f", inflationParameter[i]) + " | " );
			}
			System.out.println("");
			System.out.print("Price per hour| ");
			for(int i= 0; i< price.length; i++) {
				System.out.print( String.format("%.2f", price[i]) + "€ | " );
			}
			System.out.println("");
		}
		
		System.out.println("");
		System.out.println("Week " + week);
		
		System.out.println("Day of the week: " + getDay(currentTick));
		System.out.println("Number of Explorer Drives: " + explorerDrivers);
		System.out.println("Number of Guided Drives: " + guidedDrivers);
		
		System.out.println("");

		System.out.println("Park   |   1   |   2   |   3   |   4   |   5   |   6   |   7   |   8   |   9   |   10  |   11  |   12  |   13 |");
		System.out.print("9:00h  | ");
		for(int i= 0; i< MorningOccupation.length; i++) {
			System.out.print( String.format("%.2f", MorningOccupation[i]) + "% | " );
		}
		
		System.out.println("");
		System.out.print("16:00h | ");
		for(int i= 0; i< AfternoonOccupation.length; i++) {
			System.out.print( String.format("%.2f", AfternoonOccupation[i]) + "% | " );
		}
		
		System.out.println("");
		System.out.print("20:00h | ");
		for(int i= 0; i< NightOccupation.length; i++) {
			System.out.print( String.format("%.2f", NightOccupation[i]) + "% | " );
		}
			
		System.out.println("");
		System.out.println("");
		System.out.println("Drivers Average Utility: " + TotalUtility/(guidedDrivers+explorerDrivers));
		System.out.println("");
		
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
		return weekDays[day];
	}
	
	public void updateMorningData(int park, double occupation) {
		MorningOccupation[park] = occupation * 100;
	}
	
	public void updateAfternoonData(int park, double occupation) {
		AfternoonOccupation[park] = occupation * 100;
	}
	
	public void updateNightData(int park, double occupation) {
		NightOccupation[park] = occupation * 100;
	}
	
	public void updateinflationParameter(int park, double inflation) {
		inflationParameter[park] = inflation;
	}
	
	public void updatePricePerHour(int park, double pricePerHour) {
		price[park] = pricePerHour;
	}
	
	public void sumUtility(double utility) {
		TotalUtility+=utility;
	}

}
