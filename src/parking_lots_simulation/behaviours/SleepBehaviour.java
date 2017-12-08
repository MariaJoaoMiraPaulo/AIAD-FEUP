package parking_lots_simulation.behaviours;

import java.util.logging.Level;

import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.Launcher;
import sajas.core.behaviours.Behaviour;

public class SleepBehaviour extends Behaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3142786094110924472L;

	private DriverAgent driver;
	private long numberOfTicksRemaining;

	public SleepBehaviour(DriverAgent driver, int ticksToSleep) {
		this.driver = driver;
		this.numberOfTicksRemaining = ticksToSleep;
	}

	@Override
	public void action() {
		if (numberOfTicksRemaining < 0) {
			if (!driver.isParked()) {
				System.err.println("Driver must be parked when this behaviour is created");
			}
			driver.getCurrentParkingFacility().removeCar(driver);
			Launcher.logger.log(Level.INFO, "Driver parking time exceeded. Exiting system...");
			Launcher.god.deleteDriver(driver);
		} else {
			numberOfTicksRemaining--;
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}
