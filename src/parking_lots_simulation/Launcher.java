package parking_lots_simulation;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import parking_lots_simulation.behaviours.DynamicParkingFacilityBehaviour;
import parking_lots_simulation.behaviours.StaticParkingFacilityBehaviour;
import parking_lots_simulation.debug.DynamicParkingFacilityAgent;
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
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class Launcher extends RepastSLauncher {

	public static Logger logger = Logger.getGlobal();
	public static int driverGenerationSeed;
	private boolean showDynamicParks = true;
	private ISchedule currentSchedule;
	private Statistics statistics;
	public static final int TICKS_IN_HOUR = 30;
	public static final int DAYS_PER_WEEK = 7;
	public static final int HOURS_PER_DAY = 24;
	public static final int TICKS_IN_WEEK = TICKS_IN_HOUR * DAYS_PER_WEEK * HOURS_PER_DAY;
	public static final int GRID_WIDTH_SIZE = 120;
	public static final int GRID_HEIGHT_SIZE = 80;
	public static final int MAX_PRICE = 5; // per hour
	public static final int MAX_DURATION_OF_STAY = 8; // TO_DO: ticks?
	public static final double MAX_PAYMENT = (1 * MAX_PRICE * MAX_DURATION_OF_STAY);
	public static final double MAX_EFFORT = (1
			* Math.sqrt(Math.pow(GRID_WIDTH_SIZE, 2) + Math.pow(GRID_HEIGHT_SIZE, 2)));
	private static Set<ParkingFacilityAgent> parkingFacilities = new HashSet<>();
	public static GodAgent god;

	private ContainerController mainContainer;

	private Grid<Object> mainGrid;

	@Override
	public String getName() {
		return "Parking Lots Simulation";
	}

	@Override
	protected void launchJADE() {
		parseParams();
		currentSchedule = RunEnvironment.getInstance().getCurrentSchedule();

		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		mainContainer = rt.createMainContainer(p1);

		launchAgents();

		god = new GodAgent(mainContainer, mainGrid, parkingFacilities);

		statistics = new Statistics();
		god.addStatistics(statistics);
		try {
			mainContainer.acceptNewAgent("god", god).start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

	private void parseParams() {
		Parameters params = RunEnvironment.getInstance().getParameters();
		driverGenerationSeed = params.getInteger("driverGenerationSeed");
		showDynamicParks = params.getBoolean("showDynamicParks");
	}

	@Override
	public Context<?> build(Context<Object> context) {
		GridFactory factory = GridFactoryFinder.createGridFactory(null);
		GridBuilderParameters<Object> gridBuilderParameters = new GridBuilderParameters<Object>(new StrictBorders(),
				new SimpleGridAdder<Object>(), true, GRID_WIDTH_SIZE, GRID_HEIGHT_SIZE);
		mainGrid = factory.createGrid("Main Grid", context, gridBuilderParameters);

		return super.build(context);
	}

	public void launchAgents() {
		// Verifying if the user wants dynamic parks or static parks
		if (showDynamicParks) { // Initialize Dynamic Parking Facilities
			initializeDynamicParkingFacility();
		} else { // Initialize Static Parking Facilities
			initializeStaticParkingFacility();
		}
	}

	public void initializeDynamicParkingFacility() {

		DynamicParkingFacilityAgent parking1 = new DynamicParkingFacilityAgent("Cabergerweg", new GridPoint(2, 46), 698,
				1.43, 20.00);
		launchDynamicParkingFacilities(parking1);
		DynamicParkingFacilityAgent parking2 = new DynamicParkingFacilityAgent("Sphinx-terrein", new GridPoint(35, 65),
				500, 2.22, 13.00);
		launchDynamicParkingFacilities(parking2);
		DynamicParkingFacilityAgent parking3 = new DynamicParkingFacilityAgent("De griend", new GridPoint(44, 71), 351,
				2.22, 13.00);
		launchDynamicParkingFacilities(parking3);
		DynamicParkingFacilityAgent parking4 = new DynamicParkingFacilityAgent("Bassin", new GridPoint(47, 49), 407,
				2.73, 25.00);
		launchDynamicParkingFacilities(parking4);
		DynamicParkingFacilityAgent parking5 = new DynamicParkingFacilityAgent("P + R station Maastricht",
				new GridPoint(58, 73), 335, 1.83, 13.00);
		launchDynamicParkingFacilities(parking5);
		DynamicParkingFacilityAgent parking6 = new DynamicParkingFacilityAgent("Mosae forum", new GridPoint(51, 55),
				1082, 2.73, 25.00);
		launchDynamicParkingFacilities(parking6);
		DynamicParkingFacilityAgent parking7 = new DynamicParkingFacilityAgent("Vrijthof", new GridPoint(61, 38), 1082,
				2.73, 25.00);
		launchDynamicParkingFacilities(parking7);
		DynamicParkingFacilityAgent parking8 = new DynamicParkingFacilityAgent("P + R meerssenerweg",
				new GridPoint(73, 69), 65, 1.89, 13.00);
		launchDynamicParkingFacilities(parking8);
		DynamicParkingFacilityAgent parking9 = new DynamicParkingFacilityAgent("O.L. vrouweparking",
				new GridPoint(79, 62), 350, 2.73, 25.00);
		launchDynamicParkingFacilities(parking9);
		DynamicParkingFacilityAgent parking10 = new DynamicParkingFacilityAgent("Plein 1992", new GridPoint(72, 34),
				449, 2.22, 13.00);
		launchDynamicParkingFacilities(parking10);
		DynamicParkingFacilityAgent parking11 = new DynamicParkingFacilityAgent("De colonel", new GridPoint(79, 17),
				297, 2.22, 13.00);
		launchDynamicParkingFacilities(parking11);
		DynamicParkingFacilityAgent parking12 = new DynamicParkingFacilityAgent("Bonnefantenmuseum",
				new GridPoint(90, 51), 303, 1.43, 25.00);
		launchDynamicParkingFacilities(parking12);
		DynamicParkingFacilityAgent parking13 = new DynamicParkingFacilityAgent("Brusselse poort",
				new GridPoint(88, 44), 610, 1.43, 25.00);
		launchDynamicParkingFacilities(parking13);

	}

	public void initializeStaticParkingFacility() {

		StaticParkingFacilityAgent parking1 = new StaticParkingFacilityAgent("Cabergerweg", new GridPoint(2, 46), 698,
				1.43, 20.00);
		launchStaticParkingFacilities(parking1);
		StaticParkingFacilityAgent parking2 = new StaticParkingFacilityAgent("Sphinx-terrein", new GridPoint(35, 65),
				500, 2.22, 13.00);
		launchStaticParkingFacilities(parking2);
		StaticParkingFacilityAgent parking3 = new StaticParkingFacilityAgent("De griend", new GridPoint(44, 71), 351,
				2.22, 13.00);
		launchStaticParkingFacilities(parking3);
		StaticParkingFacilityAgent parking4 = new StaticParkingFacilityAgent("Bassin", new GridPoint(47, 49), 407, 2.73,
				25.00);
		launchStaticParkingFacilities(parking4);
		StaticParkingFacilityAgent parking5 = new StaticParkingFacilityAgent("P + R station Maastricht",
				new GridPoint(58, 73), 335, 1.83, 13.00);
		launchStaticParkingFacilities(parking5);
		StaticParkingFacilityAgent parking6 = new StaticParkingFacilityAgent("Mosae forum", new GridPoint(51, 55), 1082,
				2.73, 25.00);
		launchStaticParkingFacilities(parking6);
		StaticParkingFacilityAgent parking7 = new StaticParkingFacilityAgent("Vrijthof", new GridPoint(61, 38), 1082,
				2.73, 25.00);
		launchStaticParkingFacilities(parking7);
		StaticParkingFacilityAgent parking8 = new StaticParkingFacilityAgent("P + R meerssenerweg",
				new GridPoint(73, 69), 65, 1.89, 13.00);
		launchStaticParkingFacilities(parking8);
		StaticParkingFacilityAgent parking9 = new StaticParkingFacilityAgent("O.L. vrouweparking",
				new GridPoint(79, 62), 350, 2.73, 25.00);
		launchStaticParkingFacilities(parking9);
		StaticParkingFacilityAgent parking10 = new StaticParkingFacilityAgent("Plein 1992", new GridPoint(72, 34), 449,
				2.22, 13.00);
		launchStaticParkingFacilities(parking10);
		StaticParkingFacilityAgent parking11 = new StaticParkingFacilityAgent("De colonel", new GridPoint(79, 17), 297,
				2.22, 13.00);
		launchStaticParkingFacilities(parking11);
		StaticParkingFacilityAgent parking12 = new StaticParkingFacilityAgent("Bonnefantenmuseum",
				new GridPoint(90, 51), 303, 1.43, 25.00);
		launchStaticParkingFacilities(parking12);
		StaticParkingFacilityAgent parking13 = new StaticParkingFacilityAgent("Brusselse poort", new GridPoint(88, 44),
				610, 1.43, 25.00);
		launchStaticParkingFacilities(parking13);
	}

	public void launchStaticParkingFacilities(StaticParkingFacilityAgent staticPark) {
		try {
			parkingFacilities.add(staticPark);
			staticPark.addBehaviour(new StaticParkingFacilityBehaviour(staticPark, currentSchedule));
			mainContainer.acceptNewAgent(staticPark.getParkFacilityName(), staticPark).start();
			mainGrid.moveTo(staticPark, staticPark.getLocation().getX(), staticPark.getLocation().getY());
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

	public void launchDynamicParkingFacilities(DynamicParkingFacilityAgent dynamicPark) {
		try {
			parkingFacilities.add(dynamicPark);
			dynamicPark.addBehaviour(new DynamicParkingFacilityBehaviour(dynamicPark, currentSchedule));
			mainContainer.acceptNewAgent(dynamicPark.getParkFacilityName(), dynamicPark).start();
			mainGrid.moveTo(dynamicPark, dynamicPark.getLocation().getX(), dynamicPark.getLocation().getY());
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
}
