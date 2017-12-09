package parking_lots_simulation;

import java.util.Set;

import parking_lots_simulation.Statistics;
import parking_lots_simulation.behaviours.GodBehaviour;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;
import sajas.wrapper.ContainerController;

public class GodAgent extends Agent {

	private final GodBehaviour godBehaviour;
	private Statistics statistics;

	public GodAgent(ContainerController mainContainer, Grid<Object> mainGrid,
			Set<ParkingFacilityAgent> parkingFacilities) {
		godBehaviour = new GodBehaviour(this, mainContainer, mainGrid, parkingFacilities);
	}

	protected void setup() {
		super.setup();
		addBehaviour(godBehaviour);
	}

	public void deleteDriver(DriverAgent driver) {
		godBehaviour.deleteDriver(driver);
	}
	
	public void addStatistics(Statistics statistics) {
		this.statistics = statistics;
	}
	
	public Statistics getStatistics() {
		return statistics;
	}
}
