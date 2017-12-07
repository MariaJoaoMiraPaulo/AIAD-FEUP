package parking_lots_simulation;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import parking_lots_simulation.behaviours.DynamicParkingFacilityBehaviour;
import parking_lots_simulation.behaviours.ExplorerDriverBehaviour;
import parking_lots_simulation.behaviours.GuidedDriverBehaviour;
import parking_lots_simulation.behaviours.SleepBehaviour;
import parking_lots_simulation.behaviours.StaticParkingFacilityBehaviour;
import parking_lots_simulation.debug.DynamicParkingFacilityAgent;
import parking_lots_simulation.debug.ExplorerDriverAgent;
import parking_lots_simulation.debug.GuidedDriverAgent;
import parking_lots_simulation.debug.StaticParkingFacilityAgent;
import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import sajas.core.Runtime;
import sajas.core.behaviours.SequentialBehaviour;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class Launcher extends RepastSLauncher {

	public static Logger logger = Logger.getGlobal();
	public int staticParkingFacilityCount = 5;
	public int dynamicParkingFacilityCount = 5;
	public int explorerDriverCount = 10;
	public int guidedDriverCount = 10;
	private int driverGenerationSeed;
	private ISchedule currentSchedule;
	public static final int TICKS_IN_HOUR = 10000;
	public static final int NUMBER_OF_DAYS_PER_WEEK = 7;
	public static final int NUMBER_OF_HOURS_PER_DAY = 24;
	public static final int TICKS_IN_WEEK = TICKS_IN_HOUR * NUMBER_OF_DAYS_PER_WEEK * NUMBER_OF_HOURS_PER_DAY;
	public static final int GRID_WIDTH_SIZE = 50;
	public static final int GRID_HEIGHT_SIZE = 50;
	public static final int MAX_PRICE = 5; // per hour
	public static final int MAX_DURATION_OF_STAY = 8; // TO_DO: ticks?
	public static final double MAX_PAYMENT = (1 * MAX_PRICE * MAX_DURATION_OF_STAY);
	public static final double MAX_EFFORT = (1
			* Math.sqrt(Math.pow(GRID_WIDTH_SIZE, 2) + Math.pow(GRID_HEIGHT_SIZE, 2)));
	private static Set<ParkingFacilityAgent> parkingFacilities = new HashSet<>();

	private ContainerController mainContainer;

	private Grid<Object> mainGrid;
	public static Random driverRandomGenerator;

	@Override
	public String getName() {
		return "Parking Lots Simulation";
	}

	@Override
	protected void launchJADE() {
		parseParams();
		driverRandomGenerator = new Random(driverGenerationSeed);
		currentSchedule = RunEnvironment.getInstance().getCurrentSchedule();

		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		mainContainer = rt.createMainContainer(p1);

		launchAgents();
	}

	private void parseParams() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		explorerDriverCount = params.getInteger("explorerDriverCount");
		guidedDriverCount = params.getInteger("guidedDriverCount");
		staticParkingFacilityCount = params.getInteger("staticParkingFacilityCount");
		dynamicParkingFacilityCount = params.getInteger("dynamicParkingFacilityCount");
		driverGenerationSeed = params.getInteger("driverGenerationSeed");
	}

	@Override
	public Context<?> build(Context<Object> context) {
		GridFactory factory = GridFactoryFinder.createGridFactory(null);
		GridBuilderParameters<Object> gridBuilderParameters = new GridBuilderParameters<Object>(new StrictBorders(),
				new SimpleGridAdder<Object>(), true, GRID_WIDTH_SIZE, GRID_HEIGHT_SIZE);
		mainGrid = factory.createGrid("Main Grid", context, gridBuilderParameters);

		return super.build(context);
	}

	private GridPoint generateRandomGridPoint() {
		return new GridPoint(driverRandomGenerator.nextInt(GRID_WIDTH_SIZE), driverRandomGenerator.nextInt(GRID_HEIGHT_SIZE));
	}

	public void launchAgents() {

		try {

			// create static parking facilities
			for (int i = 0; i < staticParkingFacilityCount; i++) {
				GridPoint location = generateRandomGridPoint();
				
				// TODO change to CSV file
				Random priceDecimalRandomGenerator = new Random();
				Random priceIntRandomGenerator = new Random();
				double price = priceDecimalRandomGenerator.nextFloat() + priceIntRandomGenerator.nextInt(2) + 1;
				double maxPricePerDay = priceDecimalRandomGenerator.nextFloat() + priceIntRandomGenerator.nextInt(13) + 9;
				
				StaticParkingFacilityAgent staticParkingFacility = new StaticParkingFacilityAgent(location, 1, price, maxPricePerDay);
				parkingFacilities.add(staticParkingFacility);
				staticParkingFacility.addBehaviour(new StaticParkingFacilityBehaviour(staticParkingFacility, mainGrid));
				mainContainer.acceptNewAgent("StaticParkingFacility" + i, staticParkingFacility).start();

				mainGrid.moveTo(staticParkingFacility, location.getX(), location.getY());
			}

			// create dynamic parking facilities
			for (int i = 0; i < dynamicParkingFacilityCount; i++) {
				GridPoint location = generateRandomGridPoint();
				
				// TODO change to CSV file
				Random priceDecimalRandomGenerator = new Random();
				Random priceIntRandomGenerator = new Random();
				double price = priceDecimalRandomGenerator.nextFloat() + priceIntRandomGenerator.nextInt(2) + 1;
				double maxPricePerDay = priceDecimalRandomGenerator.nextFloat() + priceIntRandomGenerator.nextInt(13) + 9;
				
				DynamicParkingFacilityAgent dynamicParkingFacility = new DynamicParkingFacilityAgent(location, 1, price, maxPricePerDay);
				parkingFacilities.add(dynamicParkingFacility);
				dynamicParkingFacility
						.addBehaviour(new DynamicParkingFacilityBehaviour(dynamicParkingFacility, mainGrid, currentSchedule));
				mainContainer.acceptNewAgent("DynamicParkingFacility" + i, dynamicParkingFacility).start();

				mainGrid.moveTo(dynamicParkingFacility, location.getX(), location.getY());
			}

			// create explorer driver agents
			for (int i = 0; i < explorerDriverCount; i++) {
				String id = "ExplorerDriver" + i;

				// Randomized from 7.5 to 8.5 hours. TODO: Change this according to day of week
				double durationOfStay = 7.5 + driverRandomGenerator.nextInt(2);

				GridPoint destination = generateRandomGridPoint();
				DriverAgent explorerDriver = new ExplorerDriverAgent(id, destination, durationOfStay);
				SequentialBehaviour driverBehaviour = new SequentialBehaviour();
				driverBehaviour.addSubBehaviour(
						new ExplorerDriverBehaviour(explorerDriver, mainGrid, parkingFacilities));

				driverBehaviour
						.addSubBehaviour(new SleepBehaviour(explorerDriver, (int) durationOfStay * TICKS_IN_HOUR));
				explorerDriver.addBehaviour(driverBehaviour);

				mainContainer.acceptNewAgent(id, explorerDriver).start();

				GridPoint start = generateRandomGridPoint();
				mainGrid.moveTo(explorerDriver, start.getX(), start.getY());
			}

			// create guided driver agents
			for (int i = 0; i < guidedDriverCount; i++) {
				String id = "GuidedDriver" + i;

				// Randomized from 7.5 to 8.5 hours. TODO: Change this according to day of week
				double durationOfStay = 7.5 + driverRandomGenerator.nextInt();

				GridPoint destination = generateRandomGridPoint();
				DriverAgent guidedDriver = new GuidedDriverAgent(id, destination, durationOfStay);

				SequentialBehaviour driverBehaviour = new SequentialBehaviour();
				driverBehaviour.addSubBehaviour(
						new GuidedDriverBehaviour(guidedDriver, mainGrid, parkingFacilities));
				driverBehaviour.addSubBehaviour(new SleepBehaviour(guidedDriver, (int) durationOfStay * TICKS_IN_HOUR));
				guidedDriver.addBehaviour(driverBehaviour);

				mainContainer.acceptNewAgent(id, guidedDriver).start();

				GridPoint start = generateRandomGridPoint();
				mainGrid.moveTo(guidedDriver, start.getX(), start.getY());
			}
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

}
