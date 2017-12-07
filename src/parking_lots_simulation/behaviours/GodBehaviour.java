package parking_lots_simulation.behaviours;

import sajas.core.behaviours.Behaviour;
import parking_lots_simulation.Launcher;
import repast.simphony.engine.environment.RunEnvironment;

public class GodBehaviour extends Behaviour {

	@Override
	public void action() {
		int tickCount = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		generateAgents(getDayOfTheWeek(tickCount), getHour(tickCount), getMinute(tickCount));
	}

	/**
	 * 
	 * @param dayOfTheWeek
	 *            0 is monday, 1 is tuesday ... 6 is sunday
	 */
	private void generateAgents(int dayOfTheWeek, int hour, int minute) {
	}

	private int getDayOfTheWeek(int tickCount) {
		return ((int) tickCount / Launcher.TICKS_IN_HOUR) / 24 % 7;
	}

	private int getHour(int tickCount) {
		return ((int) tickCount / Launcher.TICKS_IN_HOUR) % 24;
	}

	private int getMinute(double tickCount) {
		return (int) (60 * tickCount) / Launcher.TICKS_IN_HOUR % 60;
	}

	@Override
	public boolean done() {
		return false;
	}
}