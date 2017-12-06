package parking_lots_simulation.behaviours;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.Set;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.Launcher;
import parking_lots_simulation.ParkingFacilityAgent;
import parking_lots_simulation.exceptions.NoValidDestinationException;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sajas.core.Agent;
import sajas.core.behaviours.TickerBehaviour;

public abstract class ParkSearchingBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -2526534492088802121L;

	/**
	 * Location of the favorite park
	 */
	protected ParkingFacilityAgent parkingDestination;
	protected DriverAgent driver;
	protected Grid<Object> grid;
	protected Set<ParkingFacilityAgent> fullParkingFacilities;
	protected Set<ParkingFacilityAgent> parkingFacilities;

	public ParkSearchingBehaviour(DriverAgent driver, int period, Grid<Object> grid,
			Set<ParkingFacilityAgent> parkingFacilities) {
		super(driver, period);
		this.driver = driver;
		this.grid = grid;
		this.parkingFacilities = parkingFacilities;
		fullParkingFacilities = new HashSet<>();
	}

	public abstract Entry<ParkingFacilityAgent, Double> getMostUsefulDestination(
			Set<ParkingFacilityAgent> fullParkingFacilities) throws NoValidDestinationException;

	@Override
	protected void onTick() {
		if (parkingDestination == null) {
			// check if utility is null
			Entry<ParkingFacilityAgent, Double> mostUsefulDestination;
			try {
				mostUsefulDestination = getMostUsefulDestination(
						fullParkingFacilities);
			} catch (NoValidDestinationException e) {
				Launcher.logger.log(Level.INFO, "No parking facility found.");
				driver.doDelete();
				return;
			}

			if (mostUsefulDestination.getValue() < 0) {
				// driver leaves the system
				// TODO: subtract utility from global value
				Launcher.logger.log(Level.INFO, "Negative utility value. Exiting system...");
				driver.doDelete();
			}

			parkingDestination = mostUsefulDestination.getKey();
		}

		GridPoint agentPosition = grid.getLocation(driver);

		if (isAdjacentTo(driver, parkingDestination.getLocation())) {

			if (parkingDestination.isFull()) {
				fullParkingFacilities.add(parkingDestination);
				parkingDestination = null;
			} else {
				grid.moveTo(driver, parkingDestination.getLocation().getX(), parkingDestination.getLocation().getY());
				parkingDestination.parkCar(driver);
				this.stop();
			}
		} else {
			int x = getDirectionToMoveTo(agentPosition.getX(), parkingDestination.getLocation().getX());
			int y = getDirectionToMoveTo(agentPosition.getY(), parkingDestination.getLocation().getY());

			grid.moveTo(driver, agentPosition.getX() + x, agentPosition.getY() + y);
		}
	}

	/**
	 * Returns -1, 0, 1 depending on the different between the destination and
	 * origin.
	 * 
	 * @param origin
	 * @param destination
	 * @return
	 */
	private int getDirectionToMoveTo(int origin, int destination) {
		int diff = destination - origin;
		
		if(diff == 0 ) {
			return 0;
		}
		else if(diff > 0) {
			return 1;
		}
		
		return -1;
	}

	/**
	 * If a1 is at sqrt(2) distance of a park facility Diagonals are included
	 * 
	 * @param distance
	 *            distance between park and driver agent
	 * @return true if is at sqrt(2) or less, false otherwise
	 */
	private boolean isAdjacentTo(Agent a1, GridPoint p1) {
		return grid.getDistance(grid.getLocation(a1), p1) <= Math.sqrt(2);
	}

	protected double getUtility(ParkingFacilityAgent parkingFacility) {
		double u = 0.9;
		double v = 0.9;

		double utility = driver.getUtilityForArrivingAtDestination();
		double distanceToDestination = grid.getDistance(driver.getDestination(), grid.getLocation(parkingFacility));

		double payment = DriverAgent.alpha * parkingFacility.getPrice() * driver.getDurationOfStay();
		double effort = DriverAgent.beta * distanceToDestination;

		return utility - driver.getPaymentEmphasis() * Math.pow(payment, u)
				- driver.getWalkDistanceEmphasis() * Math.pow(effort, v);
	}
}
