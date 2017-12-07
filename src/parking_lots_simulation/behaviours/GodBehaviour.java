package parking_lots_simulation.behaviours;

import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.SequentialBehaviour;
import sajas.wrapper.ContainerController;

import java.util.Random;
import java.util.Set;

import jade.wrapper.StaleProxyException;
import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.Launcher;
import parking_lots_simulation.ParkingFacilityAgent;
import parking_lots_simulation.debug.ExplorerDriverAgent;
import parking_lots_simulation.debug.GuidedDriverAgent;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class GodBehaviour extends Behaviour {
	private static final long serialVersionUID = 5217224817467263075L;

	public static final Random driverRandomGenerator = new Random(Launcher.driverGenerationSeed);
	private static final double[] weekday = { 75, 75, 75, 80, 150, 215, 280, 275, 215, 200, 175, 180, 180, 170, 160,
			150, 145, 140, 125, 70, 50, 50, 75, 50, 25 };
	private int currentDrivers = 0;

	private ContainerController mainContainer;

	private Grid<Object> mainGrid;

	private Set<ParkingFacilityAgent> parkingFacilities;

	private static int currentDriverId = 0;

	public GodBehaviour(ContainerController mainContainer, Grid<Object> mainGrid,
			Set<ParkingFacilityAgent> parkingFacilities) {
		this.mainContainer = mainContainer;
		this.mainGrid = mainGrid;
		this.parkingFacilities = parkingFacilities;
	}

	@Override
	public void action() {
		int tickCount = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		generateAgents(getDayOfTheWeek(tickCount), getHour(tickCount), getMinute(tickCount));
	}

	/**
	 * 
	 * @param dayOfTheWeek
	 *            0 is monday, 1 is tuesday ... 6 is sunday
	 */
	private void generateAgents(int dayOfTheWeek, int hour, int minute) {
		for (int i = 0; i < (weekday[hour] - currentDrivers) / 2; i++) {
			String id = "ExplorerDriver" + currentDriverId;

			// Randomized from 7.5 to 8.5 hours. TODO: Change this according to day of week
			double durationOfStay = 7.5 + driverRandomGenerator.nextInt(2);

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
		}

		// create guided driver agents
		for (int i = 0; i < weekday[hour] - currentDrivers; i++) {
			String id = "GuidedDriver" + currentDriverId;

			// Randomized from 7.5 to 8.5 hours. TODO: Change this according to day of week
			double durationOfStay = 7.5 + driverRandomGenerator.nextInt();

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
		}
	}

	private int getDayOfTheWeek(int tickCount) {
		return ((int) tickCount / Launcher.TICKS_IN_HOUR) / 24 % 7;
	}

	private int getHour(int tickCount) {
		return ((int) tickCount / Launcher.TICKS_IN_HOUR) % 24;
	}

	private int getMinute(double tickCount) {
		return (int) (60 * tickCount) / Launcher.TICKS_IN_HOUR % 60;
	}

	@Override
	public boolean done() {
		return false;
	}

	private GridPoint generateRandomGridPoint() {
		return new GridPoint(driverRandomGenerator.nextInt(Launcher.GRID_WIDTH_SIZE),
				driverRandomGenerator.nextInt(Launcher.GRID_HEIGHT_SIZE));
	}

	public void deleteDriver(DriverAgent driver) {
		driver.doDelete();
		currentDrivers--;
	}
}