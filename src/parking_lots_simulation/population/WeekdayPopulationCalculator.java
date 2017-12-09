package parking_lots_simulation.population;

public class WeekdayPopulationCalculator implements PopulationCalculator {
	@Override
	public double calculate(double x) {
		return 7.4759504935373840e+001 + -4.3481968317186556e+001 * x + 1.1580478885389195e+002 * Math.pow(x, 2)
				+ -1.0759609355142878e+002 * Math.pow(x, 3) + 4.5786908581600990e+001 * Math.pow(x, 4)
				+ -9.8661450474507237e+000 * Math.pow(x, 5) + 1.1258434059543252e+000 * Math.pow(x, 6)
				+ -6.0614556706607120e-002 * Math.pow(x, 7) + 1.3220819520461547e-004 * Math.pow(x, 8)
				+ 1.4050841880529859e-004 * Math.pow(x, 9) + -4.4363687526606185e-006 * Math.pow(x, 10)
				+ -7.3979085365996366e-008 * Math.pow(x, 11) + 2.9845474270554181e-009 * Math.pow(x, 12)
				+ 3.1120350252867562e-011 * Math.pow(x, 13) + 5.8253886684231085e-012 * Math.pow(x, 14)
				+ -1.1545560770430154e-013 * Math.pow(x, 15) + -2.1647326313214739e-014 * Math.pow(x, 16)
				+ 9.5257543399641177e-016 * Math.pow(x, 17) + -1.1389794631568667e-017 * Math.pow(x, 18);
	}
}
