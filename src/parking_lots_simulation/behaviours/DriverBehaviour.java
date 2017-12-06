package parking_lots_simulation.behaviours;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import sajas.core.behaviours.TickerBehaviour;

public abstract class DriverBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 4252257290496119984L;
	/**
	 * All parking facilities open
	 */
	protected Set<ParkingFacilityAgent> parkingFacilities;

	/**
	 * Grid that represents the simulation space
	 */
	protected Grid<Object> mainGrid;

	/**
	 * Driver agent where this behaviour is acting
	 */
	protected DriverAgent driverAgent;

	/**
	 * Location of the favorite park
	 */
	private GridPoint parkingDestination;

	/**
	 * Parking facilities the driver cannot go into because it was full the first
	 * time he went there
	 */
	private Set<GridPoint> parkingFacilitiesToAvoid;

	public DriverBehaviour(DriverAgent driver, int period, Grid<Object> mainGrid,
			Set<ParkingFacilityAgent> parkingFacilities) {
		super(driver, period);
		this.driverAgent = driver;
		this.mainGrid = mainGrid;
		this.parkingFacilities = parkingFacilities;
		this.parkingFacilitiesToAvoid = new HashSet<>();
	}

	public abstract Entry<GridPoint, Double> getMostUsefulDestination(Set<GridPoint> parkingFacilitiesToAvoid);

	@Override
	public void onTick() {
		if (parkingDestination == null) {
			// check if utility is null
			Entry<GridPoint, Double> mostUsefulDestination = getMostUsefulDestination(parkingFacilitiesToAvoid);
			if (mostUsefulDestination.getValue() < 0) {
				// driver leaves the system
				// TODO: subtract utility from global value
				System.out.println(mostUsefulDestination.getValue());
				driverAgent.doDelete();
			}

			parkingDestination = mostUsefulDestination.getKey();

		}

		GridPoint agentPosition = mainGrid.getLocation(driverAgent);

		if (isAgentOnParkEntrance(mainGrid.getDistance(agentPosition, parkingDestination))) {
			ParkingFacilityAgent parkingFacility = (ParkingFacilityAgent) mainGrid
					.getObjectAt(parkingDestination.getX(), parkingDestination.getY());

			if (parkingFacility.isFull()) {
				// parkingFacilities.add(parkingFacility);
				// destination = null;
			} else {
				mainGrid.moveTo(driverAgent, parkingDestination.getX(), parkingDestination.getY());
				parkingFacility.parkCar(driverAgent);
				// TODO random time

				driverAgent.addBehaviour(new SleepBehaviour(driverAgent, parkingFacility, 5));
				this.stop();
			}
		} else {
			int x = directions(parkingDestination.getX(), agentPosition.getX());
			int y = directions(parkingDestination.getY(), agentPosition.getY());

			mainGrid.moveTo(driverAgent, agentPosition.getX() + x, agentPosition.getY() + y);
		}
	}

	/**
	 * @param park
	 * @param agent
	 * @return 0 if are on the same line/column, 1 or -1 on a different
	 */
	private int directions(int park, int agent) {

		int delta = park - agent;

		if (delta == 0) {
			return 0;
		} else if (delta > 0) {
			return 1;
		}

		return -1;
	}

	protected double getUtility(ParkingFacilityAgent parkingFacility) {
		double u = 0.9;
		double v = 0.9;

		double utility = driverAgent.getUtilityForArrivingAtDestination();
		double distanceToDestination = mainGrid.getDistance(driverAgent.getDestination(),
				mainGrid.getLocation(parkingFacility));

		double payment = DriverAgent.alpha * parkingFacility.getPrice() * driverAgent.getDurationOfStay();
		double effort = DriverAgent.beta * distanceToDestination;

		return utility - driverAgent.getPaymentEmphasis() * Math.pow(payment, u)
				- driverAgent.getWalkDistanceEmphasis() * Math.pow(effort, v);
	}

	/**
	 * If the driver agent is at sqrt(2) distance of a park facility
	 * 
	 * @param distance
	 *            distance between park and driver agent
	 * @return true if is at sqrt(2) or less, false otherwise
	 */
	private boolean isAgentOnParkEntrance(double distance) {

		if (distance <= Math.sqrt(2)) {
			return true;
		}

		return false;
	}
}
