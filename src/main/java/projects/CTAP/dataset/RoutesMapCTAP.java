package projects.CTAP.dataset;

import com.google.inject.Inject;

import config.Config;
import core.dataset.RoutesMap;
import core.graph.routing.RoutingManager;

public class RoutesMapCTAP extends RoutesMap {

	private RoutingManager rm;
	private Config config;
	
	@Inject
	public RoutesMapCTAP(Config config, RoutingManager rm) {
		super(config, rm);
		this.config = config;
		this.rm = rm;
	}
	
	@Override
	public void initialization() {
		
	}

}
