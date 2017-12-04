package parking_lots_simulation;

import java.util.HashSet;
import java.util.Set;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import parking_lots_simulation.behaviours.ExplorerDriverBehaviour;
import parking_lots_simulation.behaviours.GuidedDriverBehaviour;
import parking_lots_simulation.behaviours.StaticParkingFacilityBehaviour;
import parking_lots_simulation.behaviours.DynamicParkingFacilityBehaviour;
import parking_lots_simulation.debug.ExplorerDriverAgent;
import parking_lots_simulation.debug.GuidedDriverAgent;
import parking_lots_simulation.debug.StaticParkingFacilityAgent;
import parking_lots_simulation.debug.DynamicParkingFacilityAgent;
import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StrictBorders;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class RepastSServiceConsumerProviderLauncher extends RepastSLauncher{


	public static final int N_STATIC_PARKING_FACILITY = 5;
	public static final int N_DYNAMIC_PARKING_FACILITY = 5;
	public static final int N_EXPLORER_DRIVERS = 10;
	public static final int N_GUIDED_DRIVERS = 10;
	public static final int GRID_WIDTH_SIZE = 50;
	public static final int GRID_HEIGHT_SIZE = 50;
	public static final int MAX_PRICE = 5; //per hour
	public static final int MAX_DURATION_OF_STAY = 8; //TO_DO: ticks?
 	public static final double MAX_PAYMENT = (1 * MAX_PRICE * MAX_DURATION_OF_STAY);
	public static final double MAX_EFFORT =  (1 * Math.sqrt(Math.pow(GRID_WIDTH_SIZE,2) + Math.pow(GRID_HEIGHT_SIZE,2)));
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

		launchAgents();
	}
	
	@Override
	public Context<?> build(Context<Object> context) {
		GridFactory  factory = GridFactoryFinder.createGridFactory(null);
		GridBuilderParameters<Object> gridBuilderParameters = new GridBuilderParameters<Object>(new StrictBorders(), 
				new RandomGridAdder<Object>(), true, GRID_WIDTH_SIZE, GRID_HEIGHT_SIZE);
		mainGrid = factory.createGrid("Main Grid", context, gridBuilderParameters);
		
		return super.build(context);
	}
	
	public GridPoint generateRandomGridPoint() {
		return new GridPoint((0 + (int)(Math.random() * GRID_WIDTH_SIZE)), (0 + (int)(Math.random() * GRID_HEIGHT_SIZE)));
	}

	public void launchAgents() {
		try {
			
			// create static parking facilities
			for (int i = 0; i < N_STATIC_PARKING_FACILITY; i++) {
				StaticParkingFacilityAgent staticParkingFacility = new StaticParkingFacilityAgent(1,5);
				parkingFacilities.add(staticParkingFacility);
				staticParkingFacility.addBehaviour(new StaticParkingFacilityBehaviour(staticParkingFacility,mainGrid));
				mainContainer.acceptNewAgent("StaticParkingFacility" + i, staticParkingFacility).start();
			}
			
			// create dynamic parking facilities
			for (int i = 0; i < N_DYNAMIC_PARKING_FACILITY; i++) {
				DynamicParkingFacilityAgent dynamicParkingFacility = new DynamicParkingFacilityAgent(1,10);
				parkingFacilities.add(dynamicParkingFacility);
				dynamicParkingFacility.addBehaviour(new DynamicParkingFacilityBehaviour(dynamicParkingFacility,mainGrid));
				mainContainer.acceptNewAgent("DynamicParkingFacility" + i, dynamicParkingFacility).start();
			}
			
			// create explorer driver agents
			for (int i = 0; i < N_EXPLORER_DRIVERS; i++) {
				String id = "ExplorerDriver" + i;
				DriverAgent explorerDriver = new ExplorerDriverAgent(id,generateRandomGridPoint(),3);
				explorerDriver.addBehaviour(new ExplorerDriverBehaviour(explorerDriver, mainGrid, parkingFacilities));
				mainContainer.acceptNewAgent(id, explorerDriver).start();
			}

			// create guided driver agents
			for (int i = 0; i < N_GUIDED_DRIVERS; i++) {
				String id = "GuidedDriver" + i;
				DriverAgent guidedDriver = new GuidedDriverAgent(id, generateRandomGridPoint(),3);
				guidedDriver.addBehaviour(new GuidedDriverBehaviour(guidedDriver, mainGrid, parkingFacilities));
				mainContainer.acceptNewAgent(id, guidedDriver).start();
			}

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

}
