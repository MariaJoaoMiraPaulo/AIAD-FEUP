package parking_lots_simulation;

import java.util.ArrayList;

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
import repast.simphony.space.grid.RandomGridAdder;
import repast.simphony.space.grid.StrictBorders;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class RepastSServiceConsumerProviderLauncher extends RepastSLauncher{

	private static final int N_STATIC_PARKING_FACILITY = 5;
	private static final int N_DYNAMIC_PARKING_FACILITY = 5;
	private static int N_EXPLORER_DRIVERS = 10;
	private static int N_GUIDED_DRIVERS = 10;
	private static int GRID_WIDTH_SIZE = 50;
	private static int GRID_HEIGHT_SIZE = 50;
	private static ArrayList<ParkingFacilityAgent> parkingFacilities = new ArrayList<>();

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

	public void launchAgents() {
		try {
			
			// create static parking facilities
			for (int i = 0; i < N_STATIC_PARKING_FACILITY; i++) {
				StaticParkingFacilityAgent staticParkingFacility = new StaticParkingFacilityAgent();
				parkingFacilities.add(staticParkingFacility);
				staticParkingFacility.addBehaviour(new StaticParkingFacilityBehaviour());
				mainContainer.acceptNewAgent("StaticParkingFacility" + i, staticParkingFacility).start();
			}
			
			// create dynamic parking facilities
			for (int i = 0; i < N_DYNAMIC_PARKING_FACILITY; i++) {
				DynamicParkingFacilityAgent dynamicParkingFacility = new DynamicParkingFacilityAgent();
				parkingFacilities.add(dynamicParkingFacility);
				dynamicParkingFacility.addBehaviour(new DynamicParkingFacilityBehaviour());
				mainContainer.acceptNewAgent("DynamicParkingFacility" + i, dynamicParkingFacility).start();
			}
			
			// create explorer driver agents
			for (int i = 0; i < N_EXPLORER_DRIVERS; i++) {
				DriverAgent explorerDriver = new ExplorerDriverAgent();
				explorerDriver.addBehaviour(new ExplorerDriverBehaviour(explorerDriver, mainGrid, parkingFacilities));
				mainContainer.acceptNewAgent("ExplorerDriver" + i, explorerDriver).start();
			}

			// create guided driver agents
			for (int i = 0; i < N_GUIDED_DRIVERS; i++) {
				DriverAgent guidedDriver = new GuidedDriverAgent();
				guidedDriver.addBehaviour(new GuidedDriverBehaviour(guidedDriver, mainGrid, parkingFacilities));
				mainContainer.acceptNewAgent("GuidedDriver" + i, guidedDriver).start();
			}

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
}
