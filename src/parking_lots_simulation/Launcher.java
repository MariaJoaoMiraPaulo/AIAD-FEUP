package parking_lots_simulation;

import java.util.HashSet;
import java.util.Set;

import java.util.logging.*;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import parking_lots_simulation.behaviours.DriverBehaviour;
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
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class Launcher extends RepastSLauncher {

	public static Logger logger = Logger.getGlobal();
	public int staticParkingFacilityCount = 5;
	public int dynamicParkingFacilityCount = 5;
	public int explorerDriverCount = 10;
	public int guidedDriverCount = 10;
	public int driverPeriod = 100;
	public static final int TICKS_IN_HOUR = 600000;
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

	@Override
	public String getName() {
		return "Parking Lots Simulation";
	}

	@Override
	protected void launchJADE() {
		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		mainContainer = rt.createMainContainer(p1);

		parseParams();
		launchAgents();
	}

	private void parseParams() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		explorerDriverCount = params.getInteger("explorerDriverCount");
		guidedDriverCount = params.getInteger("guidedDriverCount");
		staticParkingFacilityCount = params.getInteger("staticParkingFacilityCount");
		dynamicParkingFacilityCount = params.getInteger("dynamicParkingFacilityCount");
		driverPeriod = params.getInteger("driverPeriod");
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
		return new GridPoint(((int) (Math.random() * GRID_WIDTH_SIZE)), ((int) (Math.random() * GRID_HEIGHT_SIZE)));
	}

	public void launchAgents() {

		try {

			// create static parking facilities
			for (int i = 0; i < staticParkingFacilityCount; i++) {
				GridPoint location = generateRandomGridPoint();
				StaticParkingFacilityAgent staticParkingFacility = new StaticParkingFacilityAgent(location, 1, 5);
				parkingFacilities.add(staticParkingFacility);
				staticParkingFacility.addBehaviour(new StaticParkingFacilityBehaviour(staticParkingFacility, mainGrid));
				mainContainer.acceptNewAgent("StaticParkingFacility" + i, staticParkingFacility).start();

				mainGrid.moveTo(staticParkingFacility, location.getX(), location.getY());
			}

			// create dynamic parking facilities
			for (int i = 0; i < dynamicParkingFacilityCount; i++) {
				GridPoint location = generateRandomGridPoint();
				DynamicParkingFacilityAgent dynamicParkingFacility = new DynamicParkingFacilityAgent(location, 1, 10);
				parkingFacilities.add(dynamicParkingFacility);
				dynamicParkingFacility
						.addBehaviour(new DynamicParkingFacilityBehaviour(dynamicParkingFacility, mainGrid));
				mainContainer.acceptNewAgent("DynamicParkingFacility" + i, dynamicParkingFacility).start();

				mainGrid.moveTo(dynamicParkingFacility, location.getX(), location.getY());
			}

			// create explorer driver agents
			for (int i = 0; i < explorerDriverCount; i++) {
				String id = "ExplorerDriver" + i;

				// Randomized from 7.5 to 8.5 hours. TODO: Change this according to day of week
				double durationOfStay = 7.5 + Math.random();

				GridPoint destination = generateRandomGridPoint();
				DriverAgent explorerDriver = new ExplorerDriverAgent(id, destination, durationOfStay);
				DriverBehaviour driverBehaviour = new DriverBehaviour(explorerDriver, driverPeriod, mainGrid,
						parkingFacilities);
				driverBehaviour.addSubBehaviour(
						new ExplorerDriverBehaviour(explorerDriver, driverPeriod, mainGrid, parkingFacilities));

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
				double durationOfStay = 7.5 + Math.random();

				GridPoint destination = generateRandomGridPoint();
				DriverAgent guidedDriver = new GuidedDriverAgent(id, destination, durationOfStay);

				DriverBehaviour driverBehaviour = new DriverBehaviour(guidedDriver, driverPeriod, mainGrid,
						parkingFacilities);
				driverBehaviour.addSubBehaviour(
						new GuidedDriverBehaviour(guidedDriver, driverPeriod, mainGrid, parkingFacilities));
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