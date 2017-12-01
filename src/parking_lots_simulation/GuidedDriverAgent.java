package parking_lots_simulation;

import java.util.ArrayList;

import parking_lots_simulation.behaviours.GuidedDriverBehaviour;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;

public class GuidedDriverAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private Grid<Object> mainGrid;
	private ArrayList<StaticParkingFacilityAgent> staticParkingFacilities;


	public GuidedDriverAgent(Grid<Object> mainGrid, ArrayList<StaticParkingFacilityAgent> staticParkingFacilities) {
		this.mainGrid = mainGrid;
		this.staticParkingFacilities = staticParkingFacilities;
	}

	@Override
	public void addBehaviour(Behaviour b) {
		super.addBehaviour(b);
	}

	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new GuidedDriverBehaviour(this, mainGrid, staticParkingFacilities));
	}
}
