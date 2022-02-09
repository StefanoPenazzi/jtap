package projects.CTAP.dataset;

import com.google.inject.Inject;

import config.Config;
import core.dataset.LocationsMap;
import core.graph.geo.CityNode;

public class LocationsMapCTAP extends LocationsMap<CityNode> {

	private Config config;
	
	@Inject
	public LocationsMapCTAP(Config config) {
		super(config);
		this.config = config;
	}
	
	@Override
	public void initialization() {
		try {
			getLocationsFromNeo4J(CityNode.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
