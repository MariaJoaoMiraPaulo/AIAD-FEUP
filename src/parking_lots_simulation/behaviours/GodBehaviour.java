package parking_lots_simulation.behaviours;

import java.util.Random;
import java.util.Set;

import cern.jet.random.Normal;
import jade.wrapper.StaleProxyException;
import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.GodAgent;
import parking_lots_simulation.Launcher;
import parking_lots_simulation.ParkingFacilityAgent;
import parking_lots_simulation.debug.ExplorerDriverAgent;
import parking_lots_simulation.debug.GuidedDriverAgent;
import parking_lots_simulation.population.PopulationCalculator;
import parking_lots_simulation.population.WeekdayPopulationCalculator;
import parking_lots_simulation.population.WeekendPopulationCalculator;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.SequentialBehaviour;
import sajas.wrapper.ContainerController;

public class GodBehaviour extends Behaviour {
	private static final long serialVersionUID = 5217224817467263075L;

	public static final Random driverRandomGenerator = new Random(Launcher.driverGenerationSeed);

	private static final WeekdayPopulationCalculator weekdayCalc = new WeekdayPopulationCalculator();
	private static final WeekendPopulationCalculator weekendCalc = new WeekendPopulationCalculator();
	private static final PopulationCalculator[] week = { weekdayCalc, weekdayCalc, weekdayCalc, weekdayCalc,
			weekdayCalc, weekendCalc, weekendCalc };

	private int currentDrivers = 0;

	private ContainerController mainContainer;

	private Grid<Object> mainGrid;

	private Set<ParkingFacilityAgent> parkingFacilities;

	private static int currentDriverId = 0;

	private GodAgent god;

	private Normal normalDistribution;

	public GodBehaviour(GodAgent god, ContainerController mainContainer, Grid<Object> mainGrid,
			Set<ParkingFacilityAgent> parkingFacilities) {
		this.mainContainer = mainContainer;
		this.mainGrid = mainGrid;
		this.parkingFacilities = parkingFacilities;
		this.god = god;
		RandomHelper.setSeed(Launcher.driverGenerationSeed);
		normalDistribution = RandomHelper.createNormal(8, 2.5);
	}

	@Override
	public void action() {
		int tickCount = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		generateAgents(getDayOfTheWeek(tickCount), getHour(tickCount));

		if (getHour(tickCount) == 9.0 || getHour(tickCount) == 16.00 || getHour(tickCount) == 20.00)
			updateOccupancyStatistics(getHour(tickCount));

		if (getDayOfTheWeek(tickCount) == 0)
			updatePriceStatistics();

	}

	/**
	 * 
	 * @param dayOfTheWeek
	 *            0 is monday, 1 is tuesday ... 6 is sunday
	 */
	private void generateAgents(int dayOfTheWeek, double hour) {
		for (int i = 0; i < (week[dayOfTheWeek].calculate(hour) - currentDrivers) / 2; i++) {
			String id = "ExplorerDriver" + currentDriverId;

			// Randomized from 7.5 to 8.5 hours. TODO: Change this according to day of week
			double durationOfStay = getRandomDurationOfStay();
			System.out.println(currentDriverId + ": " + durationOfStay);

			GridPoint destination = generateRandomGridPoint();
			DriverAgent explorerDriver = new ExplorerDriverAgent(id, destination, durationOfStay);
			SequentialBehaviour driverBehaviour = new SequentialBehaviour();
			driverBehaviour.addSubBehaviour(new ExplorerDriverBehaviour(explorerDriver, mainGrid, parkingFacilities));

			driverBehaviour
					.addSubBehaviour(new SleepBehaviour(explorerDriver, (int) durationOfStay * Launcher.TICKS_IN_HOUR));
			explorerDriver.addBehaviour(driverBehaviour);

			try {
				mainContainer.acceptNewAgent(id, explorerDriver).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}

			GridPoint start = generateRandomGridPoint();
			mainGrid.moveTo(explorerDriver, start.getX(), start.getY());
			currentDrivers++;
			currentDriverId++;
			god.getStatistics().incrementExplorerDrivers();
		}

		// create guided driver agents
		for (int i = 0; i < week[dayOfTheWeek].calculate(hour) - currentDrivers; i++) {
			String id = "GuidedDriver" + currentDriverId;

			// Randomized from 7.5 to 8.5 hours.
			double durationOfStay = getRandomDurationOfStay();
			System.out.println(currentDriverId + ": " + durationOfStay);

			GridPoint destination = generateRandomGridPoint();
			DriverAgent guidedDriver = new GuidedDriverAgent(id, destination, durationOfStay);

			SequentialBehaviour driverBehaviour = new SequentialBehaviour();
			driverBehaviour.addSubBehaviour(new GuidedDriverBehaviour(guidedDriver, mainGrid, parkingFacilities));
			driverBehaviour
					.addSubBehaviour(new SleepBehaviour(guidedDriver, (int) durationOfStay * Launcher.TICKS_IN_HOUR));
			guidedDriver.addBehaviour(driverBehaviour);

			try {
				mainContainer.acceptNewAgent(id, guidedDriver).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}

			GridPoint start = generateRandomGridPoint();
			mainGrid.moveTo(guidedDriver, start.getX(), start.getY());
			currentDrivers++;
			currentDriverId++;
			god.getStatistics().incrementGuidedDrivers();
		}
	}

	private double getRandomDurationOfStay() {
		double ret;
		do {
			ret = normalDistribution.nextDouble();
		} while (ret <= 0);

		return ret;
	}

	private int getDayOfTheWeek(int tickCount) {
		return ((int) tickCount / Launcher.TICKS_IN_HOUR) / 24 % 7;
	}

	private double getHour(int tickCount) {
		return (tickCount / Launcher.TICKS_IN_HOUR) % 24;
	}

	@Override
	public boolean done() {
		return false;
	}

	private GridPoint generateRandomGridPoint() {
		return new GridPoint(driverRandomGenerator.nextInt(Launcher.GRID_WIDTH_SIZE),
				driverRandomGenerator.nextInt(Launcher.GRID_HEIGHT_SIZE));
	}

	public void onDriverDelete(DriverAgent driver, double utility) {
		currentDrivers--;
		god.getStatistics().sumUtility(utility);
	}

	public void updateOccupancyStatistics(double hour) {
		int parkIndex = 0;

		for (ParkingFacilityAgent park : parkingFacilities) {

			if (hour == 9.00)
				god.getStatistics().updateMorningData(parkIndex, park.getOccupancyPercentage());
			if (hour == 16.00)
				god.getStatistics().updateAfternoonData(parkIndex, park.getOccupancyPercentage());
			if (hour == 20.00)
				god.getStatistics().updateNightData(parkIndex, park.getOccupancyPercentage());

			parkIndex++;
		}
	}

	public void updatePriceStatistics() {
		int parkIndex = 0;
		for (ParkingFacilityAgent park : parkingFacilities) {
			god.getStatistics().updateinflationParameter(parkIndex, park.getInflationParameter());
			god.getStatistics().updatePricePerHour(parkIndex, park.getPricePerHour());
			parkIndex++;
		}
	}
}