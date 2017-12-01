package parking_lots_simulation;

import java.util.ArrayList;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
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
	private static int N_EXPLORER_DRIVERS = 10;
	private static int N_GUIDED_DRIVERS = 10;
	private static int GRID_WIDTH_SIZE = 50;
	private static int GRID_HEIGHT_SIZE = 50;
	private static ArrayList<StaticParkingFacilityAgent> staticParkingFacilities = new ArrayList<>();

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
	public Context build(Context<Object> context) {
		GridFactory  factory = GridFactoryFinder.createGridFactory(null);
		GridBuilderParameters<Object> gridBuilderParameters = new GridBuilderParameters<Object>(new StrictBorders(), 
				new RandomGridAdder<Object>(), true, GRID_WIDTH_SIZE, GRID_HEIGHT_SIZE);
		mainGrid = factory.createGrid("Main Grid", context, gridBuilderParameters);
		
		return super.build(context);
	}

	public void launchAgents() {
		try {
			
			for (int i = 0; i < N_STATIC_PARKING_FACILITY; i++) {
				StaticParkingFacilityAgent spf = new StaticParkingFacilityAgent();
				staticParkingFacilities.add(spf);
				mainContainer.acceptNewAgent("StaticParkingFacility" + i, spf).start();
			}
			// create explorer driver agents
			for (int i = 0; i < N_EXPLORER_DRIVERS; i++) {
				ExplorerDriverAgent ed = new ExplorerDriverAgent();
				mainContainer.acceptNewAgent("ExplorerDriver" + i, ed).start();
			}

			// create guided driver agents
			for (int i = 0; i < N_GUIDED_DRIVERS; i++) {
				GuidedDriverAgent gd = new GuidedDriverAgent(mainGrid, staticParkingFacilities);
				mainContainer.acceptNewAgent("GuidedDriver" + i, gd).start();
			}

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
}
