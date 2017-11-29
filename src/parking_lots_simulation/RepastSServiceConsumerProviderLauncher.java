package parking_lots_simulation;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import sajas.core.Runtime;
import sajas.sim.repasts.RepastSLauncher;
import sajas.wrapper.ContainerController;

public class RepastSServiceConsumerProviderLauncher extends RepastSLauncher{

	private static int N_EXPLORER_DRIVERS = 10;
	private static int N_GUIDED_DRIVERS = 10;

	private ContainerController mainContainer;

	@Override
	public String getName() {
		return "Parking Lots Simulation";
	}

	@Override
	protected void launchJADE() {
		
		System.out.println("Boasssssss");

		Runtime rt = Runtime.instance();
		Profile p1 = new ProfileImpl();
		System.out.println(rt.toString());
		mainContainer = rt.createMainContainer(p1);
		
		//mainContainer = rt.createAgentContainer(p1);

		launchAgents();
	}
	
	@Override
	public Context build(Context<Object> context) {
		
		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>("Service Consumer/Provider network", context, true);
		netBuilder.buildNetwork();
		
		return super.build(context);
	}

	public void launchAgents() {

		try {
			System.out.println("Boasssssss1111");
			// create explorer driver agents
			for (int i = 0; i < N_EXPLORER_DRIVERS; i++) {
				ExplorerDriverAgent ed = new ExplorerDriverAgent();
				mainContainer.acceptNewAgent("ExplorerDriver" + i, ed).start();
				System.out.println("aaaaaaaaaaaaaaaa"); 
			}

			// create guided driver agents
			for (int i = 0; i < N_GUIDED_DRIVERS; i++) {
				ExplorerDriverAgent gd = new ExplorerDriverAgent();
				mainContainer.acceptNewAgent("GuidedDriver" + i, gd).start();
			}

		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}

}
