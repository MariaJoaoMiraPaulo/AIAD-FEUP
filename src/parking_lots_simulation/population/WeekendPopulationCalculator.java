package parking_lots_simulation.population;

public class WeekendPopulationCalculator implements PopulationCalculator {
	@Override
	public double calculate(double x) {
		return 4.4650115275983417e+001 + -3.1345989640402735e+001 * x + 7.7796384043764249e+001 * Math.pow(x, 2)
				+ -5.7718150873272201e+001 * Math.pow(x, 3) + 1.8777895901782845e+001 * Math.pow(x, 4)
				+ -3.2030305573117501e+000 * Math.pow(x, 5) + 3.1632907712318309e-001 * Math.pow(x, 6)
				+ -1.8806224026828137e-002 * Math.pow(x, 7) + 6.6516435154752405e-004 * Math.pow(x, 8)
				+ -1.2909827085176139e-005 * Math.pow(x, 9) + 1.0595904434493052e-007 * Math.pow(x, 10);
	}
}
