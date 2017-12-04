package parking_lots_simulation.behaviours;


import parking_lots_simulation.DriverAgent;
import parking_lots_simulation.ParkingFacilityAgent;
import sajas.core.behaviours.Behaviour;

public class SleepBehaviour extends Behaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3142786094110924472L;
	
	private boolean done = false;
	private DriverAgent driverAgent;
	private ParkingFacilityAgent parkingFacility;
	private int numberOfTicksRemaining;
	
	
	public SleepBehaviour(DriverAgent driverAgent, ParkingFacilityAgent parkingFacility ,int numberOfTicksRemaining) {
		this.driverAgent = driverAgent;
		this.parkingFacility = parkingFacility;
		this.numberOfTicksRemaining = numberOfTicksRemaining;
	}

	@Override
	public void action() {
		
		if(numberOfTicksRemaining == 0) {
			parkingFacility.removeCar(driverAgent);
			done = true;
			driverAgent.doDelete();
		}
		else {
			numberOfTicksRemaining--;
		}
		
	}

	@Override
	public boolean done() {
		return done;
	}

}
