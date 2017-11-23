package parking_lots_simulation;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class RepastSServiceConsumerProviderLauncher extends RepastSLauncher{

	private static int N_EXPLORER_DRIVERS = 10;
	private static int N_GUIDED_DRIVERS = 10;

	private ContainerController mainContainer;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void launchJADE() {

		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		mainContainer = rt.createMainContainer(p1);

		launchAgents();
	}

	public void launchAgents() {

		try {

			// create explorer driver agents
			for (int i = 0; i < N_EXPLORER_DRIVERS; i++) {
				ExplorerDriverAgent ed = new ExplorerDriverAgent();
				mainContainer.acceptNewAgent("ExplorerDriver" + i, ed).start();
			}

			// create guided driver agents
			for (int i = 0; i < N_EXPLORER_DRIVERS; i++) {
				ExplorerDriverAgent gd = new ExplorerDriverAgent();
				mainContainer.acceptNewAgent("GuidedDriver" + i, gd).start();
			}

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

}
