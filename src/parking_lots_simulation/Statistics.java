package parking_lots_simulation;

public class Statistics {

	String[] weekDays = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	private int explorerDrivers = 0;
	private int guidedDrivers = 0;
	double[] morningOccupation = new double[13];
	double[] afternoonOccupation = new double[13];
	double[] nightOccupation = new double[13];
	double[] inflationParameter = new double[13];
	double[] price = new double[13];
	double totalUtility = 0;
	int week = 1;

	public Statistics() {
	}

	public void printStatistics(double currentTick) {

		System.out.println("--------------------------------");

		int day = (((int) currentTick / Launcher.TICKS_IN_HOUR) / Launcher.HOURS_PER_DAY) % 7;
		if (day == 0) {
			week++;
			System.out.println("--------  New Prices  ----------");
			System.out.println(
					"Park      |   1   |   2   |   3   |   4   |   5   |   6   |   7   |   8   |   9   |   10  |   11  |   12  |   13 |");
			System.out.print("Inflation     | ");
			for (int i = 0; i < inflationParameter.length; i++) {
				System.out.print(String.format("%.2f", inflationParameter[i]) + " | ");
			}
			System.out.println("");
			System.out.print("Price per hour| ");
			for (int i = 0; i < price.length; i++) {
				System.out.print(String.format("%.2f", price[i]) + "€ | ");
			}
			System.out.println("");
		}

		System.out.println("");
		System.out.println("Week " + week);

		System.out.println("Day of the week: " + getDay(currentTick));
		System.out.println("Number of Explorer Drives: " + explorerDrivers);
		System.out.println("Number of Guided Drives: " + guidedDrivers);

		System.out.println("");

		System.out.println(
				"Park   |   1   |   2   |   3   |   4   |   5   |   6   |   7   |   8   |   9   |   10  |   11  |   12  |   13 |");
		System.out.print("9:00h  | ");
		for (int i = 0; i < morningOccupation.length; i++) {
			System.out.print(String.format("%.2f", morningOccupation[i]) + "% | ");
		}

		System.out.println("");
		System.out.print("16:00h | ");
		for (int i = 0; i < afternoonOccupation.length; i++) {
			System.out.print(String.format("%.2f", afternoonOccupation[i]) + "% | ");
		}

		System.out.println("");
		System.out.print("20:00h | ");
		for (int i = 0; i < nightOccupation.length; i++) {
			System.out.print(String.format("%.2f", nightOccupation[i]) + "% | ");
		}

		System.out.println("");
		System.out.println("");
		System.out.println("Drivers Average Utility: " + totalUtility / (guidedDrivers + explorerDrivers));
		System.out.println("");

		guidedDrivers = 0;
		explorerDrivers = 0;
		totalUtility = 0;
	}

	public void incrementExplorerDrivers() {
		explorerDrivers++;
	}

	public void incrementGuidedDrivers() {
		guidedDrivers++;
	}

	public String getDay(double tickCount) {
		int day = (((int) tickCount / Launcher.TICKS_IN_HOUR) / Launcher.HOURS_PER_DAY) % 7;
		return weekDays[day];
	}

	public void updateMorningData(int park, double occupation) {
		morningOccupation[park] = occupation * 100;
	}

	public void updateAfternoonData(int park, double occupation) {
		afternoonOccupation[park] = occupation * 100;
	}

	public void updateNightData(int park, double occupation) {
		nightOccupation[park] = occupation * 100;
	}

	public void updateinflationParameter(int park, double inflation) {
		inflationParameter[park] = inflation;
	}

	public void updatePricePerHour(int park, double pricePerHour) {
		price[park] = pricePerHour;
	}

	public void sumUtility(double utility) {
		totalUtility += utility;
	}

}
