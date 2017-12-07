package parking_lots_simulation;

import java.util.Set;

import parking_lots_simulation.behaviours.GodBehaviour;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;
import sajas.wrapper.ContainerController;

public class GodAgent extends Agent {

	private final GodBehaviour godBehaviour;

	public GodAgent(ContainerController mainContainer, Grid<Object> mainGrid,
			Set<ParkingFacilityAgent> parkingFacilities) {
		godBehaviour = new GodBehaviour(mainContainer, mainGrid, parkingFacilities);
	}

	protected void setup() {
		super.setup();
		addBehaviour(godBehaviour);
	}

	public void deleteDriver(DriverAgent driver) {
		godBehaviour.deleteDriver(driver);
	}
}
