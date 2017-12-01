package parking_lots_simulation.behaviours;

import java.util.ArrayList;
import java.util.Iterator;

import parking_lots_simulation.GuidedDriverAgent;
import parking_lots_simulation.StaticParkingFacilityAgent;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.CyclicBehaviour;

public class GuidedDriverBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid<Object> mainGrid;
	private GuidedDriverAgent guidedDriverAgent;
	private ArrayList<StaticParkingFacilityAgent> staticParkingFacilities;
	private boolean done = false;

	public GuidedDriverBehaviour(GuidedDriverAgent guidedDriverAgent, Grid<Object> mainGrid, ArrayList<StaticParkingFacilityAgent> staticParkingFacilities) {
		super();
		this.guidedDriverAgent = guidedDriverAgent;
		this.mainGrid = mainGrid;
		this.staticParkingFacilities = staticParkingFacilities;
	}

	private int genCoordinate(int coord, int max) {
		return Math.min(Math.max(coord + (int) (Math.random()*3)-1, 0), max-1);
	}
	
	@Override
	public void action() {
		Iterator<StaticParkingFacilityAgent> it = staticParkingFacilities.iterator();
		
		while(it.hasNext()) {
			if(mainGrid.getLocation(it.next()).equals(mainGrid.getLocation(guidedDriverAgent))) {
				done = false;
				return;
			}
		}
		
		int x = genCoordinate(mainGrid.getLocation(guidedDriverAgent).getX(), mainGrid.getDimensions().getWidth());
		int y = genCoordinate(mainGrid.getLocation(guidedDriverAgent).getY(), mainGrid.getDimensions().getHeight());
		mainGrid.moveTo(guidedDriverAgent, x, y);
	}

	@Override
	public boolean done() {
		return done;
	}
}
